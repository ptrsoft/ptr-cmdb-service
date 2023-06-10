package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.CloudElementSummaryApi;
import com.synectiks.asset.api.model.CloudElementSummaryDTO;
import com.synectiks.asset.domain.CloudElementSummary;
import com.synectiks.asset.mapper.CloudElementSummaryMapper;
import com.synectiks.asset.repository.CloudElementSummaryRepository;
import com.synectiks.asset.service.CloudElementSummaryService;
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
public class CloudElementSummaryController implements CloudElementSummaryApi {
	
	private static final Logger logger = LoggerFactory.getLogger(CloudElementSummaryController.class);
	private static final String ENTITY_NAME = "CloudElementSummary";
	
	@Value("jhipster.clientApp.name")
	private String applicationName;
	
	@Autowired
	private CloudElementSummaryRepository cloudElementSummaryRepository;
	
	@Autowired
	private CloudElementSummaryService cloudElementSummaryService;

	@Autowired
	private Validator validator;

	@Override
	public ResponseEntity<CloudElementSummaryDTO> getCloudElementSummary(Long id) {
		logger.debug("REST request to get CloudElementSummary : ID: {}", id);
		Optional<CloudElementSummary> oObj = cloudElementSummaryService.findOne(id);
		CloudElementSummaryDTO cloudElementSummaryDTO = CloudElementSummaryMapper.INSTANCE.entityToDto(oObj.orElse(null));
		return ResponseUtil.wrapOrNotFound(Optional.of(cloudElementSummaryDTO));
	}

	@Override
	public ResponseEntity<List<CloudElementSummaryDTO>> getCloudElementSummaryList(){
		logger.debug("REST request to get all CloudElementSummary");
		List<CloudElementSummary> cloudElementSummaryList = cloudElementSummaryService.findAll();
		List<CloudElementSummaryDTO> CloudEnvironmentDTOList = CloudElementSummaryMapper.INSTANCE.entityToDtoList(cloudElementSummaryList);
		return ResponseEntity.ok(CloudEnvironmentDTOList);
	}

	@Override
	public ResponseEntity<CloudElementSummaryDTO> addCloudElementSummary(CloudElementSummaryDTO cloudElementSummaryDTO){
		logger.debug("REST request to add CloudElementSummary : {}", cloudElementSummaryDTO);
		validator.validateNotNull(cloudElementSummaryDTO.getId(), ENTITY_NAME);
		CloudElementSummary cloudElementSummary = CloudElementSummaryMapper.INSTANCE.dtoToEntity(cloudElementSummaryDTO);
		cloudElementSummary = cloudElementSummaryService.save(cloudElementSummary);
		CloudElementSummaryDTO result = CloudElementSummaryMapper.INSTANCE.entityToDto(cloudElementSummary);
		return ResponseEntity.ok(result);
	}

	@Override
	public ResponseEntity<CloudElementSummaryDTO> updateCloudElementSummary(CloudElementSummaryDTO cloudElementSummaryDTO) {
		logger.debug("REST request to update CloudElementSummary : {}", cloudElementSummaryDTO);
		validator.validateNull(cloudElementSummaryDTO.getId(), ENTITY_NAME);
		validator.validateEntityExistsInDb(cloudElementSummaryDTO.getId(), ENTITY_NAME, cloudElementSummaryRepository);
		CloudElementSummary existingCloudElementSummary = cloudElementSummaryRepository.findById(cloudElementSummaryDTO.getId()).get();
		CloudElementSummary tempCloudElementSummary = CloudElementSummaryMapper.INSTANCE.dtoToEntityForUpdate(cloudElementSummaryDTO,existingCloudElementSummary);
		CloudElementSummary cloudElementSummary = cloudElementSummaryService.save(tempCloudElementSummary);
		CloudElementSummaryDTO result = CloudElementSummaryMapper.INSTANCE.entityToDto(cloudElementSummary);
		return ResponseEntity.ok(result);
	}

	@Override
	public ResponseEntity<List<CloudElementSummaryDTO>> searchCloudElementSummary(CloudElementSummaryDTO cloudElementSummaryDto) {
		CloudElementSummary cloudElementSummary = CloudElementSummaryMapper.INSTANCE.dtoToEntityForSearch(cloudElementSummaryDto);
		logger.debug("REST request to get all CloudElementSummary on given filters : {} ", cloudElementSummary);
		List<CloudElementSummary> cloudElementSummaryList = cloudElementSummaryService.search(cloudElementSummary);
		List<CloudElementSummaryDTO> cloudElementSummaryDTOList = CloudElementSummaryMapper.INSTANCE.entityToDtoList(cloudElementSummaryList);
		return ResponseEntity.ok(cloudElementSummaryDTOList);
	}
	
}
