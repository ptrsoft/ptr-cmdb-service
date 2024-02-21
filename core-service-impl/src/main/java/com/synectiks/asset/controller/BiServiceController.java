package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.BiServiceApi;
import com.synectiks.asset.api.model.BiServiceDTO;
import com.synectiks.asset.domain.BiService;
import com.synectiks.asset.mapper.BiServiceMapper;
import com.synectiks.asset.repository.BiServiceRepository;
import com.synectiks.asset.service.BiServiceService;
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
public class BiServiceController implements BiServiceApi {

    private final Logger logger = LoggerFactory.getLogger(BiServiceController.class);

    private static final String ENTITY_NAME = "BiService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private BiServiceService biServiceService;

    @Autowired
    private BiServiceRepository biServiceRepository;

    @Autowired
    private Validator validator;

    @Override
    public ResponseEntity<BiServiceDTO> getBiService(Long id) {
        logger.debug("REST request to get biService : ID: {}", id);
        Optional<BiService> oObj = biServiceService.findOne(id);
        BiServiceDTO biServiceDTO = BiServiceMapper.INSTANCE.entityToDto(oObj.orElse(null));
        return ResponseUtil.wrapOrNotFound(Optional.of(biServiceDTO));
    }

    @Override
    public ResponseEntity<List<BiServiceDTO>> getBiServiceList(){
        logger.debug("REST request to get all biServices");
        List<BiService> biServiceList = biServiceService.findAll();
        List<BiServiceDTO> biServiceDTOList = BiServiceMapper.INSTANCE.entityToDtoList(biServiceList);
        return ResponseEntity.ok(biServiceDTOList);
    }

    @Override
    public ResponseEntity<BiServiceDTO> addBiService(BiServiceDTO biServiceDTO){
        logger.debug("REST request to add biService : {}", biServiceDTO);
        validator.validateNotNull(biServiceDTO.getId(), ENTITY_NAME);
        BiService biService = BiServiceMapper.INSTANCE.dtoToEntity(biServiceDTO);
        biService = biServiceService.save(biService);
        BiServiceDTO result = BiServiceMapper.INSTANCE.entityToDto(biService);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<BiServiceDTO> updateBiService(BiServiceDTO biServiceDTO) {
        logger.debug("REST request to update biService : {}", biServiceDTO);
        validator.validateNull(biServiceDTO.getId(), ENTITY_NAME);
        validator.validateEntityExistsInDb(biServiceDTO.getId(), ENTITY_NAME, biServiceRepository);
        BiService existingBiService = biServiceRepository.findById(biServiceDTO.getId()).get();
        BiService tempBiService = BiServiceMapper.INSTANCE.dtoToEntityForUpdate(biServiceDTO,existingBiService);
        BiService biService = biServiceService.save(tempBiService);
        BiServiceDTO result = BiServiceMapper.INSTANCE.entityToDto(biService);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<BiServiceDTO>> searchBiService(BiServiceDTO biServiceDTO) {
        logger.debug("REST request to get all biService on given filters : {} ", biServiceDTO);
        List<BiService> biServiceList = biServiceService.search(biServiceDTO);
        List<BiServiceDTO> biServiceDTOList = BiServiceMapper.INSTANCE.entityToDtoList(biServiceList);
        return ResponseEntity.ok(biServiceDTOList);
    }

}
