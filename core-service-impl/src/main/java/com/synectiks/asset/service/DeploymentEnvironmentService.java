package com.synectiks.asset.service;

import com.synectiks.asset.domain.DeploymentEnvironment;
import com.synectiks.asset.repository.DeploymentEnvironmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DeploymentEnvironmentService {

	private final Logger logger = LoggerFactory.getLogger(DeploymentEnvironmentService.class);

	@Autowired
	private DeploymentEnvironmentRepository deploymentEnvironmentRepository;

	public DeploymentEnvironment save(DeploymentEnvironment deploymentEnvironment) {
		logger.debug("Request to save DeploymentEnvironment : {}", deploymentEnvironment);
		return deploymentEnvironmentRepository.save(deploymentEnvironment);
	}

	@Transactional(readOnly = true)
	public List<DeploymentEnvironment> findAll() {
		logger.debug("Request to get all DeploymentEnvironments");
		return deploymentEnvironmentRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<DeploymentEnvironment> findOne(Long id) {
		logger.debug("Request to get DeploymentEnvironment : {}", id);
		return deploymentEnvironmentRepository.findById(id);
	}

	public void delete(Long id) {
		logger.debug("Request to delete DeploymentEnvironment : {}", id);
		deploymentEnvironmentRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<DeploymentEnvironment> search(DeploymentEnvironment deploymentEnvironment) {
		logger.debug("Get all DeploymentEnvironment on given filters");
		return deploymentEnvironmentRepository.findAll(Example.of(deploymentEnvironment), Sort.by(Sort.Direction.DESC, "name"));
	}


}
