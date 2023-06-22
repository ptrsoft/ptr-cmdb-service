package com.synectiks.asset.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synectiks.asset.domain.Organization;
import com.synectiks.asset.repository.OrganizationRepository;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;

@Service
public class OrganizationService {
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    @Autowired
    private OrganizationRepository organizationRepository;

	@Autowired
	private JsonAndObjectConverterUtil jsonAndObjectConverterUtil;

    public Organization save(Organization organization) {
    	logger.debug("Save Organization : {}", organization);
        return organizationRepository.save(organization);
    }

    @Transactional(readOnly = true)
    public List<Organization> findAll(){
    	logger.debug("Get all Organizations");
        return organizationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Organization> findOne(Long id) {
    	logger.debug("Get Organization : {}", id);
        return organizationRepository.findById(id);
    }

    public void delete(Long id) {
    	logger.debug("Request to delete Organization : {}", id);
        organizationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Organization> search(Organization organization) {
    	logger.debug("Search organizations on given filters");
        return organizationRepository.findAll(Example.of(organization), Sort.by(Sort.Direction.DESC, "name"));
    }
    
}
