package com.synectiks.asset.service;

import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.repository.CloudElementRepository;
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
 * Service Implementation for managing {@link CloudElement}.
 */
@Service
@Transactional
public class CloudElementService {

    private final Logger logger = LoggerFactory.getLogger(CloudElementService.class);

    @Autowired
    private CloudElementRepository cloudElementRepository;

    public CloudElement save(CloudElement cloudElement) {
        logger.debug("Request to save CloudElement : {}", cloudElement);
        return cloudElementRepository.save(cloudElement);
    }

    @Transactional(readOnly = true)
    public List<CloudElement> findAll() {
        logger.debug("Request to get all CloudElement");
        return cloudElementRepository.findAll(Sort.by(Direction.DESC, "id"));
    }

    @Transactional(readOnly = true)
    public Optional<CloudElement> findOne(Long id) {
        logger.debug("Request to get CloudElement : {}", id);
        return cloudElementRepository.findById(id);
    }

    public void delete(Long id) {
        logger.debug("Request to delete CloudElement : {}", id);
        cloudElementRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
	public List<CloudElement> search(CloudElement cloudElement) {
        logger.info("Search cloud element");
		return cloudElementRepository.findAll(Example.of(cloudElement),
				Sort.by(Direction.DESC, "id"));
	}

}
