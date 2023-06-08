package com.synectiks.asset.controller;

import com.synectiks.asset.business.domain.CloudElement;
import com.synectiks.asset.business.domain.CloudEnvironment;
import com.synectiks.asset.business.service.CloudElementService;
import com.synectiks.asset.business.service.CloudEnvironmentService;
import com.synectiks.asset.repository.CloudElementRepository;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class CloudElementController {

    private final Logger log = LoggerFactory.getLogger(CloudElementController.class);

    private static final String ENTITY_NAME = "CloudElement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private CloudElementService cloudElementService;

    @Autowired
    private CloudEnvironmentService cloudEnvironmentService;

    @Autowired
    private CloudElementRepository cloudElementRepository;


    /**
     * {@code POST  /cloud-elements} : Create a new cloudElements.
     *
     * @param cloudElements the cloudElements to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cloudElements, or with status {@code 400 (Bad Request)} if the cloudElements has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cloud-elements")
    public ResponseEntity<CloudElement> create(@Valid @RequestBody CloudElement cloudElements)
        throws URISyntaxException {
        log.debug("REST request to save CloudElements : {}", cloudElements);
        if (cloudElements.getId() != null) {
            throw new BadRequestAlertException("A new cloudElements cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (cloudElements.getCloudEnvironment() == null) {
			throw new BadRequestAlertException("Cloud environment not provided", ENTITY_NAME, "idnull");
		}else if (cloudElements.getCloudEnvironment() != null && cloudElements.getCloudEnvironment().getId() == null) {
			throw new BadRequestAlertException("Cloud environment id not provided", ENTITY_NAME, "idnull");
		}else if (cloudElements.getCloudEnvironment() != null && cloudElements.getCloudEnvironment().getId() != null) {
			Optional<CloudEnvironment> oc = cloudEnvironmentService.findOne(cloudElements.getCloudEnvironment().getId());
			if(!oc.isPresent()) {
				throw new BadRequestAlertException("Cloud environment not found", ENTITY_NAME, "idnotfound");
			}
		}
        CloudElement result = cloudElementService.save(cloudElements);
        return ResponseEntity
            .created(new URI("/api/cloud-elements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cloud-elements/:id} : Updates an existing cloudElements.
     *
     * @param id the id of the cloudElements to save.
     * @param cloudElements the cloudElements to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cloudElements,
     * or with status {@code 400 (Bad Request)} if the cloudElements is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cloudElements couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cloud-elements/{id}")
    public ResponseEntity<CloudElement> update(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CloudElement cloudElements
    ) throws URISyntaxException {
        log.debug("REST request to update CloudElements : {}, {}", id, cloudElements);
        if (cloudElements.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cloudElements.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cloudElementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CloudElement result = cloudElementService.save(cloudElements);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cloudElements.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cloud-elements/:id} : Partial updates given fields of an existing cloudElements, field will ignore if it is null
     *
     * @param id the id of the cloudElements to save.
     * @param cloudElements the cloudElements to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cloudElements,
     * or with status {@code 400 (Bad Request)} if the cloudElements is not valid,
     * or with status {@code 404 (Not Found)} if the cloudElements is not found,
     * or with status {@code 500 (Internal Server Error)} if the cloudElements couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cloud-elements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CloudElement> partialUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CloudElement cloudElements
    ) throws URISyntaxException {
        log.debug("REST request to partial update CloudElements partially : {}, {}", id, cloudElements);
        if (cloudElements.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cloudElements.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cloudElementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        
        Optional<CloudElement> result = cloudElementService.partialUpdate(cloudElements);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, cloudElements.getId().toString())
        );
    }

    /**
     * {@code GET  /cloud-elements} : get all the cloudElements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cloudElements in body.
     */
    @GetMapping("/cloud-elements")
    public List<CloudElement> getAll() {
        log.debug("REST request to get all CloudElements");
        return cloudElementService.findAll();
    }

    /**
     * {@code GET  /cloud-elements/:id} : get the "id" cloudElements.
     *
     * @param id the id of the cloudElements to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cloudElements, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cloud-elements/{id}")
    public ResponseEntity<CloudElement> get(@PathVariable Long id) {
        log.debug("REST request to get CloudElements : {}", id);
        Optional<CloudElement> cloudElements = cloudElementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cloudElements);
    }

    /**
     * {@code DELETE  /cloud-elements/:id} : delete the "id" cloudElements.
     *
     * @param id the id of the cloudElements to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cloud-elements/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete CloudElements : {}", id);
        cloudElementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
    
    /**
     * {@code GET  /cloud-elements/search} : get all the cloudElements on given filters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cloudElements in body.
     */
    @GetMapping("/cloud-elements/search")
    public ResponseEntity<List<CloudElement>> search(@RequestParam Map<String, String> filter) throws IOException {
        log.debug("REST request to get all CloudElements on given filters");
        List<CloudElement> list = cloudElementService.search(filter);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
}
