package com.synectiks.asset.controller;

import com.synectiks.asset.business.domain.Department;
import com.synectiks.asset.business.service.CloudEnvironmentService;
import com.synectiks.asset.business.service.DepartmentService;
import com.synectiks.asset.repository.DepartmentRepository;
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
public class DepartmentController {

	private final Logger log = LoggerFactory.getLogger(DepartmentController.class);

	private static final String ENTITY_NAME = "Department";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	@Autowired
	private DepartmentService departmentService;

	

	@Autowired
	private CloudEnvironmentService cloudEnvironmentService;

	

	@Autowired
	private DepartmentRepository departmentRepository;

	/**
	 * {@code POST  /departments} : Create a new department.
	 *
	 * @param department the department to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
	 *         body the new department, or with status {@code 400 (Bad Request)} if
	 *         the department has already an ID.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PostMapping("/departments")
	public ResponseEntity<Department> create(@Valid @RequestBody Department department)
			throws URISyntaxException {
		log.debug("REST request to save Department : {}", department);
		if (department.getId() != null) {
			throw new BadRequestAlertException("A new department cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Department result = departmentService.save(department);
		return ResponseEntity
				.created(new URI("/api/departments/" + result.getId())).headers(HeaderUtil
						.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PUT  /departments/:id} : Updates an existing department.
	 *
	 * @param id         the id of the department to save.
	 * @param department the department to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated department, or with status {@code 400 (Bad Request)} if
	 *         the department is not valid, or with status
	 *         {@code 500 (Internal Server Error)} if the department couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PutMapping("/departments/{id}")
	public ResponseEntity<Department> update(@PathVariable(value = "id", required = false) final Long id,
			@Valid @RequestBody Department department) throws URISyntaxException {
		log.debug("REST request to update Department : {}, {}", id, department);
		if (department.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, department.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!departmentRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		Department result = departmentService.save(department);
		return ResponseEntity.ok().headers(
				HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, department.getId().toString()))
				.body(result);
	}

	/**
	 * {@code PATCH  /departments/:id} : Partial updates given fields of an existing
	 * department, field will ignore if it is null
	 *
	 * @param id         the id of the department to save.
	 * @param department the department to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the updated department, or with status {@code 400 (Bad Request)} if
	 *         the department is not valid, or with status {@code 404 (Not Found)}
	 *         if the department is not found, or with status
	 *         {@code 500 (Internal Server Error)} if the department couldn't be
	 *         updated.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@PatchMapping(value = "/departments/{id}", consumes = { "application/json", "application/merge-patch+json" })
	public ResponseEntity<Department> partialUpdate(
			@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody Department department)
			throws URISyntaxException {
		log.debug("REST request to partial update Department partially : {}, {}", id, department);
		if (department.getId() == null) {
			throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
		}
		if (!Objects.equals(id, department.getId())) {
			throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
		}

		if (!departmentRepository.existsById(id)) {
			throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
		}

		Optional<Department> result = departmentService.partialUpdate(department);

		return ResponseUtil.wrapOrNotFound(result,
				HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, department.getId().toString()));
	}

	/**
	 * {@code GET  /departments} : get all the departments.
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of departments in body.
	 */
	@GetMapping("/departments")
	public List<Department> getAll() {
		log.debug("REST request to get all Departments");
		return departmentService.findAll();
	}

	/**
	 * {@code GET  /departments/:id} : get the "id" department.
	 *
	 * @param id the id of the department to retrieve.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
	 *         the department, or with status {@code 404 (Not Found)}.
	 */
	@GetMapping("/departments/{id}")
	public ResponseEntity<Department> get(@PathVariable Long id) {
		log.debug("REST request to get Department : {}", id);
		Optional<Department> department = departmentService.findOne(id);
		return ResponseUtil.wrapOrNotFound(department);
	}

	/**
	 * {@code DELETE  /departments/:id} : delete the "id" department.
	 *
	 * @param id the id of the department to delete.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@DeleteMapping("/departments/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		log.debug("REST request to delete Department : {}", id);
		departmentService.delete(id);
		return ResponseEntity.noContent()
				.headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
				.build();
	}

	/**
	 * {@code GET  /departments/search} : get all the departments on given filters.
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of departments in body.
	 */
	@GetMapping("/departments/search")
	public List<Department> search(@RequestParam Map<String, String> filter) throws IOException {
		log.debug("REST request to get all departments on given filters");
		return departmentService.search(filter);
	}


}
