package com.synectiks.asset.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.api.controller.LandingzoneApi;
import com.synectiks.asset.api.model.LandingzoneDTO;
import com.synectiks.asset.api.model.LandingzoneResponseDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.mapper.LandingzoneMapper;
import com.synectiks.asset.repository.LandingzoneRepository;
import com.synectiks.asset.service.LandingzoneService;
import com.synectiks.asset.service.VaultService;
import com.synectiks.asset.web.rest.validation.Validator;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LandingzoneController implements LandingzoneApi {

	private final Logger logger = LoggerFactory.getLogger(LandingzoneController.class);

	private static final String ENTITY_NAME = "Landingzone";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;


    @Autowired
    private LandingzoneService landingzoneService;

    @Autowired
    private LandingzoneRepository landingzoneRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private VaultService vaultService;

    @Override
    public ResponseEntity<LandingzoneResponseDTO> getLandingzone(Long id) {
        logger.debug("REST request to get a landing-zone : ID: {}", id);
        Optional<Landingzone> oObj = landingzoneService.findOne(id);
        LandingzoneResponseDTO landingzoneResponseDTO = LandingzoneMapper.INSTANCE.entityToResponseDto(oObj.orElse(null));
        return ResponseUtil.wrapOrNotFound(Optional.of(landingzoneResponseDTO));
    }

    @Override
    public ResponseEntity<List<LandingzoneResponseDTO>> getLandingzoneList(){
        logger.debug("REST request to get all landing-zones");
        List<Landingzone> landingzoneList = landingzoneService.findAll();
        List<LandingzoneResponseDTO> landingzoneResponseDTOList = LandingzoneMapper.INSTANCE.entityToResponseDtoList(landingzoneList);
        return ResponseEntity.ok(landingzoneResponseDTOList);
    }

    @Override
    public ResponseEntity<LandingzoneResponseDTO> addLandingzone(LandingzoneDTO landingzoneDTO){
        logger.debug("REST request to add a landing-zone : {}", landingzoneDTO);
        validator.validateNotNull(landingzoneDTO.getId(), ENTITY_NAME);
        if(StringUtils.isBlank(landingzoneDTO.getStatus())){
            landingzoneDTO.setStatus(Constants.ACTIVE);
        }
        Landingzone landingzone = LandingzoneMapper.INSTANCE.dtoToEntity(landingzoneDTO);
        if(landingzoneDTO.getDepartmentId() == null){
            landingzone.setDepartment(null);
        }
        if(landingzoneDTO.getOrganizationId() == null){
            landingzone.setOrganization(null);
        }
        if(StringUtils.isBlank(landingzoneDTO.getLandingZone())){
            landingzone.setLandingZone(landingzoneDTO.getRoleArn().split(":")[4]);
        }
        landingzone = landingzoneService.save(landingzone);
        LandingzoneResponseDTO result = LandingzoneMapper.INSTANCE.entityToResponseDto(landingzone);
        return ResponseEntity.ok(result);
    }

    private void addLandingZoneInVault(LandingzoneResponseDTO landingzoneResponseDTO) {
        try{
            logger.debug("REST request to save landing zone to vault");
            ObjectNode objectNode = vaultService.prepareObjectToSaveInVault(landingzoneResponseDTO);
            HttpStatus httpStatus = vaultService.saveLandingZone(objectNode);
            if(httpStatus.isError()){
                logger.error("Landing Zone could not be saved in vault. Status code :{}",httpStatus.value());
            }else{
                logger.info("Landing Zone saved successfully in vault. Status code :{}",httpStatus.value());
            }

        }catch (Exception e){
            logger.error("Exception while saving landing zone to vault: {} ",e.getMessage());
        }
    }


    @Override
    public ResponseEntity<LandingzoneResponseDTO> updateLandingzone(LandingzoneDTO landingzoneDTO) {
        logger.debug("REST request to update a landing-zone : {}", landingzoneDTO);
        validator.validateNull(landingzoneDTO.getId(), ENTITY_NAME);
        validator.validateEntityExistsInDb(landingzoneDTO.getId(), ENTITY_NAME, landingzoneRepository);
        Landingzone existingLandingzone = landingzoneRepository.findById(landingzoneDTO.getId()).get();
        Landingzone tempLandingzone = LandingzoneMapper.INSTANCE.dtoToEntityForUpdate(landingzoneDTO, existingLandingzone);
        Landingzone landingzone = landingzoneService.save(tempLandingzone);
        LandingzoneResponseDTO result = LandingzoneMapper.INSTANCE.entityToResponseDto(landingzone);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<LandingzoneResponseDTO>> searchLandingzone(LandingzoneDTO landingzoneDTO) {
        logger.debug("REST request to get all landing-zones on given filters : {} ", landingzoneDTO);
        List<Landingzone> landingzoneList = landingzoneService.search(landingzoneDTO);
        List<LandingzoneResponseDTO> landingzoneResponseDTOList = LandingzoneMapper.INSTANCE.entityToResponseDtoList(landingzoneList);
        return ResponseEntity.ok(landingzoneResponseDTOList);
    }


    @Override
    public ResponseEntity<Void> addAllLandingzonesToVault(){
        List<Landingzone> landingzoneList = landingzoneService.findAll();
        List<LandingzoneResponseDTO> landingzoneResponseDTOList = LandingzoneMapper.INSTANCE.entityToResponseDtoList(landingzoneList);
        for(LandingzoneResponseDTO landingzoneResponseDTO: landingzoneResponseDTOList){
            addLandingZoneInVault(landingzoneResponseDTO);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> getVaultKeyByLandingzoneId(Long landingZoneId){
        logger.debug("REST request to get vault key for landing-zone id: {} ", landingZoneId);
        Landingzone landingzone = landingzoneRepository.findById(landingZoneId).get();
        String vaultKey =  vaultService.resolveVaultKey(landingzone.getDepartment().getOrganization().getName(), landingzone.getDepartment().getName(), landingzone.getCloud(), landingzone.getLandingZone());
        return ResponseEntity.ok(vaultKey);
    }

    @Override
    public ResponseEntity<Object> getCloudCredsByLandingzoneId(Long landingZoneId){
        logger.debug("REST request to get cloud credentials for landing-zone id: {} ", landingZoneId);
        Optional<Landingzone> optionalLandingzone = landingzoneService.findOne(landingZoneId);
        ObjectNode response = null;
        if(!optionalLandingzone.isPresent()){
            logger.error("Landing-zone of given landing-zone-id not found. Landing-zone-id: "+landingZoneId);
            response = setStatus("error", "Landing-zone of given landing-zone-id not found. Landing-zone-id: "+landingZoneId, 418, null);
            return ResponseEntity.status(HttpStatus.valueOf(418)).body(response);
        }
        Landingzone landingzone = optionalLandingzone.get();
        String vaultKey =  vaultService.resolveVaultKey(landingzone.getDepartment().getOrganization().getName(), landingzone.getDepartment().getName(), landingzone.getCloud(), landingzone.getLandingZone());
        try {
            response = (ObjectNode)vaultService.getCloudCreds(null);
        } catch (JsonProcessingException e) {
            logger.error("Error in getting user creds from vault: ",e);
            response = setStatus("error", "Error in getting user creds from vault: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
            return ResponseEntity.status(HttpStatus.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())).body(response);
        }
        response = setStatus("success", "api successful", HttpStatus.OK.value(), response);
        return ResponseEntity.status(HttpStatus.valueOf(HttpStatus.OK.value())).body(response);
    }

    private ObjectNode setStatus(String status, String message, int statusCode, JsonNode obj){
        ObjectNode response = new ObjectMapper().createObjectNode();
        response.put("status",status);
        response.put("message",message);
        response.put("statusCode",statusCode);
        response.put("data",obj);
        return response;
    }

}
