package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.CloudElementApi;
import com.synectiks.asset.api.model.CloudElementDTO;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.mapper.CloudElementMapper;
import com.synectiks.asset.service.CloudElementService;
import com.synectiks.asset.repository.CloudElementRepository;
import com.synectiks.asset.web.rest.validation.Validator;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

//    @Autowired
//    private CloudEnvironmentService cloudEnvironmentService;

    @Autowired
    private CloudElementRepository cloudElementRepository;

    @Autowired
    private Validator validator;

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

}
