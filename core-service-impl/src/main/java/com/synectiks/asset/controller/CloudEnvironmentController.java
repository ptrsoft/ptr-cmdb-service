package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.CloudEnvironmentsApi;
import com.synectiks.asset.api.model.CloudEnvironmentDTO;
import com.synectiks.asset.domain.CloudEnvironment;
import com.synectiks.asset.mapper.CloudEnvironmentMapper;
import com.synectiks.asset.repository.CloudEnvironmentRepository;
import com.synectiks.asset.service.CloudEnvironmentService;
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
public class CloudEnvironmentController implements CloudEnvironmentsApi {

	private final Logger logger = LoggerFactory.getLogger(CloudEnvironmentController.class);

	private static final String ENTITY_NAME = "CloudEnvironment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private CloudEnvironmentService cloudEnvironmentService;

    @Autowired
    private CloudEnvironmentRepository cloudEnvironmentRepository;

    @Autowired
    private Validator validator;

    @Override
    public ResponseEntity<CloudEnvironmentDTO> getCloudEnvironment(Long id) {
        logger.debug("REST request to get CloudEnvironment : ID: {}", id);
        Optional<CloudEnvironment> oObj = cloudEnvironmentService.findOne(id);
        CloudEnvironmentDTO CloudEnvironmentDTO = CloudEnvironmentMapper.INSTANCE.entityToDto(oObj.orElse(null));
        return ResponseUtil.wrapOrNotFound(Optional.of(CloudEnvironmentDTO));
    }

    @Override
    public ResponseEntity<List<CloudEnvironmentDTO>> getCloudEnvironmentList(){
        logger.debug("REST request to get all CloudEnvironments");
        List<CloudEnvironment> cloudEnvironmentList = cloudEnvironmentService.findAll();
        List<CloudEnvironmentDTO> CloudEnvironmentDTOList = CloudEnvironmentMapper.INSTANCE.entityToDtoList(cloudEnvironmentList);
        return ResponseEntity.ok(CloudEnvironmentDTOList);
    }

    @Override
    public ResponseEntity<CloudEnvironmentDTO> addCloudEnvironment(CloudEnvironmentDTO cloudEnvironmentDTO){
        logger.debug("REST request to add CloudEnvironment : {}", cloudEnvironmentDTO);
        validator.validateNotNull(cloudEnvironmentDTO.getId(), ENTITY_NAME);
        CloudEnvironment cloudEnvironment = CloudEnvironmentMapper.INSTANCE.dtoToEntity(cloudEnvironmentDTO);
        cloudEnvironment = cloudEnvironmentService.save(cloudEnvironment);
        CloudEnvironmentDTO result = CloudEnvironmentMapper.INSTANCE.entityToDto(cloudEnvironment);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<CloudEnvironmentDTO> updateCloudEnvironment(CloudEnvironmentDTO cloudEnvironmentDTO) {
        logger.debug("REST request to update CloudEnvironment : {}", cloudEnvironmentDTO);
        validator.validateNull(cloudEnvironmentDTO.getId(), ENTITY_NAME);
        validator.validateEntityExistsInDb(cloudEnvironmentDTO.getId(), ENTITY_NAME, cloudEnvironmentRepository);
        CloudEnvironment existingCloudEnvironment = cloudEnvironmentRepository.findById(cloudEnvironmentDTO.getId()).get();
        CloudEnvironment tempCloudEnvironment = CloudEnvironmentMapper.INSTANCE.dtoToEntityForUpdate(cloudEnvironmentDTO,existingCloudEnvironment);
        CloudEnvironment cloudEnvironment = cloudEnvironmentService.save(tempCloudEnvironment);
        CloudEnvironmentDTO result = CloudEnvironmentMapper.INSTANCE.entityToDto(cloudEnvironment);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<CloudEnvironmentDTO>> searchCloudEnvironment(CloudEnvironmentDTO cloudEnvironmentDTO) {
        CloudEnvironment cloudEnvironment = CloudEnvironmentMapper.INSTANCE.dtoToEntityForSearch(cloudEnvironmentDTO);
        logger.debug("REST request to get all cloudEnvironments on given filters : {} ", cloudEnvironment);
        List<CloudEnvironment> cloudEnvironmentList = cloudEnvironmentService.search(cloudEnvironment);
        List<CloudEnvironmentDTO> CloudEnvironmentDTOList = CloudEnvironmentMapper.INSTANCE.entityToDtoList(cloudEnvironmentList);
        return ResponseEntity.ok(CloudEnvironmentDTOList);
    }


    
}
