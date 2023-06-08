package com.synectiks.asset.business.service;

import com.synectiks.asset.business.domain.CloudElement;
import com.synectiks.asset.business.domain.CloudEnvironment;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.repository.CloudElementRepository;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;
import com.synectiks.asset.web.rest.errors.BadRequestAlertException;
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
 * Service Implementation for managing {@link CloudElement}.
 */
@Service
@Transactional
public class CloudElementService {

    private final Logger log = LoggerFactory.getLogger(CloudElementService.class);

    @Autowired
    private CloudElementRepository cloudElementRepository;

	@Autowired
	private JsonAndObjectConverterUtil jsonAndObjectConverterUtil;
	
	@Autowired
	private CloudEnvironmentService cloudEnvironmentService;
	
    public CloudElement save(CloudElement cloudElement) {
        log.debug("Request to save CloudElement : {}", cloudElement);
        return cloudElementRepository.save(cloudElement);
    }

    public Optional<CloudElement> partialUpdate(CloudElement cloudElement) {
        log.debug("Request to partially update CloudElement : {}", cloudElement);

        return cloudElementRepository
            .findById(cloudElement.getId())
            .map(existingCloudElement -> {
                
                if (cloudElement.getInstanceId() != null) {
                    existingCloudElement.setInstanceId(cloudElement.getInstanceId());
                }
                if (cloudElement.getElementType() != null) {
                    existingCloudElement.setElementType(cloudElement.getElementType());
                }
                if (cloudElement.getArn() != null) {
                    existingCloudElement.setArn(cloudElement.getArn());
                }
                
                if (cloudElement.getCloudEntity() != null) {
                    existingCloudElement.setCloudEntity(cloudElement.getCloudEntity());
                }
                if (cloudElement.getHardwareLocation() != null) {
                    existingCloudElement.setHardwareLocation(cloudElement.getHardwareLocation());
                }
                if (cloudElement.getHostedServices() != null) {
                    existingCloudElement.setHostedServices(cloudElement.getHostedServices());
                }
                if (cloudElement.getSlaJson() != null) {
                    existingCloudElement.setSlaJson(cloudElement.getSlaJson());
                }
                if (cloudElement.getCostJson() != null) {
                    existingCloudElement.setCostJson(cloudElement.getCostJson());
                }
                if (cloudElement.getViewJson() != null) {
                    existingCloudElement.setViewJson(cloudElement.getViewJson());
                }
                if (cloudElement.getConfigJson() != null) {
                    existingCloudElement.setConfigJson(cloudElement.getConfigJson());
                }
                if (cloudElement.getComplianceJson() != null) {
                    existingCloudElement.setComplianceJson(cloudElement.getComplianceJson());
                }
                
                if (cloudElement.getTagStatus() != null) {
                    existingCloudElement.setTagStatus(cloudElement.getTagStatus());
                }
                
				if (cloudElement.getStatus() != null) {
                    existingCloudElement.setStatus(cloudElement.getStatus());
                }
                if(cloudElement.getCloudEnvironment() != null && cloudElement.getCloudEnvironment().getId() != null) {
					Optional<CloudEnvironment> od = cloudEnvironmentService.findOne(cloudElement.getCloudEnvironment().getId());
					if(od.isPresent()) {
						existingCloudElement.setCloudEnvironment(od.get());
					}else {
						throw new BadRequestAlertException("CloudEnvironment entity not found", "CloudElement", "parentidnotfound");
					}
				}
                if (cloudElement.getCreatedOn() != null) {
                    existingCloudElement.setCreatedOn(cloudElement.getCreatedOn());
                }
                if (cloudElement.getUpdatedOn() != null) {
                    existingCloudElement.setUpdatedOn(cloudElement.getUpdatedOn());
                }
                if (cloudElement.getUpdatedBy() != null) {
                    existingCloudElement.setUpdatedBy(cloudElement.getUpdatedBy());
                }
                if (cloudElement.getCreatedBy() != null) {
                    existingCloudElement.setCreatedBy(cloudElement.getCreatedBy());
                }

                return existingCloudElement;
            })
            .map(cloudElementRepository::save);
    }

    @Transactional(readOnly = true)
    public List<CloudElement> findAll() {
        log.debug("Request to get all CloudElement");
        return cloudElementRepository.findAll(Sort.by(Direction.DESC, "id"));
    }

    @Transactional(readOnly = true)
    public Optional<CloudElement> findOne(Long id) {
        log.debug("Request to get CloudElement : {}", id);
        return cloudElementRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete CloudElement : {}", id);
        cloudElementRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
	public List<CloudElement> search(Map<String, String> filter) throws IOException {
		log.debug("Request to get all CloudElements on given filters");

		CloudEnvironment cloudEnvironment = null;
		if (!StringUtils.isBlank(filter.get(Constants.CLOUD_ENVIRONMENT_ID))) {
			cloudEnvironment = new CloudEnvironment();
			cloudEnvironment.setId(Long.parseLong(filter.get(Constants.CLOUD_ENVIRONMENT_ID)));
			cloudEnvironment.setCreatedOn(null);
			cloudEnvironment.setUpdatedOn(null);
			filter.remove(Constants.CLOUD_ENVIRONMENT_ID);
		}else if(!StringUtils.isBlank(filter.get(Constants.LANDING_ZONE))) {
			cloudEnvironment = new CloudEnvironment();
			cloudEnvironment.setAccountId(filter.get(Constants.LANDING_ZONE));
			cloudEnvironment.setCreatedOn(null);
			cloudEnvironment.setUpdatedOn(null);
			filter.remove(Constants.LANDING_ZONE);
		}
		
		CloudElement cloudElement = jsonAndObjectConverterUtil
				.convertSourceObjectToTarget(Constants.instantiateMapper(), filter, CloudElement.class);

		if(cloudEnvironment != null) {
			cloudElement.setCloudEnvironment(cloudEnvironment);
		}
		// unset default value if createdOn is not coming in filter
		if (StringUtils.isBlank(filter.get(Constants.CREATED_ON))) {
			cloudElement.setCreatedOn(null);
		}
		// unset default value if updatedOn is not coming in filter
		if (StringUtils.isBlank(filter.get(Constants.UPDATED_ON))) {
			cloudElement.setUpdatedOn(null);
		}
		List<CloudElement> list = cloudElementRepository.findAll(Example.of(cloudElement),
				Sort.by(Direction.DESC, "id"));

		return list;
	}

}
