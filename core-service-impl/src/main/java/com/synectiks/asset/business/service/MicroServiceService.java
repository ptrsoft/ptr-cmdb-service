package com.synectiks.asset.business.service;

import com.synectiks.asset.business.domain.MicroService;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.repository.MicroServiceRepository;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Implementation for managing {@link MicroService}.
 */
@Service
@Transactional
public class MicroServiceService {

    private final Logger log = LoggerFactory.getLogger(MicroServiceService.class);

    @Autowired
    private MicroServiceRepository microServiceRepository;

	@Autowired
	private JsonAndObjectConverterUtil jsonAndObjectConverterUtil;
	
//	@Autowired
//	private CloudEnvironmentService cloudEnvironmentService;
//	
    public MicroService save(MicroService microService) {
        log.debug("Request to save MicroService: {}", microService);
        return microServiceRepository.save(microService);
    }

    public Optional<MicroService> partialUpdate(MicroService microService) {
        log.debug("Request to partially update MicroService : {}", microService);

        return microServiceRepository
            .findById(microService.getId())
            .map(existingMicroService -> {
                
                if (microService.getName() != null) {
                    existingMicroService.setName(microService.getName());
                }
                if (microService.getDepartment() != null) {
                    existingMicroService.setDepartment(microService.getDepartment());
                }
                if (microService.getProduct() != null) {
                    existingMicroService.setProduct(microService.getProduct());
                }
                if (microService.getEnvironment() != null) {
                    existingMicroService.setEnvironment(microService.getEnvironment());
                }
                if (microService.getServiceType() != null) {
                    existingMicroService.setServiceType(microService.getServiceType());
                }
                if (microService.getServiceTopology() != null) {
                    existingMicroService.setServiceTopology(microService.getServiceTopology());
                }
                if (microService.getBusinessLocation() != null) {
                    existingMicroService.setBusinessLocation(microService.getBusinessLocation());
                }
                if (microService.getSlaJson() != null) {
                    existingMicroService.setSlaJson(microService.getSlaJson());
                }
                if (microService.getCostJson() != null) {
                    existingMicroService.setCostJson(microService.getCostJson());
                }
                if (microService.getViewJson() != null) {
                    existingMicroService.setViewJson(microService.getViewJson());
                }
                if (microService.getConfigJson() != null) {
                    existingMicroService.setConfigJson(microService.getConfigJson());
                }
                if (microService.getComplianceJson() != null) {
                    existingMicroService.setComplianceJson(microService.getComplianceJson());
                }
				if (microService.getStatus() != null) {
                    existingMicroService.setStatus(microService.getStatus());
                }
                if (microService.getCreatedOn() != null) {
                    existingMicroService.setCreatedOn(microService.getCreatedOn());
                }
                if (microService.getUpdatedOn() != null) {
                    existingMicroService.setUpdatedOn(microService.getUpdatedOn());
                }
                if (microService.getUpdatedBy() != null) {
                    existingMicroService.setUpdatedBy(microService.getUpdatedBy());
                }
                if (microService.getCreatedBy() != null) {
                    existingMicroService.setCreatedBy(microService.getCreatedBy());
                }

                return existingMicroService;
            })
            .map(microServiceRepository::save);
    }

    @Transactional(readOnly = true)
    public List<MicroService> findAll() {
        log.debug("Request to get all MicroServices");
        return microServiceRepository.findAll(Sort.by(Direction.DESC, "id"));
    }

    @Transactional(readOnly = true)
    public Optional<MicroService> findOne(Long id) {
        log.debug("Request to get MicroService: {}", id);
        return microServiceRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete MicroService : {}", id);
        microServiceRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
	public List<MicroService> search(Map<String, String> filter) throws IOException {
		log.debug("Request to get all MicroServices on given filters");

		MicroService microService = jsonAndObjectConverterUtil
				.convertSourceObjectToTarget(Constants.instantiateMapper(), filter, MicroService.class);

		// unset default value if createdOn is not coming in filter
		if (StringUtils.isBlank(filter.get(Constants.CREATED_ON))) {
			microService.setCreatedOn(null);
		}
		// unset default value if updatedOn is not coming in filter
		if (StringUtils.isBlank(filter.get(Constants.UPDATED_ON))) {
			microService.setUpdatedOn(null);
		}
		List<MicroService> list = microServiceRepository.findAll(Example.of(microService),
				Sort.by(Direction.DESC, "id"));

		return list;
	}

}
