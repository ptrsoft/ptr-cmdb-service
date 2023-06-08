package com.synectiks.asset.controller;

import com.synectiks.asset.business.domain.CloudElementSummary;
import com.synectiks.asset.business.domain.CloudEnvironment;
import com.synectiks.asset.business.service.CloudElementSummaryService;
import com.synectiks.asset.business.service.CloudEnvironmentService;
import com.synectiks.asset.repository.CloudElementSummaryRepository;
import com.synectiks.asset.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CloudElementSummaryController {
	
	private static final Logger logger = LoggerFactory.getLogger(CloudElementSummaryController.class);
	private static final String ENTITY_NAME = "CloudElementSummary";
	
	@Value("jhipster.clientApp.name")
	private String applicationName;
	
	@Autowired
	private CloudElementSummaryRepository cloudElementSummaryRepository;
	
	@Autowired
	private CloudElementSummaryService cloudElementService;
	
	@Autowired
	private CloudEnvironmentService cloudEnvironmentService;
	
	/**
     * {@code POST  /cloud-element-summary} : Create a new cloudElementSummary.
     *
     * @param cloudElementSummary the cloudElementSummary to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cloudElementSummary, or with status {@code 400 (Bad Request)} if the cloudElementSummary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
	@PostMapping("/cloud-element-summary")
	public ResponseEntity<CloudElementSummary> createCloudElement(@RequestBody CloudElementSummary obj) throws URISyntaxException{
		logger.info("Request to create new cloud-element-summary");
		if (obj.getId() != null) {
            throw new BadRequestAlertException("A new cloud-element-summary cannot already have an ID", ENTITY_NAME, "idexists");
        }
		if (obj.getCloudEnvironment() == null) {
			throw new BadRequestAlertException("Cloud environment not provided", ENTITY_NAME, "idnull");
		}else if (obj.getCloudEnvironment() != null && obj.getCloudEnvironment().getId() == null) {
			throw new BadRequestAlertException("Cloud environment id not provided", ENTITY_NAME, "idnull");
		}else if (obj.getCloudEnvironment() != null && obj.getCloudEnvironment().getId() != null) {
			Optional<CloudEnvironment> oc = cloudEnvironmentService.findOne(obj.getCloudEnvironment().getId());
			if(!oc.isPresent()) {
				throw new BadRequestAlertException("Cloud environment not found", ENTITY_NAME, "idnotfound");
			}
		}
		CloudElementSummary result = cloudElementService.createCloudElement(obj);
		return ResponseEntity
	            .created(new URI("cloud-element-summary/" + result.getId()))
	            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
	            .body(result);
	}
	
	@GetMapping("/cloud-element-summary/{id}")
	public ResponseEntity<CloudElementSummary> getCloudElement(@PathVariable Long id) {
		logger.info("Request to get cloud-element-summary by Id: "+id);
		Optional<CloudElementSummary> odp = cloudElementService.getCloudElement(id);
		return ResponseUtil.wrapOrNotFound(odp);
	}
	
	@GetMapping("/cloud-element-summary")
	public ResponseEntity<List<CloudElementSummary>> getAllCloudElement() {
		logger.info("Request to get all cloud-element-summary");
		List<CloudElementSummary> list = cloudElementService.getAllCloudElement();
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
	@DeleteMapping("/cloud-element-summary/{id}")
	public ResponseEntity<Optional<CloudElementSummary>> deleteCloudElement(@PathVariable Long id) {
		logger.info("Request to delete cloud-element-summary by id: {}", id);
		Optional<CloudElementSummary> oSpa = cloudElementService.deleteCloudElement(id);
		return ResponseEntity.status(HttpStatus.OK).body(oSpa);
	}
	
	
	
	@PatchMapping("/cloud-element-summary/{id}")
	public ResponseEntity<Optional<CloudElementSummary>> partialUpdateCloudElement(
			@PathVariable(value = "id", required = false) final Long id,
			@NotNull @RequestBody CloudElementSummary obj){
		logger.info("Request to partially update cloud-element-summary");
		
		if (obj.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, obj.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!cloudElementSummaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        
		Optional<CloudElementSummary> oSpa = cloudElementService.partialUpdateCloudElement(obj);
		return ResponseEntity.status(HttpStatus.OK).body(oSpa);
	}
	
	@GetMapping("/cloud-element-summary/search")
	public ResponseEntity<List<CloudElementSummary>> searchAllCloudElement(@RequestParam Map<String, String> obj){
		logger.info("Request to search cloud-element-summary");
		List<CloudElementSummary> list = cloudElementService.searchAllCloudElement(obj);
		return ResponseEntity.status(HttpStatus.OK).body(list);
	}
	
}
