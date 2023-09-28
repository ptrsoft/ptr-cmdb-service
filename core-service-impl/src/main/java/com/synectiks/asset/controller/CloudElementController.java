package com.synectiks.asset.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.api.controller.CloudElementApi;
import com.synectiks.asset.api.model.CloudElementDTO;
import com.synectiks.asset.api.model.CloudElementTagDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.BusinessElement;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        logger.debug("REST request to get all cloud-elements on given filters : {} ", cloudElementDTO);
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
        logger.debug("REST request to associate a service with infrastructure or tag a business element");
        Map reqObj = (Map)obj;
        CloudElement cloudElement = cloudElementService.associateCloudElement(reqObj);
        if(cloudElement == null){
            logger.warn("Cloud-element not found. Manual association/tagging cannot be done");
            return ResponseEntity.status(HttpStatus.valueOf(418)).body(null);
        }
        CloudElementDTO cloudElementDTO = CloudElementMapper.INSTANCE.entityToDto(cloudElement);
        return ResponseUtil.wrapOrNotFound(Optional.of(cloudElementDTO));
    }

    @GetMapping(value = "/cloud-element/auto-associate/org/{orgId}/cloud/{cloud}", produces = { "application/json" }    )
    public void autoAssociateCloudElement(@PathVariable("orgId") Long orgId, @PathVariable("cloud") String cloud){
        cloudElementService.autoAssociateCloudElement(orgId, cloud);
    }

}
