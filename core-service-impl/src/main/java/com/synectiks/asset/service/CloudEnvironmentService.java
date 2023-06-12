package com.synectiks.asset.service;

import com.synectiks.asset.domain.CloudEnvironment;
import com.synectiks.asset.repository.CloudEnvironmentRepository;
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
public class CloudEnvironmentService {

	private final Logger logger = LoggerFactory.getLogger(CloudEnvironmentService.class);

	@Autowired
	private CloudEnvironmentRepository cloudEnvironmentRepository;

	@Autowired
	private DepartmentService departmentService;

    public CloudEnvironment save(CloudEnvironment cloudEnvironment) {
        logger.debug("Request to save CloudEnvironment : {}", cloudEnvironment);
        return cloudEnvironmentRepository.save(cloudEnvironment);
    }



    @Transactional(readOnly = true)
    public List<CloudEnvironment> findAll() {
        logger.debug("Request to get all CloudEnvironments");
        return cloudEnvironmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<CloudEnvironment> findOne(Long id) {
        logger.debug("Request to get CloudEnvironment : {}", id);
        return cloudEnvironmentRepository.findById(id);
    }

    public void delete(Long id) {
        logger.debug("Request to delete CloudEnvironment : {}", id);
        cloudEnvironmentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
	public List<CloudEnvironment> search(CloudEnvironment cloudEnvironment) {
		logger.debug("Get all cloudEnvironments on given filters");
        return cloudEnvironmentRepository.findAll(Example.of(cloudEnvironment), Sort.by(Sort.Direction.DESC, "id"));
	}


}
