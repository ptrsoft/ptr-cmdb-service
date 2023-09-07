package com.synectiks.asset.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.api.controller.CloudElementApi;
import com.synectiks.asset.api.model.CloudElementDTO;
import com.synectiks.asset.api.model.CloudElementTagDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.domain.CloudElementSummary;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.domain.query.CloudElementTagQueryObj;
import com.synectiks.asset.mapper.CloudElementMapper;
import com.synectiks.asset.mapper.query.CloudElementTagMapper;
import com.synectiks.asset.repository.CloudElementRepository;
import com.synectiks.asset.service.CloudElementService;
import com.synectiks.asset.service.CloudElementSummaryService;
import com.synectiks.asset.service.LandingzoneService;
import com.synectiks.asset.service.VaultService;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;
import com.synectiks.asset.web.rest.validation.Validator;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;


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
    private CloudElementRepository cloudElementRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private JsonAndObjectConverterUtil jsonAndObjectConverterUtil;

    @Autowired
    private LandingzoneService landingzoneService;

//    @Autowired
//    private ConfigService configService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private VaultService vaultService;

    @Override
    public ResponseEntity<CloudElementDTO> getCloudElement(Long id) {
        logger.debug("REST request to get CloudElement : ID: {}", id);
        Optional<CloudElement> oObj = cloudElementService.findOne(id);
        CloudElementDTO cloudElementDTO = CloudElementMapper.INSTANCE.entityToDto(oObj.orElse(null));
        return ResponseUtil.wrapOrNotFound(Optional.of(cloudElementDTO));
    }

    @Override
    public ResponseEntity<List<CloudElementDTO>> getCloudElementList(){
        logger.debug("REST request to get all CloudElements");
        List<CloudElement> cloudElementList = cloudElementService.findAll();
        List<CloudElementDTO> cloudElementDTOList = CloudElementMapper.INSTANCE.entityToDtoList(cloudElementList);
        return ResponseEntity.ok(cloudElementDTOList);
    }

    @Override
    public ResponseEntity<CloudElementDTO> addCloudElement(CloudElementDTO cloudElementDTO){
        logger.debug("REST request to add CloudElement : {}", cloudElementDTO);
        validator.validateNotNull(cloudElementDTO.getId(), ENTITY_NAME);
        CloudElement cloudElement = CloudElementMapper.INSTANCE.dtoToEntity(cloudElementDTO);
        cloudElement = cloudElementService.save(cloudElement);
        CloudElementDTO result = CloudElementMapper.INSTANCE.entityToDto(cloudElement);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<CloudElementDTO> updateCloudElement(CloudElementDTO cloudElementDTO) {
        logger.debug("REST request to update CloudElement : {}", cloudElementDTO);
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
//        CloudElement cloudElement = CloudElementMapper.INSTANCE.dtoToEntityForSearch(cloudElementDTO);
//        logger.debug("REST request to get all CloudElements on given filters : {} ", cloudElement);
        List<CloudElement> cloudElementList = cloudElementService.search(cloudElementDTO);
        List<CloudElementDTO> cloudElementDTOList = CloudElementMapper.INSTANCE.entityToDtoList(cloudElementList);
        return ResponseEntity.ok(cloudElementDTOList);
    }

    @Override
    public ResponseEntity<List<CloudElementTagDTO>> getCloudElementTag(Long landingZoneId, String instanceId) {
        logger.debug("REST request to get all the tags of a landing-zone : LandingZoneId: {}", landingZoneId);
        List<CloudElementTagQueryObj> cloudElementTagQueryObjList = cloudElementService.getCloudElementTag(landingZoneId, instanceId);
        List<CloudElementTagDTO>  cloudElementTagDTOList = CloudElementTagMapper.INSTANCE.toDtoList(cloudElementTagQueryObjList);
        return ResponseUtil.wrapOrNotFound(Optional.of(cloudElementTagDTOList));
    }

    @Override
    public ResponseEntity<Object> deleteCloudElementTag(Long landingZoneId, String instanceId, Long serviceId) {
        logger.debug("REST request to delete a tag. LandingZoneId: {}, instanceId: {}, serviceId: {}", landingZoneId,instanceId,serviceId);
        CloudElement cloudElement = cloudElementService.getCloudElement(landingZoneId,serviceId,instanceId);
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
        logger.debug("REST request to associate a service with infrastructure or tag a business element");
        ObjectMapper objectMapper = Constants.instantiateMapper();
        Map reqObj = (Map)obj;
        CloudElement cloudElement = cloudElementService.findByInstanceId((String)reqObj.get("instanceId"));
        if(cloudElement != null){
            try{
                String js = jsonAndObjectConverterUtil.convertObjectToJsonString(objectMapper, cloudElement.getHostedServices(), Map.class);
                JsonNode rootNode = null;
                ArrayNode arrayNode = null;
                if(!StringUtils.isBlank(js) && !"null".equalsIgnoreCase(js)){
                    rootNode = objectMapper.readTree(js);
                    arrayNode = jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, rootNode.get("HOSTEDSERVICES"), ArrayNode.class);
                }else{
                    arrayNode = objectMapper.createArrayNode();
                }

                ObjectNode objectNode = objectMapper.createObjectNode();
                if(reqObj.get("instanceId") != null){
                    objectNode.put("instanceId",(String) reqObj.get("instanceId"));
                }
                objectNode.put("serviceId",(Integer) reqObj.get("serviceId"));
                objectNode.set("tag", jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, (Map)reqObj.get("tag"), JsonNode.class));
                arrayNode.add(objectNode);
                ObjectNode finalNode =  objectMapper.createObjectNode();
                finalNode.put("HOSTEDSERVICES", arrayNode);
                Map map = jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, finalNode, Map.class);
                cloudElement.setHostedServices(map);
                cloudElementService.save(cloudElement);
            }catch (Exception e){
                logger.error("Exception: ",e);
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        CloudElementDTO result = CloudElementMapper.INSTANCE.entityToDto(cloudElement);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/cloud-element/get-elements/aws/{organization}/{department}/{landingZone}/{awsRegion}")
    public void addAwsCloudElements(
            @PathVariable(name = "organization") String organization ,
            @PathVariable(name = "department") String department ,
            @PathVariable(name = "landingZone") String landingZone,
            @PathVariable(name = "awsRegion") String awsRegion )  {

            String vaultAccountKey =  vaultService.resolveLandingZonePath(organization, department, Constants.AWS, landingZone);

            String appConfigUrl = Constants.AWSX_API_APPCONFIG_URL+"?zone="+awsRegion+"&vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey;
            Map configSummary = this.restTemplate.getForObject(appConfigUrl, Map.class);
            saveAwsAppConfigSummary(organization, department, landingZone, configSummary);

            List resourceList = configSummary.get("ResourceCounts") != null ? (ArrayList)configSummary.get("ResourceCounts") : Collections.emptyList();
            for(Object obj: resourceList){
            Map awsResource = (Map)obj;
            String value = (String)awsResource.get("ResourceType");
            if(Constants.AWS_LAMBDA_FUNCTION_KEY.equalsIgnoreCase(value)){
                String lambdaUrl = Constants.AWSX_API_LAMBDA_URL+"?zone="+awsRegion+"&vaultUrl="+Constants.VAULT_URL+"&vaultToken="+Constants.VAULT_ROOT_TOKEN+"&accountId="+vaultAccountKey;
                Object awsLambda = this.restTemplate.getForObject(lambdaUrl, Object.class);
                System.out.println("Lambda Map "+awsLambda.getClass().getName());
                if(awsLambda != null && awsLambda.getClass().getName().equalsIgnoreCase("java.util.ArrayList")){
                    List lambdaList = (ArrayList)awsLambda;
                    for(Object lambdaObj: lambdaList){
                        Map lambdaMap = (Map)lambdaObj;
                        saveAwsLambda(organization, department, landingZone, lambdaMap);
                    }

                }else if(awsLambda != null && awsLambda.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
                    Map lambdaMap = (LinkedHashMap)awsLambda;
                    saveAwsLambda(organization, department, landingZone, lambdaMap);
                }
            }
        }
    }

    private void saveAwsAppConfigSummary(String organization, String department, String landingZone, Map configSummary) {
        List<CloudElementSummary> cloudElementSummaryList =  cloudElementSummaryService.getCloudElementSummary(organization, department, Constants.AWS, landingZone);
        if(cloudElementSummaryList != null && cloudElementSummaryList.size() > 0){
            logger.info("Updating cloud-element-summary for existing landing-zone: {}", landingZone);
            for(CloudElementSummary cloudElementSummary: cloudElementSummaryList){
                cloudElementSummary.setSummaryJson(configSummary);
                cloudElementSummaryService.save(cloudElementSummary);
            }
        }else{
            List<Landingzone> landingzoneList =  landingzoneService.getLandingZone(organization, department, Constants.AWS, landingZone);
            for(Landingzone landingzone: landingzoneList){
                logger.info("Adding cloud-element-summary for landing-zone: {}", landingZone);
                CloudElementSummary cloudElementSummary = CloudElementSummary.builder()
                        .summaryJson(configSummary)
                        .landingzone(landingzone)
                        .build();
                cloudElementSummaryService.save(cloudElementSummary);
            }
        }
    }

    private void saveAwsLambda(String organization, String department, String landingZone, Map lambdaMap) {
        List<CloudElement> cloudElementList =  cloudElementService.getCloudElement(organization, department, Constants.AWS, landingZone, (String)lambdaMap.get("FunctionArn"));
        if(cloudElementList != null && cloudElementList.size() > 0){
            logger.info("Updating cloud-element for existing landing-zone: {}", landingZone);
            for(CloudElement cloudElement: cloudElementList){
                if(((String)lambdaMap.get("FunctionArn")).equalsIgnoreCase(cloudElement.getArn())){
                    cloudElement.setConfigJson(lambdaMap);
                    cloudElement.setInstanceId((String)lambdaMap.get("FunctionName"));
                    cloudElement.setInstanceName((String)lambdaMap.get("FunctionName"));
                    cloudElementService.save(cloudElement);
                }
            }
        }else{
            addLambdaInDb(organization, department, landingZone, lambdaMap);
        }
    }

    private void addLambdaInDb(String organization, String department, String landingZone, Map lambdaMap) {
        List<Landingzone> landingzoneList =  landingzoneService.getLandingZone(organization, department, Constants.AWS, landingZone);
        for(Landingzone landingzone: landingzoneList){
            logger.info("Adding cloud-element for landing-zone: {}", landingZone);
            CloudElement cloudElementObj = CloudElement.builder()
                    .elementType(Constants.LAMBDA)
                    .arn((String) lambdaMap.get("FunctionArn"))
                    .instanceId((String) lambdaMap.get("FunctionName"))
                    .instanceName((String) lambdaMap.get("FunctionName"))
                    .category(Constants.APP_SERVICES)
                    .landingzone(landingzone)
                    .configJson(lambdaMap)
                    .build();
            cloudElementService.save(cloudElementObj);
        }
    }

}
