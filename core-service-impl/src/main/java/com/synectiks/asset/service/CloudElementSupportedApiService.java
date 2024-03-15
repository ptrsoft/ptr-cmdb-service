package com.synectiks.asset.service;

import com.synectiks.asset.api.model.CloudElementSupportedApiDTO;
import com.synectiks.asset.domain.CloudElementSupportedApi;
import com.synectiks.asset.repository.CloudElementSupportedApiRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Service
public class CloudElementSupportedApiService {

	private final Logger logger = LoggerFactory.getLogger(CloudElementSupportedApiService.class);

	@Autowired
	private CloudElementSupportedApiRepository cloudElementSupportedApiRepository;

	@Autowired
	private EntityManager entityManager;

	public CloudElementSupportedApi save(CloudElementSupportedApi cloudElementSupportedApi) {
		logger.debug("Request to save cloudElementSupportedApi : {}", cloudElementSupportedApi);
		return cloudElementSupportedApiRepository.save(cloudElementSupportedApi);
	}

	@Transactional(readOnly = true)
	public List<CloudElementSupportedApi> findAll() {
		logger.debug("Request to get all cloudElementSupportedApis");
		return cloudElementSupportedApiRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<CloudElementSupportedApi> findOne(Long id) {
		logger.debug("Request to get cloudElementSupportedApi : {}", id);
		return cloudElementSupportedApiRepository.findById(id);
	}

	public void delete(Long id) {
		logger.debug("Request to delete cloudElementSupportedApi : {}", id);
		cloudElementSupportedApiRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<CloudElementSupportedApi> search(CloudElementSupportedApiDTO cloudElementSupportedApiDTO) {
		logger.info("Search cloudElementSupportedApi");
		StringBuilder primarySql = new StringBuilder("select c.* from cloud_element_supported_api c where 1 = 1 ");
		if(cloudElementSupportedApiDTO.getId() != null){
			primarySql.append(" and c.id = ? ");
		}
		if(cloudElementSupportedApiDTO.getCloud() != null){
			primarySql.append(" and c.cloud = ? ");
		}
		if(cloudElementSupportedApiDTO.getElementType() != null){
			primarySql.append(" and c.element_type = ? ");
		}
		if(cloudElementSupportedApiDTO.getName() != null){
			primarySql.append(" and c.name = ? ");
		}
		if(cloudElementSupportedApiDTO.getFrames() != null){
			primarySql.append(" and c.frames = ? ");
		}
		if(!StringUtils.isBlank(cloudElementSupportedApiDTO.getStatus())){
			primarySql.append(" and upper(c.status) = upper(?) ");
		}
		if(!StringUtils.isBlank(cloudElementSupportedApiDTO.getCreatedBy())){
			primarySql.append(" and upper(c.created_by) = upper(?) ");
		}
		if(!StringUtils.isBlank(cloudElementSupportedApiDTO.getUpdatedBy())){
			primarySql.append(" and upper(c.updated_by) = upper(?) ");
		}
		Query query = entityManager.createNativeQuery(primarySql.toString(), CloudElementSupportedApi.class);
		int index = 0;
		if(cloudElementSupportedApiDTO.getId() != null){
			query.setParameter(++index, cloudElementSupportedApiDTO.getId());
		}
		if(cloudElementSupportedApiDTO.getCloud() != null){
			query.setParameter(++index, cloudElementSupportedApiDTO.getCloud());
		}
		if(cloudElementSupportedApiDTO.getElementType() != null){
			query.setParameter(++index, cloudElementSupportedApiDTO.getElementType());
		}
		if(cloudElementSupportedApiDTO.getName() != null){
			query.setParameter(++index, cloudElementSupportedApiDTO.getName());
		}
		if(!StringUtils.isBlank(cloudElementSupportedApiDTO.getStatus())){
			query.setParameter(++index, cloudElementSupportedApiDTO.getStatus());
		}
		if(!StringUtils.isBlank(cloudElementSupportedApiDTO.getCreatedBy())){
			query.setParameter(++index, cloudElementSupportedApiDTO.getCreatedBy());
		}
		if(!StringUtils.isBlank(cloudElementSupportedApiDTO.getUpdatedBy())){
			query.setParameter(++index, cloudElementSupportedApiDTO.getUpdatedBy());
		}
		return query.getResultList();
	}


}
