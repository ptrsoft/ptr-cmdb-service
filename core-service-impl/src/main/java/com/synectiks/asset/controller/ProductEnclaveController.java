package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.ProductEnclaveApi;
import com.synectiks.asset.api.model.ProductEnclaveDTO;
import com.synectiks.asset.domain.ProductEnclave;
import com.synectiks.asset.mapper.ProductEnclaveMapper;
import com.synectiks.asset.repository.ProductEnclaveRepository;
import com.synectiks.asset.service.ProductEnclaveService;
import com.synectiks.asset.web.rest.validation.Validator;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductEnclaveController implements ProductEnclaveApi {
	
    private final Logger logger = LoggerFactory.getLogger(ProductEnclaveController.class);

    private static final String ENTITY_NAME = "ProductEnclave";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private ProductEnclaveService productEnclaveService;

    @Autowired
    private ProductEnclaveRepository productEnclaveRepository;

    @Autowired
    private Validator validator;

    @Override
    public ResponseEntity<ProductEnclaveDTO> getProductEnclave(Long id) {
        logger.debug("REST request to get a product_enclave : ID: {}", id);
        Optional<ProductEnclave> oObj = productEnclaveService.findOne(id);
        ProductEnclaveDTO productEnclaveDTO = ProductEnclaveMapper.INSTANCE.entityToDto(oObj.orElse(null));
        return ResponseUtil.wrapOrNotFound(Optional.of(productEnclaveDTO));
    }

    @Override
    public ResponseEntity<List<ProductEnclaveDTO>> getProductEnclaveList(){
        logger.debug("REST request to get all product_enclaves");
        List<ProductEnclave> productEnclaveList = productEnclaveService.findAll();
        List<ProductEnclaveDTO> productEnclaveDTOList = ProductEnclaveMapper.INSTANCE.entityToDtoList(productEnclaveList);
        return ResponseEntity.ok(productEnclaveDTOList);
    }

    @Override
    public ResponseEntity<ProductEnclaveDTO> addProductEnclave(ProductEnclaveDTO productEnclaveDTO){
        logger.debug("REST request to add a product_enclave : {}", productEnclaveDTO);
        validator.validateNotNull(productEnclaveDTO.getId(), ENTITY_NAME);
        ProductEnclave productEnclave = ProductEnclaveMapper.INSTANCE.dtoToEntity(productEnclaveDTO);
        productEnclave = productEnclaveService.save(productEnclave);
        ProductEnclaveDTO result = ProductEnclaveMapper.INSTANCE.entityToDto(productEnclave);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ProductEnclaveDTO> updateProductEnclave(ProductEnclaveDTO productEnclaveDTO) {
        logger.debug("REST request to update a product_enclave : {}", productEnclaveDTO);
        validator.validateNull(productEnclaveDTO.getId(), ENTITY_NAME);
        validator.validateEntityExistsInDb(productEnclaveDTO.getId(), ENTITY_NAME, productEnclaveRepository);
        ProductEnclave existingProductEnclave = productEnclaveRepository.findById(productEnclaveDTO.getId()).get();
        ProductEnclave tempProductEnclave = ProductEnclaveMapper.INSTANCE.dtoToEntityForUpdate(productEnclaveDTO, existingProductEnclave);
        ProductEnclave productEnclave = productEnclaveService.save(tempProductEnclave);
        ProductEnclaveDTO result = ProductEnclaveMapper.INSTANCE.entityToDto(productEnclave);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<ProductEnclaveDTO>> searchProductEnclave(ProductEnclaveDTO productEnclaveDTO) {
        ProductEnclave productEnclave = ProductEnclaveMapper.INSTANCE.dtoToEntityForSearch(productEnclaveDTO);
        logger.debug("REST request to get all product_enclaves on given filters : {} ", productEnclave);
        List<ProductEnclave> productEnclaveList = productEnclaveService.search(productEnclave);
        List<ProductEnclaveDTO> productEnclaveDTOList = ProductEnclaveMapper.INSTANCE.entityToDtoList(productEnclaveList);
        return ResponseEntity.ok(productEnclaveDTOList);
    }

}
