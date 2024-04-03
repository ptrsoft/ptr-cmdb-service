package com.synectiks.asset.service;

import com.synectiks.asset.domain.ServiceQueue;
import com.synectiks.asset.repository.ServiceQueueRepository;
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
 * Service Implementation for managing {@link ServiceQueue}.
 */
@Service
public class ServiceQueueService {

	private final Logger logger = LoggerFactory.getLogger(ServiceQueueService.class);

	@Autowired
	private ServiceQueueRepository serviceQueueRepository;

	public ServiceQueue save(ServiceQueue serviceQueue) {
		logger.debug("Request to save serviceQueue : {}", serviceQueue);
		return serviceQueueRepository.save(serviceQueue);
	}

	@Transactional(readOnly = true)
	public List<ServiceQueue> findAll() {
		logger.debug("Request to get all ServiceQueue");
		return serviceQueueRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<ServiceQueue> findOne(Long id) {
		logger.debug("Request to get a serviceQueue : {}", id);
		return serviceQueueRepository.findById(id);
	}

	public void delete(Long id) {
		logger.debug("Request to delete a serviceQueue : {}", id);
		serviceQueueRepository.deleteById(id);
	}

	public ServiceQueue findByKey(String key){
		logger.debug("Get a config by key {}",key);
		return serviceQueueRepository.findByKey(key);
	}
	@Transactional(readOnly = true)
	public List<ServiceQueue> search(ServiceQueue serviceQueue) {
		logger.debug("Get all serviceQueue on given filters");
		return serviceQueueRepository.findAll(Example.of(serviceQueue), Sort.by(Sort.Direction.DESC, "id"));
	}

	public List<ServiceQueue> findByKeyAndStatus(String key, String status){
		logger.debug("Get all serviceQueue on key and status. key: {}, status: {}",key, status);
		return serviceQueueRepository.findByKeyAndStatus(key, status);
	}

}
