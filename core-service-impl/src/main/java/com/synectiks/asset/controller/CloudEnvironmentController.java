package com.synectiks.asset.controller;

import com.synectiks.asset.business.domain.CloudEnvironment;
import com.synectiks.asset.business.domain.Department;
import com.synectiks.asset.business.service.CloudEnvironmentService;
import com.synectiks.asset.business.service.DepartmentService;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.repository.CloudEnvironmentRepository;
import com.synectiks.asset.web.rest.errors.BadRequestAlertException;
import com.synectiks.asset.web.rest.errors.DuplicateRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class CloudEnvironmentController {

	private final Logger log = LoggerFactory.getLogger(CloudEnvironmentController.class);

	private static final String ENTITY_NAME = "CloudEnvironment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private CloudEnvironmentService cloudEnvironmentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CloudEnvironmentRepository cloudEnvironmentRepository;



    /**
     * {@code POST  /cloud-environments} : Create a new cloudEnvironment.
     *
     * @param cloudEnvironment the cloudEnvironment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cloudEnvironment, or with status {@code 400 (Bad Request)} if the cloudEnvironment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws IOException
     */
    @PostMapping("/cloud-environments")
    public ResponseEntity<CloudEnvironment> createCloudEnvironment(@Valid @RequestBody CloudEnvironment cloudEnvironment)
        throws URISyntaxException, IOException {
        log.debug("REST request to save CloudEnvironment : {}", cloudEnvironment);
        if (cloudEnvironment.getId() != null) {
            throw new BadRequestAlertException("A new cloudEnvironment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if(cloudEnvironment.getDepartment() == null) {
        	log.warn("Department not found");
        	throw new BadRequestAlertException("Department not found", ENTITY_NAME, "idnotfound");
        }else if(cloudEnvironment.getDepartment() != null && cloudEnvironment.getDepartment().getId() != null) {
			Optional<Department> od = departmentService.findOne(cloudEnvironment.getDepartment().getId());
			if(!od.isPresent()) {
				log.warn("Department not found in the system");
				throw new BadRequestAlertException("Department not found", ENTITY_NAME, "idnotfound");
			}
		}

        String accountId = cloudEnvironment.getAccountId();
        Map<String, String> filter = new HashMap<>();
        if(Constants.AWS.equalsIgnoreCase(cloudEnvironment.getCloud().toUpperCase())) {
        	accountId = cloudEnvironment.getRoleArn().split(":")[4];
        }else if(StringUtils.isBlank(accountId)) {
        	log.warn("Account id/Landing zone not found");
			throw new BadRequestAlertException("Account id/Landing zone not found", ENTITY_NAME, "idnotfound");
        }
        cloudEnvironment.setAccountId(accountId);

        filter.put("cloud", cloudEnvironment.getCloud().toUpperCase());
        filter.put(Constants.ACCOUNT_ID, cloudEnvironment.getAccountId());
        filter.put(Constants.DEPARTMENT_ID, String.valueOf(cloudEnvironment.getDepartment().getId()));
        List<CloudEnvironment> list = cloudEnvironmentService.search(filter);

        CloudEnvironment result = null;
        if(list == null || (list != null && list.size() == 0)) {
        	result = cloudEnvironmentService.save(cloudEnvironment);
        }else {
//        	Set<String> set = new HashSet<>();
//        	for(CloudEnvironment ce: list) {
//        		set.add(ce.getRoleArn());
//        	}
//        	if(set.size() == 1) {
//        		Optional<String> oArn = set.stream().findFirst();
//        		if(oArn.isPresent() && oArn.get().equals(cloudEnvironment.getRoleArn())) {
//        			result = cloudEnvironmentService.save(cloudEnvironment);
//        		}else  {
//        			throw new BadRequestAlertException("cloudEnvironment already discovered", ENTITY_NAME, "idexists");
//        		}
//        	}else {
        		throw new DuplicateRequestAlertException("cloudEnvironment already exists", ENTITY_NAME, "idexists");
//        	}
        }


        return ResponseEntity
            .created(new URI("/api/cloud-environments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cloud-environments/:id} : Updates an existing cloudEnvironment.
     *
     * @param id the id of the cloudEnvironment to save.
     * @param cloudEnvironment the cloudEnvironment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cloudEnvironment,
     * or with status {@code 400 (Bad Request)} if the cloudEnvironment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cloudEnvironment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cloud-environments/{id}")
    public ResponseEntity<CloudEnvironment> updateCloudEnvironment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CloudEnvironment cloudEnvironment
    ) throws URISyntaxException {
        log.debug("REST request to update CloudEnvironment : {}, {}", id, cloudEnvironment);
        if (cloudEnvironment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cloudEnvironment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cloudEnvironmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CloudEnvironment result = cloudEnvironmentService.save(cloudEnvironment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cloudEnvironment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cloud-environments/:id} : Partial updates given fields of an existing cloudEnvironment, field will ignore if it is null
     *
     * @param id the id of the cloudEnvironment to save.
     * @param cloudEnvironment the cloudEnvironment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cloudEnvironment,
     * or with status {@code 400 (Bad Request)} if the cloudEnvironment is not valid,
     * or with status {@code 404 (Not Found)} if the cloudEnvironment is not found,
     * or with status {@code 500 (Internal Server Error)} if the cloudEnvironment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws IOException
     */
    @PatchMapping(value = "/cloud-environments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CloudEnvironment> partialUpdateCloudEnvironment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CloudEnvironment cloudEnvironment
    ) throws URISyntaxException, IOException {
        log.debug("REST request to partial update CloudEnvironment partially : {}, {}", id, cloudEnvironment);
        if (cloudEnvironment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cloudEnvironment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cloudEnvironmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        if(cloudEnvironment.getDepartment() != null && cloudEnvironment.getDepartment().getId() != null) {
			Optional<Department> od = departmentService.findOne(cloudEnvironment.getDepartment().getId());
			if(!od.isPresent()) {
				throw new BadRequestAlertException("Department not found", ENTITY_NAME, "idnotfound");
			}
		}

        String accountId = cloudEnvironment.getRoleArn().split(":")[4];
        Map<String, String> filter = new HashMap<>();
        filter.put("accountId", accountId);
        List<CloudEnvironment> list = cloudEnvironmentService.search(filter);

        Optional<CloudEnvironment> result = null;
        if(list == null || (list != null && list.size() == 0)) {
        	result = cloudEnvironmentService.partialUpdate(cloudEnvironment);
        }else {
        	Set<String> set = new HashSet<>();
        	for(CloudEnvironment ce: list) {
        		set.add(ce.getRoleArn());
        	}
        	if(set.size() == 1) {
        		Optional<String> oArn = set.stream().findFirst();
        		if(oArn.isPresent() && oArn.get().equals(cloudEnvironment.getRoleArn())) {
        			result = cloudEnvironmentService.partialUpdate(cloudEnvironment);
        		}else  {
        			throw new BadRequestAlertException("cloudEnvironment already discovered", ENTITY_NAME, "idexists");
        		}
        	}else {
        		throw new BadRequestAlertException("cloudEnvironment already discovered", ENTITY_NAME, "idexists");
        	}
        }

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cloudEnvironment.getId().toString())
        );
    }

    /**
     * {@code GET  /cloud-environments} : get all the cloudEnvironments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cloudEnvironments in body.
     */
    @GetMapping("/cloud-environments")
    public List<CloudEnvironment> getAllCloudEnvironments() {
        log.debug("REST request to get all CloudEnvironments");
        return cloudEnvironmentService.findAll();
    }

    /**
     * {@code GET  /cloud-environments/:id} : get the "id" cloudEnvironment.
     *
     * @param id the id of the cloudEnvironment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cloudEnvironment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cloud-environments/{id}")
    public ResponseEntity<CloudEnvironment> getCloudEnvironment(@PathVariable Long id) {
        log.debug("REST request to get CloudEnvironment : {}", id);
        Optional<CloudEnvironment> cloudEnvironment = cloudEnvironmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cloudEnvironment);
    }

    /**
     * {@code DELETE  /cloud-environments/:id} : delete the "id" cloudEnvironment.
     *
     * @param id the id of the cloudEnvironment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cloud-environments/{id}")
    public ResponseEntity<Void> deleteCloudEnvironment(@PathVariable Long id) {
        log.debug("REST request to delete CloudEnvironment : {}", id);
        cloudEnvironmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

	/**
	 * {@code GET  /cloud-environments/search} : get all the cloud-environments on given filters.
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of cloud-environments in body.
	 */
	@GetMapping("/cloud-environments/search")
	public List<CloudEnvironment> search(@RequestParam Map<String, String> filter) throws IOException {
		log.debug("REST request to get all cloud-environments on given filters");
		return cloudEnvironmentService.search(filter);
	}

	/**
	 * {@code GET  /organizations/{orgId}/cloud-environments/count} : get record count of landing zone and its associated assets, alerts and billing cost for each cloud of an organization.
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of record count of landing zone and its associated assets, alerts and billing cost for each cloud of an organization.
	 * @throws URISyntaxException
	 */
//	@GetMapping("/organizations/{orgId}/cloud-environments/count")
//	public ResponseEntity<List<EnvironmentCountsDto>> getEnvironmentCounts(@PathVariable Long orgId) throws IOException, URISyntaxException {
//		log.debug("REST request to get record count of landing zone and its associated assets, alerts and billing cost for each cloud of an organization. Org id: {}", orgId);
//		List<EnvironmentCountsDto> result = cloudEnvironmentService.getEnvironmentCounts(orgId);
//
//		return ResponseEntity
//	            .ok()
//	            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, "orgId: "+orgId))
//	            .body(result);
//
//	}
//
//	/**
//	 * {@code GET  /organizations/{orgId}/cloud/{cloud}/cloud-environments/count} : get record count of landing zone and its associated assets, alerts and billing cost for given cloud of an organization.
//	 *
//	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
//	 *         of record count of landing zone and its associated assets, alerts and billing cost for given cloud of an organization.
//	 * @throws URISyntaxException
//	 */
//	@GetMapping("/organizations/{orgId}/cloud/{cloud}/cloud-environments/count")
//	public ResponseEntity<EnvironmentCountsDto> getEnvironmentCounts(@PathVariable String cloud, @PathVariable Long orgId) throws IOException, URISyntaxException {
//        log.debug("REST request to get record count of landing zone and its associated assets, alerts and billing cost for given cloud: {} and organization id: {} ",cloud, orgId);
//        EnvironmentCountsDto result = cloudEnvironmentService.getEnvironmentCounts(cloud, orgId);
//        return ResponseEntity
//            .ok()
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, "orgId: "+orgId+", cloud: "+cloud))
//            .body(result);
//    }
//
//
//    /**
//     * {@code GET  /organizations/{orgId}/cloud-environments/summary} : get list of landing zone and its associated product enclaves, products, and app and data services for a given organization.
//     *
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and list of landing zone and its associated product enclaves, products, and app and data services for a given organization
//     * @throws URISyntaxException
//     */
//    @GetMapping("/organizations/{orgId}/cloud-environments/summary")
//    public ResponseEntity<List<EnvironmentDto>> getEnvironmentSummary(@PathVariable Long orgId,
//                                                                      @RequestParam(name = "department", required = false) String department,
//                                                                      @RequestParam(name = "product", required = false) String product,
//                                                                      @RequestParam(name = "environment", required = false) String environment) throws IOException, URISyntaxException {
//        log.debug("REST request to get list of landing zone and its associated product enclaves, products, and and data services for a given organization. organization id: {} ",orgId);
//        List<EnvironmentDto> result = cloudEnvironmentService.getEnvironmentSummary(orgId, department, product, environment);
//        return ResponseEntity
//            .ok()
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, "orgId: "+orgId))
//            .body(result);
//    }
//
//    /**
//     * {@code GET  /organizations/{orgId}/cloud/{cloud}/cloud-environments/summary} : get list of landing zone and its associated product enclaves, products, and app and data services for a given organization and cloud.
//     *
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and list of landing zone and its associated product enclaves, products, and app and data services for a given organization and cloud
//     * @throws URISyntaxException
//     */
//    @GetMapping("/organizations/{orgId}/cloud/{cloud}/cloud-environments/summary")
//    public ResponseEntity<List<EnvironmentDto>> getEnvironmentSummary(@PathVariable String cloud, @PathVariable Long orgId) throws IOException, URISyntaxException {
//        log.debug("REST request to get list of landing zone and its associated product enclaves, products, and and data services for a given organization and cloud. organization id: {}, cloud: {}",orgId, cloud);
//        List<EnvironmentDto> result = cloudEnvironmentService.getEnvironmentSummary(cloud, orgId);
//        return ResponseEntity
//            .ok()
//            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, "orgId: "+orgId))
//            .body(result);
//    }
}
