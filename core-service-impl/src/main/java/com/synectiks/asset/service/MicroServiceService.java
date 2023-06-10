package com.synectiks.asset.service;

import com.synectiks.asset.domain.MicroService;
import com.synectiks.asset.repository.MicroServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link MicroService}.
 */
@Service
@Transactional
public class MicroServiceService {

    private final Logger logger = LoggerFactory.getLogger(MicroServiceService.class);

    @Autowired
    private MicroServiceRepository microServiceRepository;

	public MicroService save(MicroService microService) {
        logger.debug("Request to save MicroService: {}", microService);
        return microServiceRepository.save(microService);
    }

    @Transactional(readOnly = true)
    public List<MicroService> findAll() {
        logger.debug("Request to get all MicroServices");
        return microServiceRepository.findAll(Sort.by(Direction.DESC, "id"));
    }

    @Transactional(readOnly = true)
    public Optional<MicroService> findOne(Long id) {
        logger.debug("Request to get MicroService: {}", id);
        return microServiceRepository.findById(id);
    }

    public void delete(Long id) {
        logger.debug("Request to delete MicroService : {}", id);
        microServiceRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
	public List<MicroService> search(MicroService microService) {
		logger.debug("Request to get all MicroServices on given filters");
        return microServiceRepository.findAll(Example.of(microService),	Sort.by(Direction.DESC, "id"));
	}

}
