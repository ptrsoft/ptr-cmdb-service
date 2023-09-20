package com.synectiks.asset.service;

import com.synectiks.asset.api.model.CloudElementDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.domain.query.CloudElementTagQueryObj;
import com.synectiks.asset.repository.CloudElementRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private EntityManager entityManager;

    public CloudElement save(CloudElement cloudElement) {
        logger.debug("Request to save cloud element : {}", cloudElement);
        return cloudElementRepository.save(cloudElement);
    }

    @Transactional(readOnly = true)
    public List<CloudElement> findAll() {
        logger.debug("Request to get all cloud element");
        return cloudElementRepository.findAll(Sort.by(Direction.DESC, "id"));
    }

    @Transactional(readOnly = true)
    public Optional<CloudElement> findOne(Long id) {
        logger.debug("Request to get cloud element : {}", id);
        return cloudElementRepository.findById(id);
    }

    public void delete(Long id) {
        logger.debug("Request to delete cloud element : {}", id);
        cloudElementRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
	public List<CloudElement> search(CloudElementDTO cloudElementDTO) {
        logger.info("Search cloud element");
        StringBuilder primarySql = new StringBuilder("select ce.* from cloud_element ce\n" +
                "left join landingzone l on ce.landingzone_id = l.id\n" +
                "left join db_category dc on ce.db_category_id = dc.id\n" +
                "left join product_enclave pe on ce.product_enclave_id = pe.id " +
                "where 1 = 1 ");
        if(cloudElementDTO.getId() != null){
            primarySql.append(" and ce.id = ? ");
        }
        if(!StringUtils.isBlank(cloudElementDTO.getElementType())){
            primarySql.append(" and upper(ce.element_type) = upper(?) ");
        }
        if(!StringUtils.isBlank(cloudElementDTO.getArn())){
            primarySql.append(" and ce.arn = ? ");
        }
        if(!StringUtils.isBlank(cloudElementDTO.getInstanceId())){
            primarySql.append(" and ce.instance_id = ? ");
        }
        if(!StringUtils.isBlank(cloudElementDTO.getInstanceName())){
            primarySql.append(" and ce.instance_name = ? ");
        }
        if(!StringUtils.isBlank(cloudElementDTO.getCategory())){
            primarySql.append(" and upper(ce.category) = upper(?) ");
        }
        if(cloudElementDTO.getLandingzoneId() != null){
            primarySql.append(" and ce.landingzone_id = ? ");
        }
        if(cloudElementDTO.getDbCategoryId() != null){
            primarySql.append(" and ce.db_category_id = ? ");
        }
        if(cloudElementDTO.getProductEnclaveId() != null){
            primarySql.append(" and ce.product_enclave_id = ? ");
        }
        if(!StringUtils.isBlank(cloudElementDTO.getStatus())){
            primarySql.append(" and upper(ce.status) = upper(?) ");
        }
        if(!StringUtils.isBlank(cloudElementDTO.getCreatedBy())){
            primarySql.append(" and upper(ce.created_by) = upper(?) ");
        }
        if(!StringUtils.isBlank(cloudElementDTO.getUpdatedBy())){
            primarySql.append(" and upper(ce.updated_by) = upper(?) ");
        }
        Query query = entityManager.createNativeQuery(primarySql.toString(),CloudElement.class);
        int index = 0;
        if(cloudElementDTO.getId() != null){
            query.setParameter(++index, cloudElementDTO.getId());
        }
        if(!StringUtils.isBlank(cloudElementDTO.getElementType())){
            query.setParameter(++index, cloudElementDTO.getElementType());
        }
        if(!StringUtils.isBlank(cloudElementDTO.getArn())){
            query.setParameter(++index, cloudElementDTO.getArn());
        }
        if(!StringUtils.isBlank(cloudElementDTO.getInstanceId())){
            query.setParameter(++index, cloudElementDTO.getInstanceId());
        }
        if(!StringUtils.isBlank(cloudElementDTO.getInstanceName())){
            query.setParameter(++index, cloudElementDTO.getInstanceName());
        }
        if(!StringUtils.isBlank(cloudElementDTO.getCategory())){
            query.setParameter(++index, cloudElementDTO.getCategory());
        }
        if(cloudElementDTO.getLandingzoneId() != null){
            query.setParameter(++index, cloudElementDTO.getLandingzoneId());
        }
        if(cloudElementDTO.getDbCategoryId() != null){
            query.setParameter(++index, cloudElementDTO.getDbCategoryId());
        }
        if(cloudElementDTO.getProductEnclaveId() != null){
            query.setParameter(++index, cloudElementDTO.getProductEnclaveId());
        }
        if(!StringUtils.isBlank(cloudElementDTO.getStatus())){
            query.setParameter(++index, cloudElementDTO.getStatus());
        }
        if(!StringUtils.isBlank(cloudElementDTO.getCreatedBy())){
            query.setParameter(++index, cloudElementDTO.getCreatedBy());
        }
        if(!StringUtils.isBlank(cloudElementDTO.getUpdatedBy())){
            query.setParameter(++index, cloudElementDTO.getUpdatedBy());
        }
        List<CloudElement> list = query.getResultList();
        for(CloudElement cloudElement: list){
            if(!StringUtils.isBlank(cloudElement.getElementType()) &&
                    Constants.LAMBDA.equalsIgnoreCase(cloudElement.getElementType())){
                getLambdaConfigMap(cloudElement);
            }
            if(!StringUtils.isBlank(cloudElement.getElementType()) &&
                    Constants.S3.equalsIgnoreCase(cloudElement.getElementType())){
                getS3ConfigMap(cloudElement);
            }
            if(!StringUtils.isBlank(cloudElement.getElementType()) &&
                    Constants.GLACIER.equalsIgnoreCase(cloudElement.getElementType())){
                getGlacierConfigMap(cloudElement);
            }
            if(!StringUtils.isBlank(cloudElement.getElementType()) &&
                    Constants.CDN.equalsIgnoreCase(cloudElement.getElementType())){
                getCdnConfigMap(cloudElement);
            }
        }

        return list;
	}

    @Transactional(readOnly = true)
    public CloudElement findByInstanceId(String instanceId){
        return cloudElementRepository.findByInstanceId(instanceId);
    }

    @Transactional(readOnly = true)
    public CloudElement getCloudElementByArn(Long landingZoneId, String arn, String elementType) {
        logger.debug("Get all cloud-elements on given criteria");
        return cloudElementRepository.getCloudElementByArn(landingZoneId, arn, elementType);
    }

    @Transactional(readOnly = true)
    public CloudElement getCloudElementByInstanceId(Long landingZoneId, String instanceId, String elementType) {
        logger.debug("Get all cloud-elements on given criteria");
        return cloudElementRepository.getCloudElementByInstanceId(landingZoneId, instanceId, elementType);
    }

    @Transactional(readOnly = true)
    public CloudElement getCloudElementByArn(Long landingZoneId, Long serviceId, String instanceId) {
        logger.debug("Get all cloud-elements on given criteria");
        return cloudElementRepository.getCloudElementForTag(landingZoneId, serviceId, instanceId);
    }

    @Transactional(readOnly = true)
    public List<CloudElementTagQueryObj> getCloudElementTag(Long landingZoneId, String instanceId) {
        logger.debug("Get all tags of a landing-zone");
        return cloudElementRepository.getCloudElementTag(landingZoneId, instanceId);
    }

    private void getLambdaConfigMap(CloudElement cloudElement){
        Map<String, Object> configMap = null;
        if(cloudElement.getConfigJson() != null && !"null".equals(cloudElement.getConfigJson())){
            configMap = cloudElement.getConfigJson();
        }else if(cloudElement.getConfigJson() != null && "null".equals(cloudElement.getConfigJson())){
            configMap = new HashMap<>();
        }else{
            configMap = new HashMap<>();
        }

        configMap.put("responseTime", "0.3ms");
        configMap.put("duration", "0k");
        configMap.put("invocations", "125k");
        configMap.put("throttles", "1.2k");
        configMap.put("errors", "5");
        configMap.put("latency", "45");
        configMap.put("networkReceived", "10");
        configMap.put("requests", "100");
        configMap.put("product", "Procurement");
        configMap.put("environment", "PROD");
        cloudElement.setConfigJson(configMap);
    }

    private void getS3ConfigMap(CloudElement cloudElement){
        Map<String, Object> configMap = null;
        if(cloudElement.getConfigJson() != null && !"null".equals(cloudElement.getConfigJson())){
            configMap = cloudElement.getConfigJson();
        }else if(cloudElement.getConfigJson() != null && "null".equals(cloudElement.getConfigJson())){
            configMap = new HashMap<>();
            setBlankMap(configMap);
        }else{
            configMap = new HashMap<>();
            setBlankMap(configMap);
        }


        cloudElement.setConfigJson(configMap);
    }
    //TODO: to be removed after getting actual values
    private void setBlankMap(Map configMap){
        configMap.put("bucketName", "project-files");
        configMap.put("replication", "replicated-files");
        configMap.put("objects", "136k");
        configMap.put("dataTransfer", "125mb");
        configMap.put("responseTime", "3.5ms");
        configMap.put("errors", "95");
        configMap.put("latency", "22");
        configMap.put("totalStorage", "200mb");
        configMap.put("requests", "230");
        configMap.put("product", "Procurement");
        configMap.put("environment", "PROD");
    }
    private void getGlacierConfigMap(CloudElement cloudElement){
        Map<String, Object> configMap = null;
        if(cloudElement.getConfigJson() != null && !"null".equals(cloudElement.getConfigJson())){
            configMap = cloudElement.getConfigJson();
        }else if(cloudElement.getConfigJson() != null && "null".equals(cloudElement.getConfigJson())){
            configMap = new HashMap<>();
        }else{
            configMap = new HashMap<>();
        }

        configMap.put("vaultName", "data-storage-vault");
        configMap.put("replication", "replicated-vault");
        configMap.put("archive", "136k");
        configMap.put("dataTransfer", "125mb");
        configMap.put("responseTime", "3.5ms");
        configMap.put("errors", "95");
        configMap.put("latency", "22");
        configMap.put("requests", "52");
        configMap.put("totalStorage", "200mb");
        configMap.put("product", "Procurement");
        configMap.put("environment", "PROD");
        cloudElement.setConfigJson(configMap);
    }
    private void getCdnConfigMap(CloudElement cloudElement){
        Map<String, Object> configMap = null;
        if(cloudElement.getConfigJson() != null && !"null".equals(cloudElement.getConfigJson())){
            configMap = cloudElement.getConfigJson();
        }else if(cloudElement.getConfigJson() != null && "null".equals(cloudElement.getConfigJson())){
            configMap = new HashMap<>();
        }else{
            configMap = new HashMap<>();
        }

        configMap.put("originName", "my-origin-server-1");
        configMap.put("edges", "NorthStar");
        configMap.put("request", "136k");
        configMap.put("byteTransfer", "125mb");
        configMap.put("cacheHit", "58%");
        configMap.put("errors", "95");
        configMap.put("latency", "150ms");
        configMap.put("invalidation", "52");
        configMap.put("byteHitRate", "58%");
        configMap.put("product", "Procurement");
        configMap.put("environment", "PROD");
        cloudElement.setConfigJson(configMap);
    }
}
