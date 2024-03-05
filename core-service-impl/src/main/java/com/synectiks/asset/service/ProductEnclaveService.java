package com.synectiks.asset.service;

import com.synectiks.asset.domain.ProductEnclave;
import com.synectiks.asset.repository.ProductEnclaveRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ProductEnclave}.
 */
@Service
public class ProductEnclaveService {

	private final Logger logger = LoggerFactory.getLogger(ProductEnclaveService.class);

	@Autowired
	private ProductEnclaveRepository productEnclaveRepository;

	public ProductEnclave save(ProductEnclave productEnclave) {
		logger.debug("Request to save product-enclave : {}", productEnclave);
		return productEnclaveRepository.save(productEnclave);
	}

	@Transactional(readOnly = true)
	public List<ProductEnclave> findAll() {
		logger.debug("Request to get all product-enclaves");
		return productEnclaveRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<ProductEnclave> findOne(Long id) {
		logger.debug("Request to get a product-enclave. id: {}", id);
		return productEnclaveRepository.findById(id);
	}

	public void delete(Long id) {
		logger.debug("Request to delete a product-enclave. id: {}", id);
		productEnclaveRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<ProductEnclave> search(ProductEnclave productEnv) {
		logger.debug("Get all product-enclaves on given filters");
		return productEnclaveRepository.findAll(Example.of(productEnv), Sort.by(Sort.Direction.DESC, "name"));
	}

	public ProductEnclave findProductEnclave(String instanceId,Long departmentId, Long landingZoneId){
		logger.debug("Get product-enclaves on vpc id: {}, department id: {}, landing-zone-id: {}",instanceId, departmentId, landingZoneId);
		if(StringUtils.isBlank(instanceId)){
			logger.warn("vpc id is null");
			return null;
		}
		return productEnclaveRepository.findProductEnclave(instanceId, departmentId, landingZoneId);
	}
}
