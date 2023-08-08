package com.synectiks.asset.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.api.controller.ProductEnvApi;
import com.synectiks.asset.api.model.ProductEnvDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.Product;
import com.synectiks.asset.domain.ProductEnv;
import com.synectiks.asset.mapper.ProductEnvMapper;
import com.synectiks.asset.repository.ProductEnvRepository;
import com.synectiks.asset.service.ProductEnvService;
import com.synectiks.asset.util.AppkubeStringUtil;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;
import com.synectiks.asset.web.rest.validation.Validator;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductEnvController implements ProductEnvApi {
	
    private final Logger logger = LoggerFactory.getLogger(ProductEnvController.class);

    private static final String ENTITY_NAME = "ProductEnv";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private ProductEnvService productEnvService;

    @Autowired
    private ProductEnvRepository productEnvRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private JsonAndObjectConverterUtil jsonAndObjectConverterUtil;

    @Override
    public ResponseEntity<ProductEnvDTO> getProductEnv(Long id) {
        logger.debug("REST request to get a product-env : ID: {}", id);
        Optional<ProductEnv> oOrg = productEnvService.findOne(id);
        ProductEnvDTO productEnvDTO = ProductEnvMapper.INSTANCE.entityToDto(oOrg.orElse(null));
        return ResponseUtil.wrapOrNotFound(Optional.of(productEnvDTO));
    }

    @Override
    public ResponseEntity<List<ProductEnvDTO>> getProductEnvList(){
        logger.debug("REST request to get all product-envs");
        List<ProductEnv> productEnvList = productEnvService.findAll();
        List<ProductEnvDTO> productEnvDTOList = ProductEnvMapper.INSTANCE.entityToDtoList(productEnvList);
        return ResponseEntity.ok(productEnvDTOList);
    }

    @Override
    public ResponseEntity<ProductEnvDTO> addProductEnv(ProductEnvDTO productEnvDTO){
        logger.debug("REST request to add a product-env : {}", productEnvDTO);
        validator.validateNotNull(productEnvDTO.getId(), ENTITY_NAME);
        ProductEnv productEnv = ProductEnvMapper.INSTANCE.dtoToEntity(productEnvDTO);
        productEnv = productEnvService.save(productEnv);
        ProductEnvDTO result = ProductEnvMapper.INSTANCE.entityToDto(productEnv);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<ProductEnvDTO>> bulkAddProductEnv(@RequestBody Object obj)  {
        logger.debug("REST request to bulk-add all the environments of a product in product-env");
        ObjectMapper objectMapper = Constants.instantiateMapper();
        List<ProductEnvDTO> productEnvDTOList = new ArrayList<>();
        try{
            String jsonString = jsonAndObjectConverterUtil.convertObjectToJsonString(objectMapper, obj, Map.class);
            JsonNode rootNode = objectMapper.readTree(jsonString);
            final JsonNode arrayNode = rootNode.get("envs");
            if (arrayNode.isArray()) {
                for (final JsonNode objNode : arrayNode) {
                    Product product = Product.builder().id(rootNode.get("productId").asLong()).build();
                    ProductEnv productEnv = ProductEnv.builder()
                            .product(product)
                            .name(AppkubeStringUtil.removeLeadingAndTrailingDoubleQuotes(objNode.toString())).build();
                    productEnv = productEnvService.save(productEnv);
                    ProductEnvDTO result = ProductEnvMapper.INSTANCE.entityToDto(productEnv);
                    productEnvDTOList.add(result);
                }
            }
            return ResponseEntity.ok(productEnvDTOList);
        }catch (JsonProcessingException je){
            logger.error("Exception: ", je);
            return ResponseUtil.wrapOrNotFound(Optional.empty());
        }

    }

    @Override
    public ResponseEntity<ProductEnvDTO> updateProductEnv(ProductEnvDTO productEnvDTO) {
        logger.debug("REST request to update a product-env : {}", productEnvDTO);
        validator.validateNull(productEnvDTO.getId(), ENTITY_NAME);
        validator.validateEntityExistsInDb(productEnvDTO.getId(), ENTITY_NAME, productEnvRepository);
        ProductEnv existingProductEnv = productEnvRepository.findById(productEnvDTO.getId()).get();
        ProductEnv tempProductEnv = ProductEnvMapper.INSTANCE.dtoToEntityForUpdate(productEnvDTO, existingProductEnv);
        ProductEnv productEnv = productEnvService.save(tempProductEnv);
        ProductEnvDTO result = ProductEnvMapper.INSTANCE.entityToDto(productEnv);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<ProductEnvDTO>> searchProductEnv(ProductEnvDTO productEnvDTO) {
        ProductEnv productEnv = ProductEnvMapper.INSTANCE.dtoToEntityForSearch(productEnvDTO);
        logger.debug("REST request to get all product-envs on given filters : {} ", productEnv);
        List<ProductEnv> productEnvList = productEnvService.search(productEnv);
        List<ProductEnvDTO> productEnvDTOList = ProductEnvMapper.INSTANCE.entityToDtoList(productEnvList);
        return ResponseEntity.ok(productEnvDTOList);
    }

}
