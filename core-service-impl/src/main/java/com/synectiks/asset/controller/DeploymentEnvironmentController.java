package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.DeploymentEnvironmentsApi;
import com.synectiks.asset.api.model.DeploymentEnvironmentDTO;
import com.synectiks.asset.domain.DeploymentEnvironment;
import com.synectiks.asset.mapper.DeploymentEnvironmentMapper;
import com.synectiks.asset.repository.DeploymentEnvironmentRepository;
import com.synectiks.asset.service.DeploymentEnvironmentService;
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
public class DeploymentEnvironmentController implements DeploymentEnvironmentsApi {
	
    private final Logger logger = LoggerFactory.getLogger(DeploymentEnvironmentController.class);

    private static final String ENTITY_NAME = "DeploymentEnvironment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private DeploymentEnvironmentService deploymentEnvironmentService;

    @Autowired
    private DeploymentEnvironmentRepository deploymentEnvironmentRepository;

    @Autowired
    private Validator validator;

    @Override
    public ResponseEntity<DeploymentEnvironmentDTO> getDeploymentEnvironment(Long id) {
        logger.debug("REST request to get DeploymentEnvironment : ID: {}", id);
        Optional<DeploymentEnvironment> oOrg = deploymentEnvironmentService.findOne(id);
        DeploymentEnvironmentDTO deploymentEnvironmentDTO = DeploymentEnvironmentMapper.INSTANCE.entityToDto(oOrg.orElse(null));
        return ResponseUtil.wrapOrNotFound(Optional.of(deploymentEnvironmentDTO));
    }

    @Override
    public ResponseEntity<List<DeploymentEnvironmentDTO>> getDeploymentEnvironmentList(){
        logger.debug("REST request to get all DeploymentEnvironments");
        List<DeploymentEnvironment> deploymentEnvironmentList = deploymentEnvironmentService.findAll();
        List<DeploymentEnvironmentDTO> deploymentEnvironmentDTOList = DeploymentEnvironmentMapper.INSTANCE.entityToDtoList(deploymentEnvironmentList);
        return ResponseEntity.ok(deploymentEnvironmentDTOList);
    }

    @Override
    public ResponseEntity<DeploymentEnvironmentDTO> addDeploymentEnvironment(DeploymentEnvironmentDTO deploymentEnvironmentDTO){
        logger.debug("REST request to add DeploymentEnvironment : {}", deploymentEnvironmentDTO);
        validator.validateNotNull(deploymentEnvironmentDTO.getId(), ENTITY_NAME);
        DeploymentEnvironment deploymentEnvironment = DeploymentEnvironmentMapper.INSTANCE.dtoToEntity(deploymentEnvironmentDTO);
        deploymentEnvironment = deploymentEnvironmentService.save(deploymentEnvironment);
        DeploymentEnvironmentDTO result = DeploymentEnvironmentMapper.INSTANCE.entityToDto(deploymentEnvironment);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<DeploymentEnvironmentDTO> updateDeploymentEnvironment(DeploymentEnvironmentDTO deploymentEnvironmentDTO) {
        logger.debug("REST request to update DeploymentEnvironment : {}", deploymentEnvironmentDTO);
        validator.validateNull(deploymentEnvironmentDTO.getId(), ENTITY_NAME);
        validator.validateEntityExistsInDb(deploymentEnvironmentDTO.getId(), ENTITY_NAME, deploymentEnvironmentRepository);
        DeploymentEnvironment existingDeploymentEnvironment = deploymentEnvironmentRepository.findById(deploymentEnvironmentDTO.getId()).get();
        DeploymentEnvironment tempDeploymentEnvironment = DeploymentEnvironmentMapper.INSTANCE.dtoToEntityForUpdate(deploymentEnvironmentDTO,existingDeploymentEnvironment);
        DeploymentEnvironment deploymentEnvironment = deploymentEnvironmentService.save(tempDeploymentEnvironment);
        DeploymentEnvironmentDTO result = DeploymentEnvironmentMapper.INSTANCE.entityToDto(deploymentEnvironment);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<DeploymentEnvironmentDTO>> searchDeploymentEnvironment(DeploymentEnvironmentDTO deploymentEnvironmentDTO) {
        DeploymentEnvironment deploymentEnvironment = DeploymentEnvironmentMapper.INSTANCE.dtoToEntityForSearch(deploymentEnvironmentDTO);
        logger.debug("REST request to get all deploymentEnvironments on given filters : {} ", deploymentEnvironment);
        List<DeploymentEnvironment> deploymentEnvironmentList = deploymentEnvironmentService.search(deploymentEnvironment);
        List<DeploymentEnvironmentDTO> deploymentEnvironmentDTOList = DeploymentEnvironmentMapper.INSTANCE.entityToDtoList(deploymentEnvironmentList);
        return ResponseEntity.ok(deploymentEnvironmentDTOList);
    }

}
