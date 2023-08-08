package com.synectiks.asset.service;

import com.synectiks.asset.domain.ProductEnv;
import com.synectiks.asset.repository.ProductEnvRepository;
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
 * Service Implementation for managing {@link ProductEnv}.
 */
@Service
public class ProductEnvService {

	private final Logger logger = LoggerFactory.getLogger(ProductEnvService.class);

	@Autowired
	private ProductEnvRepository productEnvRepository;

	public ProductEnv save(ProductEnv productEnv) {
		logger.debug("Request to save product-env : {}", productEnv);
		return productEnvRepository.save(productEnv);
	}

	@Transactional(readOnly = true)
	public List<ProductEnv> findAll() {
		logger.debug("Request to get all product-envs");
		return productEnvRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<ProductEnv> findOne(Long id) {
		logger.debug("Request to get a product-env : {}", id);
		return productEnvRepository.findById(id);
	}

	public void delete(Long id) {
		logger.debug("Request to delete a product-env : {}", id);
		productEnvRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<ProductEnv> search(ProductEnv productEnv) {
		logger.debug("Get all product-envs on given filters");
		return productEnvRepository.findAll(Example.of(productEnv), Sort.by(Sort.Direction.DESC, "name"));
	}


}
