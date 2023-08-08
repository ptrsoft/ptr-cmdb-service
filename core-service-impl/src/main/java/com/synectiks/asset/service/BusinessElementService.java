package com.synectiks.asset.service;

import com.synectiks.asset.domain.BusinessElement;
import com.synectiks.asset.repository.BusinessElementRepository;
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
 * Service Implementation for managing {@link BusinessElement}.
 */
@Service
@Transactional
public class BusinessElementService {

    private final Logger logger = LoggerFactory.getLogger(BusinessElementService.class);

    @Autowired
    private BusinessElementRepository businessElementRepository;

	public BusinessElement save(BusinessElement businessElement) {
        logger.debug("Request to save business element: {}", businessElement);
        return businessElementRepository.save(businessElement);
    }

    @Transactional(readOnly = true)
    public List<BusinessElement> findAll() {
        logger.debug("Request to get all businessElement");
        return businessElementRepository.findAll(Sort.by(Direction.DESC, "id"));
    }

    @Transactional(readOnly = true)
    public Optional<BusinessElement> findOne(Long id) {
        logger.debug("Request to get business element: {}", id);
        return businessElementRepository.findById(id);
    }

    public void delete(Long id) {
        logger.debug("Request to delete business element : {}", id);
        businessElementRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
	public List<BusinessElement> search(BusinessElement businessElement) {
		logger.debug("Request to get all business element on given filters");
        return businessElementRepository.findAll(Example.of(businessElement),	Sort.by(Direction.DESC, "id"));
	}

}
