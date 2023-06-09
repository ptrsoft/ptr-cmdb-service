package com.synectiks.asset.service;

import com.synectiks.asset.domain.CloudEnvironment;
import com.synectiks.asset.repository.CloudEnvironmentRepository;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;
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

	@Autowired
	private JsonAndObjectConverterUtil jsonAndObjectConverterUtil;

    public CloudEnvironment save(CloudEnvironment cloudEnvironment) {
        logger.debug("Request to save CloudEnvironment : {}", cloudEnvironment);
//        cloudEnvironment.setAccountId(cloudEnvironment.getRoleArn().split(":")[4]);
        return cloudEnvironmentRepository.save(cloudEnvironment);
    }

    public Optional<CloudEnvironment> partialUpdate(CloudEnvironment cloudEnvironment) {
        logger.debug("Request to partially update CloudEnvironment : {}", cloudEnvironment);

        return cloudEnvironmentRepository
            .findById(cloudEnvironment.getId())
            .map(existingCloudEnvironment -> {
                if (cloudEnvironment.getDescription() != null) {
                    existingCloudEnvironment.setDescription(cloudEnvironment.getDescription());
                }
                if (cloudEnvironment.getAccountId() != null) {
                    existingCloudEnvironment.setAccountId(cloudEnvironment.getAccountId());
                }
//                if (cloudEnvironment.getVaultId() != null) {
//                    existingCloudEnvironment.setVaultId(cloudEnvironment.getVaultId());
//                }
//                if (cloudEnvironment.getDisplayName() != null) {
//                    existingCloudEnvironment.setDisplayName(cloudEnvironment.getDisplayName());
//                }
//                if (cloudEnvironment.getRoleArn() != null) {
//                    existingCloudEnvironment.setRoleArn(cloudEnvironment.getRoleArn());
//                }
//                if (cloudEnvironment.getExternalId() != null) {
//                    existingCloudEnvironment.setExternalId(cloudEnvironment.getExternalId());
//                }
                if (cloudEnvironment.getCloud() != null) {
                    existingCloudEnvironment.setCloud(cloudEnvironment.getCloud());
                }
                if (cloudEnvironment.getStatus() != null) {
                    existingCloudEnvironment.setStatus(cloudEnvironment.getStatus());
                }
                if (cloudEnvironment.getCreatedOn() != null) {
                    existingCloudEnvironment.setCreatedOn(cloudEnvironment.getCreatedOn());
                }
                if (cloudEnvironment.getUpdatedOn() != null) {
                    existingCloudEnvironment.setUpdatedOn(cloudEnvironment.getUpdatedOn());
                }
                if (cloudEnvironment.getUpdatedBy() != null) {
                    existingCloudEnvironment.setUpdatedBy(cloudEnvironment.getUpdatedBy());
                }
                if (cloudEnvironment.getCreatedBy() != null) {
                    existingCloudEnvironment.setCreatedBy(cloudEnvironment.getCreatedBy());
                }

				return existingCloudEnvironment;
            })
            .map(cloudEnvironmentRepository::save);
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


//    public List<EnvironmentCountsDto> getEnvironmentCounts(Long orgId) throws IOException {
//    	logger.debug("Getting cloud wise landing zone and their resource counts");
//    	return cloudEnvironmentRepository.getCount(orgId);
//    }
//
//    public EnvironmentCountsDto getEnvironmentCounts(String cloud, Long orgId) throws IOException {
//    	logger.debug("Getting cloud wise landing zone and their resource counts");
//    	return cloudEnvironmentRepository.getCount(cloud, orgId);
//    }
//
//    public List<EnvironmentDto> getEnvironmentSummary(Long orgId, String department, String product, String environment) throws IOException {
//        logger.debug("Getting organization wise environment summary");
//        List<EnvironmentSummaryDto> list = cloudEnvironmentRepository.getEnvironmentSummary(orgId, department, product, environment);
//        List<EnvironmentDto> environmentDtoList = filterEnvironmentSummary(list);
//        return environmentDtoList;
//    }
//
//    public List<EnvironmentDto> getEnvironmentSummary(String cloud, Long orgId) throws IOException {
//        logger.debug("Getting cloud and organization wise environment summary");
//        List<EnvironmentSummaryDto> list = cloudEnvironmentRepository.getEnvironmentSummary(cloud, orgId);
//        List<EnvironmentDto> environmentDtoList = filterEnvironmentSummary(list);
//        return environmentDtoList;
//    }

//    private List<EnvironmentDto> filterEnvironmentSummary(List<EnvironmentSummaryDto> list) {
//        Set<String> cloudSet = list.stream().map(EnvironmentSummaryDto::getCloud).collect(Collectors.toSet());
//        List<EnvironmentDto> environmentDtoList = new ArrayList<>();
//        for (Object obj: cloudSet){
//            String cloudName = (String)obj;
//            logger.debug("Getting list for cloud: {}", cloudName);
//            List<EnvironmentSummaryDto> filteredList = list.stream().filter(l -> !StringUtils.isBlank(l.getCloud()) && l.getCloud().equalsIgnoreCase(cloudName)).collect(Collectors.toList());
//            EnvironmentDto dto = EnvironmentDto.builder()
//                .cloud(cloudName)
//                .environmentSummaryList(filteredList)
//                .build();
//            environmentDtoList.add(dto);
//        }
//        return environmentDtoList;
//    }
}
