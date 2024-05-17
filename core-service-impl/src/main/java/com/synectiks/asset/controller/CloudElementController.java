package com.synectiks.asset.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.api.controller.CloudElementApi;
import com.synectiks.asset.api.model.CloudElementDTO;
import com.synectiks.asset.api.model.CloudElementTagDTO;
import com.synectiks.asset.api.model.DiscoveredResourceDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.domain.query.CloudElementTagQueryObj;
import com.synectiks.asset.mapper.CloudElementMapper;
import com.synectiks.asset.mapper.query.CloudElementTagMapper;
import com.synectiks.asset.repository.CloudElementRepository;
import com.synectiks.asset.service.BusinessElementService;
import com.synectiks.asset.service.CloudElementService;
import com.synectiks.asset.service.CloudElementSummaryService;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;
import com.synectiks.asset.web.rest.validation.Validator;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class CloudElementController implements CloudElementApi {

    private final Logger logger = LoggerFactory.getLogger(CloudElementController.class);

    private static final String ENTITY_NAME = "CloudElement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private CloudElementService cloudElementService;

    @Autowired
    private CloudElementSummaryService cloudElementSummaryService;

    @Autowired
    private BusinessElementService businessElementService;

    @Autowired
    private CloudElementRepository cloudElementRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private JsonAndObjectConverterUtil jsonAndObjectConverterUtil;

    @Override
    public ResponseEntity<CloudElementDTO> getCloudElement(Long id) {
        logger.info("REST request to get CloudElement : ID: {}", id);
        Optional<CloudElement> oObj = cloudElementService.findOne(id);
        CloudElementDTO cloudElementDTO = CloudElementMapper.INSTANCE.entityToDto(oObj.orElse(null));
        return ResponseUtil.wrapOrNotFound(Optional.of(cloudElementDTO));
    }

    @Override
    public ResponseEntity<List<CloudElementDTO>> getCloudElementList(){
        logger.info("REST request to get all CloudElements");
        List<CloudElement> cloudElementList = cloudElementService.findAll();
        List<CloudElementDTO> cloudElementDTOList = CloudElementMapper.INSTANCE.entityToDtoList(cloudElementList);
        return ResponseEntity.ok(cloudElementDTOList);
    }

    @Override
    public ResponseEntity<CloudElementDTO> addCloudElement(CloudElementDTO cloudElementDTO){
        logger.info("REST request to add CloudElement : {}", cloudElementDTO);
        validator.validateNotNull(cloudElementDTO.getId(), ENTITY_NAME);
        CloudElement cloudElement = CloudElementMapper.INSTANCE.dtoToEntity(cloudElementDTO);
        cloudElement = cloudElementService.save(cloudElement);
        CloudElementDTO result = CloudElementMapper.INSTANCE.entityToDto(cloudElement);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<CloudElementDTO> updateCloudElement(CloudElementDTO cloudElementDTO) {
        logger.info("REST request to update CloudElement : {}", cloudElementDTO);
        validator.validateNull(cloudElementDTO.getId(), ENTITY_NAME);
        validator.validateEntityExistsInDb(cloudElementDTO.getId(), ENTITY_NAME, cloudElementRepository);
        CloudElement existingCloudElement = cloudElementRepository.findById(cloudElementDTO.getId()).get();
        CloudElement tempCloudElement = CloudElementMapper.INSTANCE.dtoToEntityForUpdate(cloudElementDTO,existingCloudElement);
        CloudElement cloudElement = cloudElementService.save(tempCloudElement);
        CloudElementDTO result = CloudElementMapper.INSTANCE.entityToDto(cloudElement);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<CloudElementDTO>> searchCloudElement(CloudElementDTO cloudElementDTO) {
        logger.info("REST request to get all cloud-elements on given filters : {} ", cloudElementDTO);
        List<CloudElement> cloudElementList = cloudElementService.search(cloudElementDTO);
        List<CloudElementDTO> cloudElementDTOList = CloudElementMapper.INSTANCE.entityToDtoList(cloudElementList);
        return ResponseEntity.ok(cloudElementDTOList);
    }

    @Override
    public ResponseEntity<List<CloudElementTagDTO>> getCloudElementTag(Long landingZoneId, String instanceId) {
        logger.info("REST request to get all the tags of a landing-zone : LandingZoneId: {}", landingZoneId);
        List<CloudElementTagQueryObj> cloudElementTagQueryObjList = cloudElementService.getCloudElementTag(landingZoneId, instanceId);
        List<CloudElementTagDTO>  cloudElementTagDTOList = CloudElementTagMapper.INSTANCE.toDtoList(cloudElementTagQueryObjList);
        return ResponseUtil.wrapOrNotFound(Optional.of(cloudElementTagDTOList));
    }

    @Override
    public ResponseEntity<Object> deleteCloudElementTag(Long landingZoneId, String instanceId, Long serviceId) {
        logger.info("REST request to delete a tag. LandingZoneId: {}, instanceId: {}, serviceId: {}", landingZoneId,instanceId,serviceId);
        CloudElement cloudElement = cloudElementService.getCloudElementByArn(landingZoneId,serviceId,instanceId);
        if(cloudElement != null){
            ObjectMapper objectMapper = Constants.instantiateMapper();
            try{
                String js = jsonAndObjectConverterUtil.convertObjectToJsonString(objectMapper, cloudElement.getHostedServices(), Map.class);
                JsonNode rootNode = null;

                if(!StringUtils.isBlank(js) && !"null".equalsIgnoreCase(js)){
                    rootNode = objectMapper.readTree(js);
                    ArrayNode arrayNode = null;
                    arrayNode = jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, rootNode.get("HOSTEDSERVICES"), ArrayNode.class);
                    ArrayNode updatedArrayNode = objectMapper.createArrayNode();
                    Iterator<JsonNode> nodeIterator = arrayNode.iterator();
                    while (nodeIterator.hasNext()) {
                        JsonNode elementNode = nodeIterator.next();
                        if(elementNode.get("serviceId").asLong() != serviceId){
                            updatedArrayNode.add(elementNode);
                        }
                    }
                    ObjectNode finalNode =  objectMapper.createObjectNode();
                    finalNode.put("HOSTEDSERVICES", updatedArrayNode);
                    Map map = jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, finalNode, Map.class);
                    cloudElement.setHostedServices(map);
                    cloudElementService.save(cloudElement);
                }
            }catch (IOException e){
                logger.error("IOException ", e);
                return ResponseUtil.wrapOrNotFound(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR));
            }
        }
        return ResponseUtil.wrapOrNotFound(Optional.of(HttpStatus.OK));

    }

    @Override
    public ResponseEntity<CloudElementDTO> associateCloudElement(Object obj){
        logger.info("REST request to associate a service with infrastructure or tag a business element");
        Map reqObj = (Map)obj;

        Map<String, Object> response = cloudElementService.associateCloudElement(reqObj);
        Integer errorCode = (Integer) response.get("code");
        if(errorCode == 5){
            logger.error("Some exception occurred or validation failed. Manual association/tagging cannot be done");
            HttpStatus s = HttpStatus.valueOf(418);
            return ResponseEntity.status(HttpStatus.valueOf(418)).body(null);
        }
        if(errorCode == 4){
            logger.error("Tag already exists. Manual association/tagging cannot be done");
            return ResponseEntity.status(HttpStatus.valueOf(419)).body(null);
        }
        if(errorCode == 6){
            logger.error("Manual association/tagging failed");
            return ResponseEntity.status(HttpStatus.valueOf(420)).body(null);
        }
        CloudElement cloudElement = (CloudElement) response.get("CLOUD_ELEMENT");
        if(cloudElement == null){
            logger.warn("Cloud-element not found. Manual association/tagging cannot be done");
            return ResponseEntity.status(HttpStatus.valueOf(421)).body(null);
        }
        CloudElementDTO cloudElementDTO = CloudElementMapper.INSTANCE.entityToDto(cloudElement);
        return ResponseUtil.wrapOrNotFound(Optional.of(cloudElementDTO));
    }

    @Override
    public ResponseEntity<Object> autoAssociateCloudElement(@PathVariable("orgId") Long orgId, @PathVariable("cloud") String cloud){
        logger.info("REST request to auto discover services and associated them with infrastructure");
        Map<Long, Object> response = cloudElementService.autoAssociateCloudElement(orgId, cloud);
        return ResponseUtil.wrapOrNotFound(Optional.of(response));
    }

    private static boolean isLambdaSechdulerRunning = false;
    private static boolean isS3SechdulerRunning = false;

    /**
     * It starts execution at 9:00:00 AM every day.
     * It waits for previous execution to be completed
     * @return
     */
