package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.ProductApi;
import com.synectiks.asset.api.model.ProductDTO;
import com.synectiks.asset.domain.Product;
import com.synectiks.asset.mapper.ProductMapper;
import com.synectiks.asset.repository.ProductRepository;
import com.synectiks.asset.service.ProductService;
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
public class ProductController implements ProductApi {
	
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private static final String ENTITY_NAME = "Product";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Validator validator;

    @Override
    public ResponseEntity<ProductDTO> getProduct(Long id) {
        logger.debug("REST request to get a product : ID: {}", id);
        Optional<Product> oObj = productService.findOne(id);
        ProductDTO productDTO = ProductMapper.INSTANCE.entityToDto(oObj.orElse(null));
        return ResponseUtil.wrapOrNotFound(Optional.of(productDTO));
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getProductList(){
        logger.debug("REST request to get all products");
        List<Product> productList = productService.findAll();
        List<ProductDTO> productDTOList = ProductMapper.INSTANCE.entityToDtoList(productList);
        return ResponseEntity.ok(productDTOList);
    }

    @Override
    public ResponseEntity<ProductDTO> addProduct(ProductDTO productDTO){
        logger.debug("REST request to add a product : {}", productDTO);
        validator.validateNotNull(productDTO.getId(), ENTITY_NAME);
        Product product = ProductMapper.INSTANCE.dtoToEntity(productDTO);
        product = productService.save(product);
        ProductDTO result = ProductMapper.INSTANCE.entityToDto(product);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ProductDTO> updateProduct(ProductDTO productDTO) {
        logger.debug("REST request to update a product : {}", productDTO);
        validator.validateNull(productDTO.getId(), ENTITY_NAME);
        validator.validateEntityExistsInDb(productDTO.getId(), ENTITY_NAME, productRepository);
        Product existingProduct = productRepository.findById(productDTO.getId()).get();
        Product tempProduct = ProductMapper.INSTANCE.dtoToEntityForUpdate(productDTO, existingProduct);
        Product product = productService.save(tempProduct);
        ProductDTO result = ProductMapper.INSTANCE.entityToDto(product);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<ProductDTO>> searchProduct(ProductDTO productDTO) {
        Product product = ProductMapper.INSTANCE.dtoToEntityForSearch(productDTO);
        logger.debug("REST request to get all products on given filters : {} ", product);
        List<Product> productList = productService.search(product);
        List<ProductDTO> productDTOList = ProductMapper.INSTANCE.entityToDtoList(productList);
        return ResponseEntity.ok(productDTOList);
    }

}
