package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.MicroServicesApi;
import com.synectiks.asset.api.model.MicroServiceDTO;
import com.synectiks.asset.domain.MicroService;
import com.synectiks.asset.mapper.MicroServiceMapper;
import com.synectiks.asset.repository.MicroServiceRepository;
import com.synectiks.asset.service.MicroServiceService;
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
public class MicroServiceController implements MicroServicesApi {

    private final Logger logger = LoggerFactory.getLogger(MicroServiceController.class);

    private static final String ENTITY_NAME = "MicroService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private MicroServiceService microServiceService;

    @Autowired
    private MicroServiceRepository microServiceRepository;

    @Autowired
    private Validator validator;

    @Override
    public ResponseEntity<MicroServiceDTO> getMicroService(Long id) {
        logger.debug("REST request to get MicroService : ID: {}", id);
        Optional<MicroService> oObj = microServiceService.findOne(id);
        MicroServiceDTO microServiceDTO = MicroServiceMapper.INSTANCE.entityToDto(oObj.orElse(null));
        return ResponseUtil.wrapOrNotFound(Optional.of(microServiceDTO));
    }

    @Override
    public ResponseEntity<List<MicroServiceDTO>> getMicroServiceList(){
        logger.debug("REST request to get all MicroServices");
        List<MicroService> microServiceList = microServiceService.findAll();
        List<MicroServiceDTO> microServiceDTOList = MicroServiceMapper.INSTANCE.entityToDtoList(microServiceList);
        return ResponseEntity.ok(microServiceDTOList);
    }

    @Override
    public ResponseEntity<MicroServiceDTO> addMicroService(MicroServiceDTO microServiceDTO){
        logger.debug("REST request to add MicroService : {}", microServiceDTO);
        validator.validateNotNull(microServiceDTO.getId(), ENTITY_NAME);
        MicroService microService = MicroServiceMapper.INSTANCE.dtoToEntity(microServiceDTO);
        microService = microServiceService.save(microService);
        MicroServiceDTO result = MicroServiceMapper.INSTANCE.entityToDto(microService);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<MicroServiceDTO> updateMicroService(MicroServiceDTO microServiceDTO) {
        logger.debug("REST request to update MicroService : {}", microServiceDTO);
        validator.validateNull(microServiceDTO.getId(), ENTITY_NAME);
        validator.validateEntityExistsInDb(microServiceDTO.getId(), ENTITY_NAME, microServiceRepository);
        MicroService existingMicroService = microServiceRepository.findById(microServiceDTO.getId()).get();
        MicroService tempMicroService = MicroServiceMapper.INSTANCE.dtoToEntityForUpdate(microServiceDTO,existingMicroService);
        MicroService microService = microServiceService.save(tempMicroService);
        MicroServiceDTO result = MicroServiceMapper.INSTANCE.entityToDto(microService);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<MicroServiceDTO>> searchMicroService(MicroServiceDTO microServiceDTO) {
        MicroService microService = MicroServiceMapper.INSTANCE.dtoToEntityForSearch(microServiceDTO);
        logger.debug("REST request to get all MicroServices on given filters : {} ", microService);
        List<MicroService> microServiceList = microServiceService.search(microService);
        List<MicroServiceDTO> microServiceDTOList = MicroServiceMapper.INSTANCE.entityToDtoList(microServiceList);
        return ResponseEntity.ok(microServiceDTOList);
    }

}
