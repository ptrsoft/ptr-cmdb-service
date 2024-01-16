package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.CloudElementSupportedApiApi;
import com.synectiks.asset.api.model.CloudElementSupportedApiDTO;
import com.synectiks.asset.domain.CloudElementSupportedApi;
import com.synectiks.asset.mapper.CloudElementSupportedApiMapper;
import com.synectiks.asset.repository.CloudElementSupportedApiRepository;
import com.synectiks.asset.service.CloudElementSupportedApiService;
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
public class CloudElementSupportedApiController implements CloudElementSupportedApiApi {

    private final Logger logger = LoggerFactory.getLogger(CloudElementSupportedApiController.class);

    private static final String ENTITY_NAME = "CloudElementSupportedApi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private CloudElementSupportedApiService cloudElementSupportedApiService;

    @Autowired
    private CloudElementSupportedApiRepository cloudElementSupportedApiRepository;

    @Autowired
    private Validator validator;

    @Override
    public ResponseEntity<CloudElementSupportedApiDTO> getCloudElementSupportedApi(Long id) {
        logger.debug("REST request to get cloudElementSupportedApi : ID: {}", id);
        Optional<CloudElementSupportedApi> oObj = cloudElementSupportedApiService.findOne(id);
        CloudElementSupportedApiDTO cloudElementSupportedApiDTO = CloudElementSupportedApiMapper.INSTANCE.entityToDto(oObj.orElse(null));
        return ResponseUtil.wrapOrNotFound(Optional.of(cloudElementSupportedApiDTO));
    }

    @Override
    public ResponseEntity<List<CloudElementSupportedApiDTO>> getCloudElementSupportedApiList(){
        logger.debug("REST request to get all cloudElementSupportedApis");
        List<CloudElementSupportedApi> cloudElementSupportedApiList = cloudElementSupportedApiService.findAll();
        List<CloudElementSupportedApiDTO> cloudElementSupportedApiDTOList = CloudElementSupportedApiMapper.INSTANCE.entityToDtoList(cloudElementSupportedApiList);
        return ResponseEntity.ok(cloudElementSupportedApiDTOList);
    }

    @Override
    public ResponseEntity<CloudElementSupportedApiDTO> addCloudElementSupportedApi(CloudElementSupportedApiDTO cloudElementSupportedApiDTO){
        logger.debug("REST request to add cloudElementSupportedApi : {}", cloudElementSupportedApiDTO);
        validator.validateNotNull(cloudElementSupportedApiDTO.getId(), ENTITY_NAME);
        CloudElementSupportedApi cloudElementSupportedApi = CloudElementSupportedApiMapper.INSTANCE.dtoToEntity(cloudElementSupportedApiDTO);
        cloudElementSupportedApi = cloudElementSupportedApiService.save(cloudElementSupportedApi);
        CloudElementSupportedApiDTO result = CloudElementSupportedApiMapper.INSTANCE.entityToDto(cloudElementSupportedApi);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<CloudElementSupportedApiDTO> updateCloudElementSupportedApi(CloudElementSupportedApiDTO cloudElementSupportedApiDTO) {
        logger.debug("REST request to update cloudElementSupportedApi : {}", cloudElementSupportedApiDTO);
        validator.validateNull(cloudElementSupportedApiDTO.getId(), ENTITY_NAME);
        validator.validateEntityExistsInDb(cloudElementSupportedApiDTO.getId(), ENTITY_NAME, cloudElementSupportedApiRepository);
        CloudElementSupportedApi existingCloudElementSupportedApi = cloudElementSupportedApiRepository.findById(cloudElementSupportedApiDTO.getId()).get();
        CloudElementSupportedApi tempCloudElementSupportedApi = CloudElementSupportedApiMapper.INSTANCE.dtoToEntityForUpdate(cloudElementSupportedApiDTO,existingCloudElementSupportedApi);
        CloudElementSupportedApi cloudElementSupportedApi = cloudElementSupportedApiService.save(tempCloudElementSupportedApi);
        CloudElementSupportedApiDTO result = CloudElementSupportedApiMapper.INSTANCE.entityToDto(cloudElementSupportedApi);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<CloudElementSupportedApiDTO>> searchCloudElementSupportedApi(CloudElementSupportedApiDTO cloudElementSupportedApiDTO) {
        logger.debug("REST request to get all cloudElementSupportedApi on given filters : {} ", cloudElementSupportedApiDTO);
        List<CloudElementSupportedApi> cloudElementSupportedApiList = cloudElementSupportedApiService.search(cloudElementSupportedApiDTO);
        List<CloudElementSupportedApiDTO> cloudElementSupportedApiDTOList = CloudElementSupportedApiMapper.INSTANCE.entityToDtoList(cloudElementSupportedApiList);
        return ResponseEntity.ok(cloudElementSupportedApiDTOList);
    }

}
