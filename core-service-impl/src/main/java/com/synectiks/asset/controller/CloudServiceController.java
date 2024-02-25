package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.CloudServiceApi;
import com.synectiks.asset.api.model.CloudDTO;
import com.synectiks.asset.domain.Cloud;
import com.synectiks.asset.mapper.CloudMapper;
import com.synectiks.asset.repository.CloudRepository;
import com.synectiks.asset.service.CloudService;
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
public class CloudServiceController implements CloudServiceApi {

    private final Logger logger = LoggerFactory.getLogger(CloudServiceController.class);

    private static final String ENTITY_NAME = "Cloud";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private CloudRepository cloudRepository;

    @Autowired
    private Validator validator;

    @Override
    public ResponseEntity<CloudDTO> getCloudService(Long id) {
        logger.debug("REST request to get cloud : ID: {}", id);
        Optional<Cloud> oObj = cloudService.findOne(id);
        CloudDTO cloudDTO = CloudMapper.INSTANCE.entityToDto(oObj.orElse(null));
        return ResponseUtil.wrapOrNotFound(Optional.of(cloudDTO));
    }

    @Override
    public ResponseEntity<List<CloudDTO>> getCloudServiceList(){
        logger.debug("REST request to get all clouds");
        List<Cloud> cloudList = cloudService.findAll();
        List<CloudDTO> cloudDTOList = CloudMapper.INSTANCE.entityToDtoList(cloudList);
        return ResponseEntity.ok(cloudDTOList);
    }

    @Override
    public ResponseEntity<CloudDTO> addCloudService(CloudDTO cloudDTO){
        logger.debug("REST request to add cloud : {}", cloudDTO);
        validator.validateNotNull(cloudDTO.getId(), ENTITY_NAME);
        Cloud cloud = CloudMapper.INSTANCE.dtoToEntity(cloudDTO);
        cloud = cloudService.save(cloud);
        CloudDTO result = CloudMapper.INSTANCE.entityToDto(cloud);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<CloudDTO> updateCloudService(CloudDTO cloudDTO) {
        logger.debug("REST request to update cloud : {}", cloudDTO);
        validator.validateNull(cloudDTO.getId(), ENTITY_NAME);
        validator.validateEntityExistsInDb(cloudDTO.getId(), ENTITY_NAME, cloudRepository);
        Cloud existingCloud = cloudRepository.findById(cloudDTO.getId()).get();
        Cloud tempCloud = CloudMapper.INSTANCE.dtoToEntityForUpdate(cloudDTO,existingCloud);
        Cloud cloud = cloudService.save(tempCloud);
        CloudDTO result = CloudMapper.INSTANCE.entityToDto(cloud);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<CloudDTO>> searchCloudService(CloudDTO cloudDTO) {
        logger.debug("REST request to get all cloud on given filters : {} ", cloudDTO);
        List<Cloud> cloudList = cloudService.search(cloudDTO);
        List<CloudDTO> cloudDTOList = CloudMapper.INSTANCE.entityToDtoList(cloudList);
        return ResponseEntity.ok(cloudDTOList);
    }

}
