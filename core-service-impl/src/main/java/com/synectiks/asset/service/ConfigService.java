package com.synectiks.asset.service;

import com.synectiks.asset.api.model.ConfigDTO;
import com.synectiks.asset.domain.Config;
import com.synectiks.asset.repository.ConfigRepository;
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

/**
 * Service Implementation for managing {@link Config}.
 */
@Service
public class ConfigService {

	private final Logger logger = LoggerFactory.getLogger(ConfigService.class);

	@Autowired
	private ConfigRepository configRepository;

	@Autowired
	private EntityManager entityManager;

	public Config save(Config config) {
		logger.debug("Request to save config : {}", config);
		return configRepository.save(config);
	}

	@Transactional(readOnly = true)
	public List<Config> findAll() {
		logger.debug("Request to get all configs");
		return configRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Config> findOne(Long id) {
		logger.debug("Request to get a config : {}", id);
		return configRepository.findById(id);
	}

	public void delete(Long id) {
		logger.debug("Request to delete a config : {}", id);
		configRepository.deleteById(id);
	}

	public Config findByKey(String key){
		logger.debug("Get a config by key {}",key);
		return configRepository.findByKey(key);
	}
	@Transactional(readOnly = true)
	public List<Config> search(ConfigDTO configDTO) {
		logger.info("Search cmdb config");
		StringBuilder primarySql = new StringBuilder("select c.* from config c where 1 = 1 ");
		
		if(configDTO.getId() != null){
			primarySql.append(" and c.id = ? ");
		}
		if(!StringUtils.isBlank(configDTO.getKey())){
			primarySql.append(" and upper(c.key) = upper(?) ");
		}
		if(!StringUtils.isBlank(configDTO.getValue())){
			primarySql.append(" and c.value = ? ");
		}
		if(!StringUtils.isBlank(configDTO.getStatus())){
			primarySql.append(" and upper(c.status) = upper(?) ");
		}
		if(!StringUtils.isBlank(configDTO.getCreatedBy())){
			primarySql.append(" and upper(c.created_by) = upper(?) ");
		}
		if(!StringUtils.isBlank(configDTO.getUpdatedBy())){
			primarySql.append(" and upper(c.updated_by) = upper(?) ");
		}
		if(configDTO.getIsEncrypted() != null){
			primarySql.append(" and c.is_encrypted = ? ");
		}
		if(configDTO.getOrganizationId() != null){
			primarySql.append(" and c.organization_id = ? ");
		}

		Query query = entityManager.createNativeQuery(primarySql.toString(), Config.class);
		int index = 0;
		if(configDTO.getId() != null){
			query.setParameter(++index, configDTO.getId());
		}
		if(!StringUtils.isBlank(configDTO.getKey())){
			query.setParameter(++index, configDTO.getKey());
		}
		if(!StringUtils.isBlank(configDTO.getValue())){
			query.setParameter(++index, configDTO.getValue());
		}
		if(!StringUtils.isBlank(configDTO.getStatus())){
			query.setParameter(++index, configDTO.getStatus());
		}
		if(!StringUtils.isBlank(configDTO.getCreatedBy())){
			query.setParameter(++index, configDTO.getCreatedBy());
		}
		if(!StringUtils.isBlank(configDTO.getUpdatedBy())){
			query.setParameter(++index, configDTO.getUpdatedBy());
		}
		if(configDTO.getIsEncrypted() != null){
			query.setParameter(++index, configDTO.getIsEncrypted());
		}
		if(configDTO.getOrganizationId() != null){
			query.setParameter(++index, configDTO.getOrganizationId());
		}

		return query.getResultList();
	}


}