//    @Scheduled(cron = "0 0 9 * * ?")
    @Override
    public ResponseEntity<Object> autoAssociateLambdaTags(){
        logger.debug("Auto scheduled rest api to discover lambda services and associated them with infrastructure");
        if(isLambdaSechdulerRunning){
            logger.info("Lambda service discovery is already running. Previous execution is not yet completed");
            return ResponseUtil.wrapOrNotFound(Optional.of(HttpStatus.PROCESSING));
        }
        isLambdaSechdulerRunning = true;
        try{
            cloudElementService.autoAssociateAwsTagExclusiveCloudElement(Constants.LAMBDA);
        }catch (Exception e){
            logger.error("Exception: ",e);
            isLambdaSechdulerRunning = false;
        }
        isLambdaSechdulerRunning = false;
        logger.debug("Auto scheduled discovery of lambda services completed");
        return ResponseUtil.wrapOrNotFound(Optional.of(HttpStatus.OK));
    }

//    @Override
//    public ResponseEntity<Object> autoAssociateS3Tags(){
//        logger.debug("Auto scheduled rest api to discover S3 services and associated them with infrastructure");
//        if(isS3SechdulerRunning){
//            logger.info("S3 service discovery is already running. Previous execution is not yet completed");
//            return ResponseUtil.wrapOrNotFound(Optional.of(HttpStatus.PROCESSING));
//        }
//    }

    /**
     * It starts execution at 8:00:00 AM every day.
     * It waits for previous execution to be completed
     * @return
     */
