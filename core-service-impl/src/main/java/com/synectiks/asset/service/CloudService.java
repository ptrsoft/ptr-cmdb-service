package com.synectiks.asset.service;

import com.synectiks.asset.api.model.CloudDTO;
import com.synectiks.asset.domain.Cloud;
import com.synectiks.asset.repository.CloudRepository;
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
public class CloudService {

	private final Logger logger = LoggerFactory.getLogger(CloudService.class);

	@Autowired
	private CloudRepository cloudRepository;

	@Autowired
	private EntityManager entityManager;

	public Cloud save(Cloud cloud) {
		logger.debug("Request to save cloud : {}", cloud);
		cloud.setElementType(cloud.getElementType().toUpperCase());
		cloud.setName(cloud.getName().toUpperCase());
		return cloudRepository.save(cloud);
	}

	@Transactional(readOnly = true)
	public List<Cloud> findAll() {
		logger.debug("Request to get all clouds");
		return cloudRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Cloud> findOne(Long id) {
		logger.debug("Request to get cloud : {}", id);
		return cloudRepository.findById(id);
	}

	public void delete(Long id) {
		logger.debug("Request to delete cloud : {}", id);
		cloudRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Cloud> search(CloudDTO cloudDTO) {
		logger.info("Search cloud");
		StringBuilder primarySql = new StringBuilder("select c.* from cloud c where 1 = 1 ");
		if(cloudDTO.getId() != null){
			primarySql.append(" and c.id = ? ");
		}
		if(cloudDTO.getElementType() != null){
			primarySql.append(" and upper(c.element_type) = upper(?) ");
		}
		if(cloudDTO.getName() != null){
			primarySql.append(" and upper(c.name) = upper(?) ");
		}
		if(!StringUtils.isBlank(cloudDTO.getStatus())){
			primarySql.append(" and upper(c.status) = upper(?) ");
		}
		if(!StringUtils.isBlank(cloudDTO.getCreatedBy())){
			primarySql.append(" and upper(c.created_by) = upper(?) ");
		}
		if(!StringUtils.isBlank(cloudDTO.getUpdatedBy())){
			primarySql.append(" and upper(c.updated_by) = upper(?) ");
		}
		Query query = entityManager.createNativeQuery(primarySql.toString(), Cloud.class);
		int index = 0;
		if(cloudDTO.getId() != null){
			query.setParameter(++index, cloudDTO.getId());
		}
		if(cloudDTO.getElementType() != null){
			query.setParameter(++index, cloudDTO.getElementType());
		}
		if(cloudDTO.getName() != null){
			query.setParameter(++index, cloudDTO.getName());
		}
		if(!StringUtils.isBlank(cloudDTO.getStatus())){
			query.setParameter(++index, cloudDTO.getStatus());
		}
		if(!StringUtils.isBlank(cloudDTO.getCreatedBy())){
			query.setParameter(++index, cloudDTO.getCreatedBy());
		}
		if(!StringUtils.isBlank(cloudDTO.getUpdatedBy())){
			query.setParameter(++index, cloudDTO.getUpdatedBy());
		}
		return query.getResultList();
	}


}
