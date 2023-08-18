package com.synectiks.asset.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.api.controller.CloudElementApi;
import com.synectiks.asset.api.model.CloudElementDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.mapper.CloudElementMapper;
import com.synectiks.asset.repository.CloudElementRepository;
import com.synectiks.asset.service.CloudElementService;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;
import com.synectiks.asset.web.rest.validation.Validator;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        CloudElement cloudElement = CloudElementMapper.INSTANCE.dtoToEntityForSearch(cloudElementDTO);
        logger.debug("REST request to get all CloudElements on given filters : {} ", cloudElement);
        List<CloudElement> cloudElementList = cloudElementService.search(cloudElement);
        List<CloudElementDTO> cloudElementDTOList = CloudElementMapper.INSTANCE.entityToDtoList(cloudElementList);
        return ResponseEntity.ok(cloudElementDTOList);
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
                objectNode.put("serviceId",(Integer) reqObj.get("serviceId"));
                arrayNode.add(objectNode);
                ObjectNode finalNode =  objectMapper.createObjectNode();
                finalNode.put("HOSTEDSERVICES", arrayNode);
                Map map = jsonAndObjectConverterUtil.convertSourceObjectToTarget(objectMapper, finalNode, Map.class);
                cloudElement.setHostedServices(map);
                cloudElementService.save(cloudElement);
            }catch (Exception e){
                logger.error("Exception: ",e);
//                return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        CloudElementDTO result = CloudElementMapper.INSTANCE.entityToDto(cloudElement);
        return ResponseEntity.ok(result);
    }
}