//    @Scheduled(cron = "0 0 8 * * ?")
    @Override
    public ResponseEntity<Object> autoAssociateS3Tags(){
        logger.debug("Auto scheduled rest api to discover s3 buckets and associated them with infrastructure");
        if(isS3SechdulerRunning){
            logger.info("S3 service discovery is already running. Previous execution is not yet completed");
            return ResponseUtil.wrapOrNotFound(Optional.of(HttpStatus.PROCESSING));
        }
        isS3SechdulerRunning = true;
        try{
            cloudElementService.autoAssociateAwsTagExclusiveCloudElement(Constants.S3);
        }catch (Exception e){
            logger.error("Exception: ",e);
            isS3SechdulerRunning = false;
            return ResponseUtil.wrapOrNotFound(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR));
        }
        isS3SechdulerRunning = false;
        logger.debug("Auto scheduled discovery of s3 buckets completed");
        return ResponseUtil.wrapOrNotFound(Optional.of(HttpStatus.OK));
    }

    @Override
    public ResponseEntity<Object> getCloudCredsByCloudElementId(Long cloudElementId){
        logger.info("Rest api to get user's cloud access credentials for cloud-element-id {}",cloudElementId);
        ObjectNode response = null;
        try{
            response = cloudElementService.getCloudCredsByCloudElementId(cloudElementId);
            if(response == null){
                logger.error("Cloud-element id not found: {}",cloudElementId);
                response = setStatus("error", "Cloud-element id not found. Cloud-element-id: "+cloudElementId, 418, null);
                return ResponseEntity.status(HttpStatus.valueOf(418)).body(response);
            }
        } catch (JsonProcessingException e) {
            logger.error("Exception while getting user's credentials from vault: ",e);
            response = setStatus("error", "Exception while getting user's credentials from vault: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return ResponseEntity.status(HttpStatus.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())).body(response);
        }
        logger.info("Returning user's cloud access credentials for cloud-element-id {}",cloudElementId);
        response = setStatus("success", "api successful", HttpStatus.OK.value(), response);
        return ResponseEntity.status(HttpStatus.valueOf(HttpStatus.OK.value())).body(response);
    }


    private ObjectNode setStatus(String status, String message, int statusCode, JsonNode obj){
        ObjectNode response = new ObjectMapper().createObjectNode();
        response.put("status",status);
        response.put("message",message);
        response.put("statusCode",statusCode);
        response.put("data",obj);
        return response;
    }

    @Override
    public ResponseEntity<Object> getAllCloudElementsOfOrganization(Long orgId, Integer pageNo, Integer pageSize) {
        logger.info("REST request to get all cloud-elements of an organization. Org id: {} ", orgId);
        Page<CloudElement> cloudElementPage = cloudElementService.getAllCloudElementsOfOrganization(orgId, pageNo, pageSize);
        logger.debug("Total elements {}",cloudElementPage.getTotalElements());
        logger.debug("Total pages {}",cloudElementPage.getTotalPages());
        List<CloudElementDTO> cloudElementDTOList = CloudElementMapper.INSTANCE.entityToDtoList(cloudElementPage.toList());
        DiscoveredResourceDTO discoveredResourceDTO = new DiscoveredResourceDTO();
        discoveredResourceDTO.setTotalPages(new Long(cloudElementPage.getTotalPages()));
        discoveredResourceDTO.setTotalRecords(cloudElementPage.getTotalElements());
        discoveredResourceDTO.setCloudElementList(cloudElementDTOList);
        return ResponseEntity.ok(discoveredResourceDTO);
    }
}
