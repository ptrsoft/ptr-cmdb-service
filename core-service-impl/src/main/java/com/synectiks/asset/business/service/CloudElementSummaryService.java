package com.synectiks.asset.business.service;

import com.synectiks.asset.business.domain.CloudElementSummary;
import com.synectiks.asset.business.domain.CloudEnvironment;
import com.synectiks.asset.repository.CloudElementSummaryRepository;
import com.synectiks.asset.web.rest.errors.BadRequestAlertException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CloudElementSummaryService {
	
	private static final Logger logger = LoggerFactory.getLogger(CloudElementSummaryService.class);
		
	@Autowired
	private CloudElementSummaryRepository cloudElementSummaryRepository;
	
	@Autowired
	private CloudEnvironmentService cloudEnvironmentService;
	
	public Optional<CloudElementSummary> getCloudElement(Long id) {
		logger.info("Get cloud element summary by id: {}", id);
		return cloudElementSummaryRepository.findById(id);
	}
	
	public List<CloudElementSummary> getAllCloudElement() {
		logger.info("Get all cloud element summary");
		return cloudElementSummaryRepository.findAll(Sort.by(Direction.DESC, "id"));
	}
	
	public Optional<CloudElementSummary> deleteCloudElement(Long id) {
		logger.info("Delete cloud element summary by id: {}", id);
		Optional<CloudElementSummary> oObj = getCloudElement(id);
		if(!oObj.isPresent()) {
			logger.warn("Id {} not found. cloud element cannot be deleted", id);
			return oObj;
		}
		cloudElementSummaryRepository.deleteById(id);
		return oObj;
	}
	
	public CloudElementSummary createCloudElement(CloudElementSummary obj){
		logger.info("Create new cloud element summary");
		return cloudElementSummaryRepository.save(obj);
	}
		
	public Optional<CloudElementSummary> partialUpdateCloudElement(CloudElementSummary obj){
		logger.info("Update cloud element summary partialy. Id: {}", obj.getId());
//		if(!cloudElementSummaryRepository.existsById(obj.getId())) {
//			throw new BadRequestAlertException("Entity not found", "CloudElementSummary", "idnotfound");
//		}
		
		Optional<CloudElementSummary> result = cloudElementSummaryRepository.findById(obj.getId())
			.map(existingObj ->{
				if(obj.getSummaryJson() != null) {
					existingObj.setSummaryJson(obj.getSummaryJson());
				}
				if(obj.getCloudEnvironment() != null && obj.getCloudEnvironment().getId() != null) {
					Optional<CloudEnvironment> od = cloudEnvironmentService.findOne(obj.getCloudEnvironment().getId());
					if(od.isPresent()) {
						existingObj.setCloudEnvironment(od.get());
					}else {
						throw new BadRequestAlertException("CloudEnvironment entity not found", "CloudElement", "parentidnotfound");
					}
				}
				
				return existingObj;
			})
			.map(cloudElementSummaryRepository::save);
		return result;
	}
	
	public List<CloudElementSummary> searchAllCloudElement(Map<String, String> obj) {
		logger.info("Search cloud element");
		CloudElementSummary cld = new CloudElementSummary();
		boolean isFilter = false;
		
		if(!StringUtils.isBlank(obj.get("id"))) {
			cld.setId(Long.parseLong(obj.get("id")));
			isFilter = true;
		}
		
		if(!StringUtils.isBlank(obj.get("cloudEnvironmentId"))) {
			Optional<CloudEnvironment> opd = cloudEnvironmentService.findOne(Long.parseLong(obj.get("cloudEnvironmentId")));
			if(opd.isPresent()) {
				cld.setCloudEnvironment(opd.get());
				isFilter = true;
			}else {
				return Collections.emptyList();
			}
		}

		
		List<CloudElementSummary> list = null;
		if(isFilter) {
			list = cloudElementSummaryRepository.findAll(Example.of(cld), Sort.by(Direction.DESC, "id"));
		}else {
			list = cloudElementSummaryRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		return list;
	}
	

	
}
