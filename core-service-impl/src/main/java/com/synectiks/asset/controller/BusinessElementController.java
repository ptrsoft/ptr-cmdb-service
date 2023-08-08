package com.synectiks.asset.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synectiks.asset.api.controller.BusinessElementApi;
import com.synectiks.asset.api.model.BusinessElementDTO;
import com.synectiks.asset.api.model.ModuleDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.BusinessElement;
import com.synectiks.asset.domain.Module;
import com.synectiks.asset.domain.Product;
import com.synectiks.asset.domain.ProductEnv;
import com.synectiks.asset.mapper.BusinessElementMapper;
import com.synectiks.asset.mapper.ModuleMapper;
import com.synectiks.asset.repository.BusinessElementRepository;
import com.synectiks.asset.service.BusinessElementService;
import com.synectiks.asset.service.ModuleService;
import com.synectiks.asset.util.AppkubeStringUtil;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;
import com.synectiks.asset.web.rest.validation.Validator;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class BusinessElementController implements BusinessElementApi {

    private final Logger logger = LoggerFactory.getLogger(BusinessElementController.class);

    private static final String ENTITY_NAME = "BusinessElement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private BusinessElementService businessElementService;

    @Autowired
    private BusinessElementRepository businessElementRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private JsonAndObjectConverterUtil jsonAndObjectConverterUtil;

    @Autowired
    private ModuleService moduleService;

    @Override
    public ResponseEntity<BusinessElementDTO> getBusinessElement(Long id) {
        logger.debug("REST request to get BusinessElement : ID: {}", id);
        Optional<BusinessElement> oObj = businessElementService.findOne(id);
        BusinessElementDTO businessElementDTO = BusinessElementMapper.INSTANCE.entityToDto(oObj.orElse(null));
        return ResponseUtil.wrapOrNotFound(Optional.of(businessElementDTO));
    }

    @Override
    public ResponseEntity<List<BusinessElementDTO>> getBusinessElementList(){
        logger.debug("REST request to get all BusinessElements");
        List<BusinessElement> businessElementList = businessElementService.findAll();
        List<BusinessElementDTO> businessElementDTOList = BusinessElementMapper.INSTANCE.entityToDtoList(businessElementList);
        return ResponseEntity.ok(businessElementDTOList);
    }

    @Override
    public ResponseEntity<BusinessElementDTO> addBusinessElement(BusinessElementDTO businessElementDTO){
        logger.debug("REST request to add BusinessElement : {}", businessElementDTO);
        validator.validateNotNull(businessElementDTO.getId(), ENTITY_NAME);
        BusinessElement businessElement = BusinessElementMapper.INSTANCE.dtoToEntity(businessElementDTO);
        businessElement = businessElementService.save(businessElement);
        BusinessElementDTO result = BusinessElementMapper.INSTANCE.entityToDto(businessElement);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<BusinessElementDTO>> bulkAddBusinessService(@RequestBody Object obj)  {
        logger.debug("REST request to bulk-add all the services of a given product, product environments and module in business service");
        ObjectMapper objectMapper = Constants.instantiateMapper();
        List<BusinessElementDTO> businessElementDTOList = new ArrayList<>();
        try{
            String jsonString = jsonAndObjectConverterUtil.convertObjectToJsonString(objectMapper, obj, Map.class);
            JsonNode rootNode = objectMapper.readTree(jsonString);
            final JsonNode envArray = rootNode.get("envs");
            for (final JsonNode env : envArray) {
                final JsonNode moduleArray = env.get("modules");
                for(final JsonNode moduleNode: moduleArray){
                    final JsonNode businessServicesArray = moduleNode.get("businessServices");
                    for(final JsonNode bsNode: businessServicesArray){
                        ModuleDTO moduleDTO = new ModuleDTO();
                        moduleDTO.setProductId(rootNode.get("productId").asLong());
                        moduleDTO.setProductEnvId(env.get("id").asLong());
                        moduleDTO.setName(AppkubeStringUtil.removeLeadingAndTrailingDoubleQuotes(moduleNode.get("name").asText()));
                        List<Module> moduleList = moduleService.search(ModuleMapper.INSTANCE.dtoToEntityForSearch(moduleDTO));
                        if(moduleList != null && moduleList.size() > 0){
                            BusinessElement businessElement = BusinessElement.builder()
                                    .product(Product.builder().id(rootNode.get("productId").asLong()).build())
                                    .productEnv(ProductEnv.builder().id(env.get("id").asLong()).build())
                                    .module(moduleList.get(0))
                                    .serviceName(AppkubeStringUtil.removeLeadingAndTrailingDoubleQuotes(bsNode.get("serviceName").asText()))
                                    .serviceNature(AppkubeStringUtil.removeLeadingAndTrailingDoubleQuotes(bsNode.get("serviceNature").asText()))
                                    .serviceType(AppkubeStringUtil.removeLeadingAndTrailingDoubleQuotes(bsNode.get("serviceType").asText()))
                                    .build();
                            businessElement = businessElementService.save(businessElement);
                            BusinessElementDTO result = BusinessElementMapper.INSTANCE.entityToDto(businessElement);
                            businessElementDTOList.add(result);
                        }

                    }

                }

            }
            return ResponseEntity.ok(businessElementDTOList);
        }catch (JsonProcessingException je){
            logger.error("Exception: ", je);
            return ResponseUtil.wrapOrNotFound(Optional.empty());
        }

    }
    @Override
    public ResponseEntity<BusinessElementDTO> updateBusinessElement(BusinessElementDTO businessElementDTO) {
        logger.debug("REST request to update BusinessElement : {}", businessElementDTO);
        validator.validateNull(businessElementDTO.getId(), ENTITY_NAME);
        validator.validateEntityExistsInDb(businessElementDTO.getId(), ENTITY_NAME, businessElementRepository);
        BusinessElement existingBusinessElement = businessElementRepository.findById(businessElementDTO.getId()).get();
        BusinessElement tempBusinessElement = BusinessElementMapper.INSTANCE.dtoToEntityForUpdate(businessElementDTO,existingBusinessElement);
        BusinessElement businessElement = businessElementService.save(tempBusinessElement);
        BusinessElementDTO result = BusinessElementMapper.INSTANCE.entityToDto(businessElement);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<BusinessElementDTO>> searchBusinessElement(BusinessElementDTO businessElementDTO) {
        BusinessElement businessElement = BusinessElementMapper.INSTANCE.dtoToEntityForSearch(businessElementDTO);
        logger.debug("REST request to get all BusinessElements on given filters : {} ", businessElement);
        List<BusinessElement> businessElementList = businessElementService.search(businessElement);
        List<BusinessElementDTO> businessElementDTOList = BusinessElementMapper.INSTANCE.entityToDtoList(businessElementList);
        return ResponseEntity.ok(businessElementDTOList);
    }

}
