package com.synectiks.asset.service;

import com.synectiks.asset.api.model.BiServiceDTO;
import com.synectiks.asset.domain.BiService;
import com.synectiks.asset.repository.BiServiceRepository;
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
public class BiServiceService {

	private final Logger logger = LoggerFactory.getLogger(BiServiceService.class);

	@Autowired
	private BiServiceRepository biServiceRepository;

	@Autowired
	private EntityManager entityManager;

	public BiService save(BiService biService) {
		logger.debug("Request to save biService : {}", biService);
		return biServiceRepository.save(biService);
	}

	@Transactional(readOnly = true)
	public List<BiService> findAll() {
		logger.debug("Request to get all biServices");
		return biServiceRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<BiService> findOne(Long id) {
		logger.debug("Request to get biService : {}", id);
		return biServiceRepository.findById(id);
	}

	public void delete(Long id) {
		logger.debug("Request to delete biService : {}", id);
		biServiceRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<BiService> search(BiServiceDTO biServiceDTO) {
		logger.info("Search biService");
		StringBuilder primarySql = new StringBuilder("select c.* from bi_service c where 1 = 1 ");
		if(biServiceDTO.getId() != null){
			primarySql.append(" and c.id = ? ");
		}
		if(biServiceDTO.getProductCategory() != null){
			primarySql.append(" and upper(c.product_category) = upper(?) ");
		}
		if(biServiceDTO.getServiceCategory() != null){
			primarySql.append(" and upper(c.service_category) = upper(?) ");
		}

		if(biServiceDTO.getName() != null){
			primarySql.append(" and c.name = ? ");
		}
		if(!StringUtils.isBlank(biServiceDTO.getStatus())){
			primarySql.append(" and upper(c.status) = upper(?) ");
		}
		if(!StringUtils.isBlank(biServiceDTO.getCreatedBy())){
			primarySql.append(" and upper(c.created_by) = upper(?) ");
		}
		if(!StringUtils.isBlank(biServiceDTO.getUpdatedBy())){
			primarySql.append(" and upper(c.updated_by) = upper(?) ");
		}
		if(!StringUtils.isBlank(biServiceDTO.getServiceType())){
			primarySql.append(" and upper(c.service_type) = upper(?) ");
		}
		if(!StringUtils.isBlank(biServiceDTO.getServiceModule())){
			primarySql.append(" and upper(c.service_module) = upper(?) ");
		}
		Query query = entityManager.createNativeQuery(primarySql.toString(), BiService.class);
		int index = 0;
		if(biServiceDTO.getId() != null){
			query.setParameter(++index, biServiceDTO.getId());
		}
		if(biServiceDTO.getProductCategory() != null){
			query.setParameter(++index, biServiceDTO.getProductCategory());
		}
		if(biServiceDTO.getServiceCategory() != null){
			query.setParameter(++index, biServiceDTO.getServiceCategory());
		}
		if(biServiceDTO.getName() != null){
			query.setParameter(++index, biServiceDTO.getName());
		}
		if(!StringUtils.isBlank(biServiceDTO.getStatus())){
			query.setParameter(++index, biServiceDTO.getStatus());
		}
		if(!StringUtils.isBlank(biServiceDTO.getCreatedBy())){
			query.setParameter(++index, biServiceDTO.getCreatedBy());
		}
		if(!StringUtils.isBlank(biServiceDTO.getUpdatedBy())){
			query.setParameter(++index, biServiceDTO.getUpdatedBy());
		}
		if(!StringUtils.isBlank(biServiceDTO.getServiceType())){
			query.setParameter(++index, biServiceDTO.getServiceType());
		}
		if(!StringUtils.isBlank(biServiceDTO.getServiceModule())){
			query.setParameter(++index, biServiceDTO.getServiceModule());
		}
		return query.getResultList();
	}


}
