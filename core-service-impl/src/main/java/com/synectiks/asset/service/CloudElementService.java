package com.synectiks.asset.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.api.model.CloudElementDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.domain.Organization;
import com.synectiks.asset.domain.query.BiMappingBusinessCloudElementQueryObj;
import com.synectiks.asset.domain.query.CloudElementTagQueryObj;
import com.synectiks.asset.handler.CloudHandler;
import com.synectiks.asset.handler.factory.AwsHandlerFactory;
import com.synectiks.asset.repository.CloudElementRepository;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    private BusinessElementService businessElementService;

    @Autowired
    private LandingzoneService landingzoneService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JsonAndObjectConverterUtil jsonAndObjectConverterUtil;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private VaultService vaultService;

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
        StringBuilder primarySql = new StringBuilder("select ce.*, l.cloud  \n" +
                " from cloud_element ce \n" +
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
        if(!StringUtils.isBlank(cloudElementDTO.getCloud())){
            primarySql.append(" and upper(ce.cloud) = upper(?) ");
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

        if(!StringUtils.isBlank(cloudElementDTO.getLogLocation())){
            primarySql.append(" and ce.log_location = ? ");
        }
        if(!StringUtils.isBlank(cloudElementDTO.getTraceLocation())){
            primarySql.append(" and ce.trace_location = ? ");
        }
        if(!StringUtils.isBlank(cloudElementDTO.getMetricLocation())){
            primarySql.append(" and ce.metric_location = ? ");
        }

        Query query = entityManager.createNativeQuery(primarySql.toString(),CloudElement.class);
        int index = 0;
        if(cloudElementDTO.getId() != null){
            query.setParameter(++index, cloudElementDTO.getId());
        }
        if(!StringUtils.isBlank(cloudElementDTO.getElementType())){
            query.setParameter(++index, cloudElementDTO.getElementType());
        }
        if(!StringUtils.isBlank(cloudElementDTO.getCloud())){
            query.setParameter(++index, cloudElementDTO.getCloud());
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
        if(!StringUtils.isBlank(cloudElementDTO.getLogLocation())){
            query.setParameter(++index, cloudElementDTO.getLogLocation());
        }
        if(!StringUtils.isBlank(cloudElementDTO.getTraceLocation())){
            query.setParameter(++index, cloudElementDTO.getTraceLocation());
        }
        if(!StringUtils.isBlank(cloudElementDTO.getMetricLocation())){
            query.setParameter(++index, cloudElementDTO.getMetricLocation());
        }

        List<CloudElement> list = query.getResultList();
        for(CloudElement cloudElement: list){
            if(!StringUtils.isBlank(cloudElement.getElementType()) &&
                    Constants.LAMBDA.equalsIgnoreCase(cloudElement.getElementType())){
                getLambdaConfigMap(cloudElement);
            }
//            if(!StringUtils.isBlank(cloudElement.getElementType()) &&
//                    Constants.S3.equalsIgnoreCase(cloudElement.getElementType())){
//                getS3ConfigMap(cloudElement);
//            }
            if(!StringUtils.isBlank(cloudElement.getElementType()) &&
                    Constants.GLACIER.equalsIgnoreCase(cloudElement.getElementType())){
                getGlacierConfigMap(cloudElement);
            }
//            if(!StringUtils.isBlank(cloudElement.getElementType()) &&
//                    Constants.CDN.equalsIgnoreCase(cloudElement.getElementType())){
//                getCdnConfigMap(cloudElement);
//            }
        }

        return list;
	}

    @Transactional(readOnly = true)
    public CloudElement findByInstanceId(String instanceId){
        return cloudElementRepository.findByInstanceId(instanceId);
    }

    @Transactional(readOnly = true)
    public CloudElement getCloudElementByLandingZoneAndInstanceId(Long landingZoneId, String instanceId){
        return cloudElementRepository.getCloudElementByLandingZoneAndInstanceId(landingZoneId, instanceId);
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

//    private void getS3ConfigMap(CloudElement cloudElement){
//        Map<String, Object> configMap = null;
//        if(cloudElement.getConfigJson() != null && !"null".equals(cloudElement.getConfigJson())){
//            configMap = cloudElement.getConfigJson();
//        }else if(cloudElement.getConfigJson() != null && "null".equals(cloudElement.getConfigJson())){
//            configMap = new HashMap<>();
//            setBlankMap(configMap);
//        }else{
//            configMap = new HashMap<>();
//            setBlankMap(configMap);
//        }
//        cloudElement.setConfigJson(configMap);
//    }
    //TODO: to be removed after getting actual values
//    private void setBlankMap(Map configMap){
//        configMap.put("bucketName", "project-files");
//        configMap.put("replication", "replicated-files");
//        configMap.put("objects", "136k");
//        configMap.put("dataTransfer", "125mb");
//        configMap.put("responseTime", "3.5ms");
//        configMap.put("errors", "95");
//        configMap.put("latency", "22");
//        configMap.put("totalStorage", "200mb");
//        configMap.put("requests", "230");
//        configMap.put("product", "Procurement");
//        configMap.put("environment", "PROD");
//    }
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
//    private void getCdnConfigMap(CloudElement cloudElement){
//        Map<String, Object> configMap = null;
//        if(cloudElement.getConfigJson() != null && !"null".equals(cloudElement.getConfigJson())){
//            configMap = cloudElement.getConfigJson();
//        }else if(cloudElement.getConfigJson() != null && "null".equals(cloudElement.getConfigJson())){
//            configMap = new HashMap<>();
//        }else{
//            configMap = new HashMap<>();
//        }
//
//        configMap.put("originName", "my-origin-server-1");
//        configMap.put("edges", "NorthStar");
//        configMap.put("request", "136k");
//        configMap.put("byteTransfer", "125mb");
//        configMap.put("cacheHit", "58%");
//        configMap.put("errors", "95");
//        configMap.put("latency", "150ms");
//        configMap.put("invalidation", "52");
//        configMap.put("byteHitRate", "58%");
//        configMap.put("product", "Procurement");
//        configMap.put("environment", "PROD");
//        cloudElement.setConfigJson(configMap);
//    }

    @Transactional
    public Map<String, Object> associateCloudElement(Map reqObj){
        logger.debug("Request to associate/tag a service (business-element) with infrastructure (cloud-element)");
        Long landingZoneId = null;
        if(reqObj.get("landingZoneId").getClass().getName().equalsIgnoreCase("java.lang.Integer")){
            landingZoneId =  ((Integer)reqObj.get("landingZoneId")).longValue();
        }else{
            landingZoneId = (Long)reqObj.get("landingZoneId");
        }
        CloudElement cloudElement = getCloudElementByLandingZoneAndInstanceId( landingZoneId, (String)reqObj.get("instanceId"));
        Map<String, Object> response = new HashMap<>();
        if(cloudElement == null){
            logger.warn("Cloud-element of given instance-id: {} not found. ",(String)reqObj.get("instanceId"));
            response.put("code", 5);
            return response;
        }
        ObjectMapper objectMapper = Constants.instantiateMapper();
        int errorCode = updateCloudElementAndBusinessElementAssociation(reqObj, cloudElement, objectMapper);
        if(errorCode != 0){
            logger.error("Some error during tag processing. Error code: {}",errorCode);
            response.put("code", errorCode);
            return response;
        }
        response.put("code", errorCode);
        response.put("CLOUD_ELEMENT", cloudElement);
        return response;
    }

    public int updateCloudElementAndBusinessElementAssociation(Map hostedServiceTagObject, CloudElement cloudElement, ObjectMapper objectMapper) {
        int errorCode = addServiceIdInHostedServices(cloudElement, hostedServiceTagObject, objectMapper);
        if(errorCode == 5){
            logger.error("There is some issue in updating hosted-services of cloud-element. cloud-element-type:{}, cloud-element-id: {}, instance-id: {}", cloudElement.getElementType(), cloudElement.getId(), cloudElement.getInstanceId());
            return errorCode;
        }else if(errorCode == 4){
            logger.error("Tag already exists");
            return errorCode;
        }
        if(errorCode == 0){
            logger.info("Completing tagging in cloud-element");
//        cloudElement.setHostedServices(map);
            cloudElement = save(cloudElement);
            logger.info("Cloud-element updated with business-element service id. Now updating business-element with cloud-element id");

            Long serviceId = null;
            if(hostedServiceTagObject.get("serviceId").getClass().getName().equalsIgnoreCase("java.lang.Integer")){
                serviceId = ((Integer) hostedServiceTagObject.get("serviceId")).longValue();
            }else{
                serviceId = (Long) hostedServiceTagObject.get("serviceId");
            }
            businessElementService.updateService(serviceId, cloudElement);
            logger.info("Tagging/association completed");
            return 0;
//            return cloudElement;
        }
        return 6;
    }

    public int addServiceIdInHostedServices(CloudElement cloudElement, Map reqObj, ObjectMapper objectMapper){
        if(objectMapper == null){
            objectMapper = Constants.instantiateMapper();
        }
        Map updatedHostedServiceMap = null;
        try{
            ArrayNode arrayNode = getArrayNodeFromHostedService(cloudElement, objectMapper);

            ObjectNode objectNode = objectMapper.createObjectNode();
            if(reqObj.get("instanceId") != null){
                objectNode.put("instanceId",(String) reqObj.get("instanceId"));
            }
            if(reqObj.get("serviceId").getClass().getName().equalsIgnoreCase("java.lang.Integer")){
                objectNode.put("serviceId",(Integer) reqObj.get("serviceId"));
            }else{
                objectNode.put("serviceId",(Long) reqObj.get("serviceId"));
            }

            if(reqObj.get("tag").getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
                objectNode.set("tag", jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, (Map)reqObj.get("tag"), JsonNode.class));
            }else if(reqObj.get("tag").getClass().getName().equalsIgnoreCase("com.fasterxml.jackson.databind.node.ObjectNode")){
                objectNode.set("tag", jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, (ObjectNode)reqObj.get("tag"), JsonNode.class));
            }


            boolean isTagFound = replaceIfTagFound(arrayNode, objectNode);
            if(!isTagFound){
                arrayNode.add(objectNode);
            }

            ObjectNode finalNode =  objectMapper.createObjectNode();
            finalNode.put("HOSTEDSERVICES", arrayNode);
            updatedHostedServiceMap = jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, finalNode, Map.class);
        }catch (Exception e){
            logger.error("Exception in updating cloud-element's hosted-services map. Error coode: 5 ",e);
            return 5;
        }
        cloudElement.setHostedServices(updatedHostedServiceMap);
        return 0;

    }

    public ArrayNode getArrayNodeFromHostedService(CloudElement cloudElement, ObjectMapper objectMapper) throws IOException {
        String hostedServiceJson = jsonAndObjectConverterUtil.convertObjectToJsonString(objectMapper, cloudElement.getHostedServices(), Map.class);
        JsonNode rootNode = null;
        ArrayNode arrayNode = null;
        if(!StringUtils.isBlank(hostedServiceJson) && !"null".equalsIgnoreCase(hostedServiceJson)){
            rootNode = objectMapper.readTree(hostedServiceJson);
            arrayNode = jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, rootNode.get("HOSTEDSERVICES"), ArrayNode.class);
        }else{
            arrayNode = objectMapper.createArrayNode();
        }
        return arrayNode;
    }

    List<CloudElement> getCloudElementsByLandingZoneIds(List<Long> landingZoneIdList){
        return cloudElementRepository.getCloudElementsByLandingZoneIds(landingZoneIdList);
    }

    public Map<Long, Object> autoAssociateCloudElement(Long orgId, String cloud){
        List<Landingzone> landingzoneList = landingzoneService.getLandingZoneByOrgId(orgId, cloud);
        List<Long> landingZoneIdList = landingzoneList.stream().map(Landingzone::getId).collect(Collectors.toList());
        List<CloudElement> cloudElementList = getCloudElementsByLandingZoneIds(landingZoneIdList);
        Map<Long, Object> response = new HashMap<>();
        for(CloudElement cloudElement: cloudElementList){
            if(!Constants.LAMBDA.equalsIgnoreCase(cloudElement.getElementType()) &&
                !Constants.S3.equalsIgnoreCase(cloudElement.getElementType())){
                CloudHandler cloudHandler = AwsHandlerFactory.getHandler(cloudElement.getElementType());
                Map processResult = cloudHandler.processTag(cloudElement);
                response.put(cloudElement.getId(), processResult);
            }
        }
        return response;
    }


    public void autoAssociateAwsTagExclusiveCloudElement(String elementType){
        List<Organization> organizationList = organizationService.findAll();
        CloudHandler cloudHandler = AwsHandlerFactory.getHandler(elementType);
        for(Organization org: organizationList){
            List<Landingzone> landingzoneList = landingzoneService.getLandingZoneByOrgId(org.getId(), Constants.AWS);
            List<Long> landingZoneIdList = landingzoneList.stream().map(Landingzone::getId).collect(Collectors.toList());
            List<CloudElement> cloudElementList = getCloudElementsByLandingZoneIds(landingZoneIdList);

            Map<Long, Object> response = new HashMap<>();
            for(CloudElement cloudElement: cloudElementList){
                try{
                    Map processResult = cloudHandler.processTag(cloudElement);
                    response.put(cloudElement.getId(), processResult);
                }catch(Exception e){
                    logger.error("Exception in getting S3 tag. Instance id: {}", cloudElement.getInstanceId());
                }
            }
        }
    }

    public boolean replaceIfTagFound(ArrayNode arrayNode, JsonNode jsonNode){
        boolean isExists = false;
        for (int i = 0; i < arrayNode.size(); i++) {
            JsonNode element = arrayNode.get(i);
            isExists = (element.get("serviceId").asLong() == jsonNode.get("serviceId").longValue()) && element.get("instanceId").equals(jsonNode.get("instanceId")) ;
            if(isExists){
                logger.info("Previous tag found. Replacing previous tag with new tag");
                arrayNode.insert(i, jsonNode);
                break;
            }
        }
        return isExists;
    }

    public ObjectNode getCloudCredsByCloudElementId(Long cloudElementId) throws JsonProcessingException {
        logger.info("Getting user's cloud credentials from vault");
        CloudElementDTO cloudElementDTO = new CloudElementDTO();
        cloudElementDTO.setId(cloudElementId);
        List<CloudElement> cloudElementList = search(cloudElementDTO);
        if(cloudElementList.size() == 0){
            logger.error("Cloud element not found. Given cloud element id : "+cloudElementId);
            return null;
        }
        logger.debug("Cloud element found. Given cloud element id : "+cloudElementId);
        CloudElement cloudElement = cloudElementList.get(0);
        String vaultKey =  vaultService.resolveVaultKey(cloudElement.getLandingzone().getDepartment().getOrganization().getName(), cloudElement.getLandingzone().getDepartment().getName(), cloudElement.getLandingzone().getCloud(), cloudElement.getLandingzone().getLandingZone());
        ObjectNode response = (ObjectNode)vaultService.getCloudCreds(vaultKey);
        logger.info("Returning user's cloud credentials");
        return response;
    }

    public List<BiMappingBusinessCloudElementQueryObj> getBiMappingCloudElementInstances(Long orgId, Long departmentId, Long productId, Long productEnvId, String elementType){
        logger.debug("Get list of cloud-element instances for bi mapping. organization id: {}, department id:{}, product id: {}, product-environment id: {}, element-type: {}", orgId, departmentId, productId, productEnvId,elementType);
        return cloudElementRepository.getBiMappingCloudElementInstances(orgId, departmentId, productId, productEnvId, elementType);
    }

    public Page<CloudElement> getAllCloudElementsOfOrganization(Long orgId, Integer pageNo, Integer pageSize){
        logger.info("Request to get all cloud-elements of an organization. Org id: {} ", orgId);
        return cloudElementRepository.getAllCloudElementsOfOrganization(orgId, PageRequest.of(pageNo.intValue(), pageSize.intValue()));
    }

}
