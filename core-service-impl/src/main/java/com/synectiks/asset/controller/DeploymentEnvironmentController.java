package com.synectiks.asset.controller;

import com.synectiks.asset.business.domain.DeploymentEnvironment;
import com.synectiks.asset.business.service.DeploymentEnvironmentService;
import com.synectiks.asset.repository.DeploymentEnvironmentRepository;
import com.synectiks.asset.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DeploymentEnvironmentController {
	
    private final Logger log = LoggerFactory.getLogger(DeploymentEnvironmentController.class);

    private static final String ENTITY_NAME = "DeploymentEnvironment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private DeploymentEnvironmentService deploymentEnvironmentService;

    @Autowired
    private DeploymentEnvironmentRepository deploymentEnvironmentRepository;

    /**
     * {@code POST  /deployment-environments} : Create a new deploymentEnvironment.
     *
     * @param deploymentEnvironment the deploymentEnvironment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deploymentEnvironment, or with status {@code 400 (Bad Request)} if the deploymentEnvironment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deployment-environments")
    public ResponseEntity<DeploymentEnvironment> create(
        @Valid @RequestBody DeploymentEnvironment deploymentEnvironment
    ) throws URISyntaxException {
        log.debug("REST request to save DeploymentEnvironment : {}", deploymentEnvironment);
        if (deploymentEnvironment.getId() != null) {
            throw new BadRequestAlertException("A new deploymentEnvironment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeploymentEnvironment result = deploymentEnvironmentService.save(deploymentEnvironment);
        return ResponseEntity
            .created(new URI("/api/deployment-environments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /deployment-environments/:id} : Updates an existing deploymentEnvironment.
     *
     * @param id the id of the deploymentEnvironment to save.
     * @param deploymentEnvironment the deploymentEnvironment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deploymentEnvironment,
     * or with status {@code 400 (Bad Request)} if the deploymentEnvironment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deploymentEnvironment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deployment-environments/{id}")
    public ResponseEntity<DeploymentEnvironment> update(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeploymentEnvironment deploymentEnvironment
    ) throws URISyntaxException {
        log.debug("REST request to update DeploymentEnvironment : {}, {}", id, deploymentEnvironment);
        if (deploymentEnvironment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deploymentEnvironment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deploymentEnvironmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeploymentEnvironment result = deploymentEnvironmentService.save(deploymentEnvironment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deploymentEnvironment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /deployment-environments/:id} : Partial updates given fields of an existing deploymentEnvironment, field will ignore if it is null
     *
     * @param id the id of the deploymentEnvironment to save.
     * @param deploymentEnvironment the deploymentEnvironment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deploymentEnvironment,
     * or with status {@code 400 (Bad Request)} if the deploymentEnvironment is not valid,
     * or with status {@code 404 (Not Found)} if the deploymentEnvironment is not found,
     * or with status {@code 500 (Internal Server Error)} if the deploymentEnvironment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/deployment-environments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeploymentEnvironment> partialUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeploymentEnvironment deploymentEnvironment
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeploymentEnvironment partially : {}, {}", id, deploymentEnvironment);
        if (deploymentEnvironment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deploymentEnvironment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deploymentEnvironmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeploymentEnvironment> result = deploymentEnvironmentService.partialUpdate(deploymentEnvironment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deploymentEnvironment.getId().toString())
        );
    }

    /**
     * {@code GET  /deployment-environments} : get all the deploymentEnvironments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deploymentEnvironments in body.
     */
    @GetMapping("/deployment-environments")
    public List<DeploymentEnvironment> getAll() {
        log.debug("REST request to get all DeploymentEnvironments");
        return deploymentEnvironmentService.findAll();
    }

    /**
     * {@code GET  /deployment-environments/:id} : get the "id" deploymentEnvironment.
     *
     * @param id the id of the deploymentEnvironment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deploymentEnvironment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deployment-environments/{id}")
    public ResponseEntity<DeploymentEnvironment> get(@PathVariable Long id) {
        log.debug("REST request to get DeploymentEnvironment : {}", id);
        Optional<DeploymentEnvironment> deploymentEnvironment = deploymentEnvironmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deploymentEnvironment);
    }

    /**
     * {@code DELETE  /deployment-environments/:id} : delete the "id" deploymentEnvironment.
     *
     * @param id the id of the deploymentEnvironment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deployment-environments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete DeploymentEnvironment : {}", id);
        deploymentEnvironmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
	 * {@code GET  /deployment-environments/search} : get all the deployment-environments on given filters.
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of deployment-environments in body.
	 */
	@GetMapping("/deployment-environments/search")
	public List<DeploymentEnvironment> search(@RequestParam Map<String, String> filter) throws IOException {
		log.debug("REST request to get all deployment-environments on given filters");
		return deploymentEnvironmentService.search(filter);
	}

}
