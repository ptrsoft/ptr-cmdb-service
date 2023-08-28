package com.synectiks.asset.service;

import com.synectiks.asset.api.model.CloudElementDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.repository.CloudElementRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
        }

        return list;
	}

    @Transactional(readOnly = true)
    public CloudElement findByInstanceId(String instanceId){
        return cloudElementRepository.findByInstanceId(instanceId);
    }

    @Transactional(readOnly = true)
    public List<CloudElement> getCloudElement(String organization, String department, String cloud, String landingZone, String arn) {
        logger.debug("Get all cloud-elements on given criteria");
        return cloudElementRepository.getCloudElement(organization, department, cloud, landingZone, arn);
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
}
