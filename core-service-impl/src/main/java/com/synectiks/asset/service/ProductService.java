package com.synectiks.asset.service;

import com.synectiks.asset.domain.Product;
import com.synectiks.asset.repository.ProductRepository;
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
 * Service Implementation for managing {@link Product}.
 */
@Service
public class ProductService {

	private final Logger logger = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	private ProductRepository productRepository;

	public Product save(Product product) {
		logger.debug("Request to save product : {}", product);
		return productRepository.save(product);
	}

	@Transactional(readOnly = true)
	public List<Product> findAll() {
		logger.debug("Request to get all products");
		return productRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Product> findOne(Long id) {
		logger.debug("Request to get a product : {}", id);
		return productRepository.findById(id);
	}

	public void delete(Long id) {
		logger.debug("Request to delete a product : {}", id);
		productRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Product> search(Product productEnv) {
		logger.debug("Get all products on given filters");
		return productRepository.findAll(Example.of(productEnv), Sort.by(Sort.Direction.DESC, "name"));
	}

	@Transactional(readOnly = true)
	public Product getProduct(String productName, Long departmentId, Long orgId){
		logger.debug("Get product by product name: {}, department id: {}, organization id: {} ",productName, departmentId, orgId);
		return productRepository.getProduct(productName, departmentId, orgId);
	}

}
