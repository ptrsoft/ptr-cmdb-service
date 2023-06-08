package com.synectiks.asset.controller;

import com.synectiks.asset.business.domain.MicroService;
import com.synectiks.asset.business.service.MicroServiceService;
import com.synectiks.asset.repository.MicroServiceRepository;
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
public class MicroServiceController {

    private final Logger log = LoggerFactory.getLogger(MicroServiceController.class);

    private static final String ENTITY_NAME = "MicroService";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private MicroServiceService microServiceService;

//    @Autowired
//    private CloudEnvironmentService cloudEnvironmentService;

    @Autowired
    private MicroServiceRepository microServiceRepository;


    /**
     * {@code POST  /micro-services} : Create a new microServices.
     *
     * @param microServices the microServices to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new microServices, or with status {@code 400 (Bad Request)} if the microServices has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/micro-services")
    public ResponseEntity<MicroService> create(@Valid @RequestBody MicroService microServices)
        throws URISyntaxException {
        log.debug("REST request to save MicroServices : {}", microServices);
        if (microServices.getId() != null) {
            throw new BadRequestAlertException("A new microServices cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MicroService result = microServiceService.save(microServices);
        return ResponseEntity
            .created(new URI("/api/micro-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /micro-services/:id} : Updates an existing microServices.
     *
     * @param id the id of the microServices to save.
     * @param microServices the microServices to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated microServices,
     * or with status {@code 400 (Bad Request)} if the microServices is not valid,
     * or with status {@code 500 (Internal Server Error)} if the microServices couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/micro-services/{id}")
    public ResponseEntity<MicroService> update(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MicroService microServices
    ) throws URISyntaxException {
        log.debug("REST request to update MicroServices : {}, {}", id, microServices);
        if (microServices.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, microServices.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!microServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MicroService result = microServiceService.save(microServices);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, microServices.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /micro-services/:id} : Partial updates given fields of an existing microServices, field will ignore if it is null
     *
     * @param id the id of the microServices to save.
     * @param microServices the microServices to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated microServices,
     * or with status {@code 400 (Bad Request)} if the microServices is not valid,
     * or with status {@code 404 (Not Found)} if the microServices is not found,
     * or with status {@code 500 (Internal Server Error)} if the microServices couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/micro-services/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MicroService> partialUpdate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MicroService microServices
    ) throws URISyntaxException {
        log.debug("REST request to partial update MicroServices partially : {}, {}", id, microServices);
        if (microServices.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, microServices.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!microServiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        
        Optional<MicroService> result = microServiceService.partialUpdate(microServices);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, microServices.getId().toString())
        );
    }

    /**
     * {@code GET  /micro-services} : get all the microServices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of microServices in body.
     */
    @GetMapping("/micro-services")
    public List<MicroService> getAll() {
        log.debug("REST request to get all MicroServices");
        return microServiceService.findAll();
    }

    /**
     * {@code GET  /micro-services/:id} : get the "id" microServices.
     *
     * @param id the id of the microServices to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the microServices, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/micro-services/{id}")
    public ResponseEntity<MicroService> get(@PathVariable Long id) {
        log.debug("REST request to get MicroServices : {}", id);
        Optional<MicroService> microServices = microServiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(microServices);
    }

    /**
     * {@code DELETE  /micro-services/:id} : delete the "id" microServices.
     *
     * @param id the id of the microServices to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/micro-services/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete MicroServices : {}", id);
        microServiceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
    
    /**
     * {@code GET  /micro-services/search} : get all the microServices on given filters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of microServices in body.
     */
    @GetMapping("/micro-services/search")
    public ResponseEntity<List<MicroService>> search(@RequestParam Map<String, String> filter) throws IOException {
        log.debug("REST request to get all MicroServices on given filters");
        List<MicroService> list = microServiceService.search(filter);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
}
