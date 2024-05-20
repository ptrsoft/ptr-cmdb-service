package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.OrganizationApi;
import com.synectiks.asset.api.model.OrganizationDTO;
import com.synectiks.asset.domain.Organization;
import com.synectiks.asset.mapper.OrganizationMapper;
import com.synectiks.asset.repository.OrganizationRepository;
import com.synectiks.asset.service.OrganizationService;
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
public class OrganizationController implements OrganizationApi {

	private final Logger logger = LoggerFactory.getLogger(OrganizationController.class);

	private static final String ENTITY_NAME = "Organization";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private Validator validator;

	@Override
	public ResponseEntity<OrganizationDTO> getOrganization(Long id) {
		logger.debug("REST request to get Organization : ID: {}", id);
		Optional<Organization> oOrg = organizationService.findOne(id);
		OrganizationDTO organizationDTO = OrganizationMapper.INSTANCE.entityToDto(oOrg.orElse(null));
		return ResponseUtil.wrapOrNotFound(Optional.of(organizationDTO));
	}

	@Override
	public ResponseEntity<List<OrganizationDTO>> getOrganizationList(){
		logger.debug("REST request to get all Organizations");
		List<Organization> organizationList = organizationService.findAll();
		List<OrganizationDTO> organizationDTOList = OrganizationMapper.INSTANCE.entityToDtoList(organizationList);
		return ResponseEntity.ok(organizationDTOList);
	}

	@Override
	public ResponseEntity<OrganizationDTO> addOrganization(OrganizationDTO organizationDTO){
		logger.debug("REST request to add Organization : {}", organizationDTO);
		validator.validateNotNull(organizationDTO.getId(), ENTITY_NAME);

		Organization org = OrganizationMapper.INSTANCE.dtoToEntityForSearch(organizationDTO);
		List<Organization> organizationList = organizationService.search(org);
		if(organizationList.size() > 0){
			logger.warn("Organization already exists");
			return ResponseEntity.ok(OrganizationMapper.INSTANCE.entityToDto(organizationList.get(0)));
		}
		Organization organization = OrganizationMapper.INSTANCE.dtoToEntity(organizationDTO);
		organization = organizationService.save(organization);
		OrganizationDTO result = OrganizationMapper.INSTANCE.entityToDto(organization);
		return ResponseEntity.ok(result);
	}

	@Override
	public ResponseEntity<OrganizationDTO> updateOrganization(OrganizationDTO organizationDTO) {
		logger.debug("REST request to update Organization : {}", organizationDTO);
		validator.validateNull(organizationDTO.getId(), ENTITY_NAME);
		validator.validateEntityExistsInDb(organizationDTO.getId(), ENTITY_NAME, organizationRepository);
		Organization existingOrganization = organizationRepository.findById(organizationDTO.getId()).get();
		Organization tempOrganization = OrganizationMapper.INSTANCE.dtoToEntityForUpdate(organizationDTO,existingOrganization);
		Organization organization = organizationService.save(tempOrganization);
		OrganizationDTO result = OrganizationMapper.INSTANCE.entityToDto(organization);
		return ResponseEntity.ok(result);
	}

	@Override
	public ResponseEntity<List<OrganizationDTO>> search(OrganizationDTO organizationDTO) {
		Organization organization = OrganizationMapper.INSTANCE.dtoToEntityForSearch(organizationDTO);
		logger.debug("REST request to get all organizations on given filters : {} ", organization);
		List<Organization> organizationList = organizationService.search(organization);
		List<OrganizationDTO> organizationDTOList = OrganizationMapper.INSTANCE.entityToDtoList(organizationList);
		return ResponseEntity.ok(organizationDTOList);
	}

	@Override
	public ResponseEntity<OrganizationDTO> getOrganizationByName(String orgName) {
		logger.debug("REST request to get Organization by name: {}", orgName);
		Organization organization = organizationService.findByName(orgName);
		OrganizationDTO organizationDTO = OrganizationMapper.INSTANCE.entityToDto(organization);
		return ResponseUtil.wrapOrNotFound(Optional.of(organizationDTO));
	}
}
