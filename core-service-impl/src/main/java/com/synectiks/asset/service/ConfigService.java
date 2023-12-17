package com.synectiks.asset.service;

import com.synectiks.asset.domain.Config;
import com.synectiks.asset.repository.ConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public List<Config> search(Config config) {
		logger.debug("Get all config on given filters");
		return configRepository.findAll(Example.of(config), Sort.by(Sort.Direction.DESC, "id"));
	}


}
