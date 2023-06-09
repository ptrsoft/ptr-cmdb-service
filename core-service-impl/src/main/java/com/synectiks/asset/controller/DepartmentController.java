package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.DepartmentsApi;
import com.synectiks.asset.api.model.DepartmentDTO;
import com.synectiks.asset.business.domain.Department;
import com.synectiks.asset.business.service.DepartmentService;
import com.synectiks.asset.mapper.DepartmentMapper;
import com.synectiks.asset.repository.DepartmentRepository;
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
public class DepartmentController implements DepartmentsApi {

	private final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

	private static final String ENTITY_NAME = "Department";

	@Value("${jhipster.clientApp.name}")
	private String applicationName;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private Validator validator;

	@Override
	public ResponseEntity<DepartmentDTO> getDepartment(Long id) {
		logger.debug("REST request to get Department : ID: {}", id);
		Optional<Department> oDept = departmentService.findOne(id);
		DepartmentDTO departmentDTO = DepartmentMapper.INSTANCE.entityToDto(oDept.orElse(null));
		return ResponseUtil.wrapOrNotFound(Optional.of(departmentDTO));
	}

	@Override
	public ResponseEntity<List<DepartmentDTO>> getDepartmentList(){
		logger.debug("REST request to get all Departments");
		List<Department> departmentList = departmentService.findAll();
		List<DepartmentDTO> departmentDTOList = DepartmentMapper.INSTANCE.entityToDtoList(departmentList);
		return ResponseEntity.ok(departmentDTOList);
	}

	@Override
	public ResponseEntity<DepartmentDTO> addDepartment(DepartmentDTO departmentDTO){
		logger.debug("REST request to add Department : {}", departmentDTO);
		validator.validateNotNull(departmentDTO.getId(), ENTITY_NAME);
		Department department = DepartmentMapper.INSTANCE.dtoToEntity(departmentDTO);
		department = departmentService.save(department);
		DepartmentDTO result = DepartmentMapper.INSTANCE.entityToDto(department);
		return ResponseEntity.ok(result);
	}

	@Override
	public ResponseEntity<DepartmentDTO> updateDepartment(DepartmentDTO departmentDTO) {
		logger.debug("REST request to update Department : {}", departmentDTO);
		validator.validateNull(departmentDTO.getId(), ENTITY_NAME);
		validator.validateEntityExistsInDb(departmentDTO.getId(), ENTITY_NAME, departmentRepository);
		Department existingDepartment = departmentRepository.findById(departmentDTO.getId()).get();
		Department tempDepartment = DepartmentMapper.INSTANCE.dtoToEntityForUpdate(departmentDTO,existingDepartment);
		Department department = departmentService.save(tempDepartment);
		DepartmentDTO result = DepartmentMapper.INSTANCE.entityToDto(department);
		return ResponseEntity.ok(result);
	}


	@Override
	public ResponseEntity<List<DepartmentDTO>> searchDepartment(DepartmentDTO departmentDTO) {
		Department department = DepartmentMapper.INSTANCE.dtoToEntityForSearch(departmentDTO);
		logger.debug("REST request to get all departments on given filters : {} ", department);
		List<Department> departmentList = departmentService.search(department);
		List<DepartmentDTO> departmentDTOList = DepartmentMapper.INSTANCE.entityToDtoList(departmentList);
		return ResponseEntity.ok(departmentDTOList);
	}

}
