package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.DepartmentApi;
import com.synectiks.asset.api.model.DepartmentDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.Department;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.domain.ServiceQueue;
import com.synectiks.asset.mapper.DepartmentMapper;
import com.synectiks.asset.repository.DepartmentRepository;
import com.synectiks.asset.service.DepartmentService;
import com.synectiks.asset.service.LandingzoneService;
import com.synectiks.asset.service.ServiceQueueService;
import com.synectiks.asset.web.rest.validation.Validator;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DepartmentController implements DepartmentApi {

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

	@Autowired
	private LandingzoneService landingzoneService;

	@Autowired
	private ServiceQueueService serviceQueueService;

	@Override
	public ResponseEntity<DepartmentDTO> getDepartment(Long id) {
		logger.debug("REST request to get a department : ID: {}", id);
		Optional<Department> oDept = departmentService.findOne(id);
		DepartmentDTO departmentDTO = DepartmentMapper.INSTANCE.entityToDto(oDept.orElse(null));
		return ResponseUtil.wrapOrNotFound(Optional.of(departmentDTO));
	}

	@Override
	public ResponseEntity<List<DepartmentDTO>> getDepartmentList() {
		logger.debug("REST request to get all departments");
		List<Department> departmentList = departmentService.findAll();
		List<DepartmentDTO> departmentDTOList = DepartmentMapper.INSTANCE.entityToDtoList(departmentList);
		return ResponseEntity.ok(departmentDTOList);
	}

	@Override
	public ResponseEntity<DepartmentDTO> addDepartment(DepartmentDTO departmentDTO) {
		logger.debug("REST request to add a department : {}", departmentDTO);
		validator.validateNotNull(departmentDTO.getId(), ENTITY_NAME);
		if(StringUtils.isBlank(departmentDTO.getStatus())){
			departmentDTO.setStatus(Constants.ACTIVE);
		}
		Department department = DepartmentMapper.INSTANCE.dtoToEntity(departmentDTO);
		department = departmentService.save(department);
		DepartmentDTO result = DepartmentMapper.INSTANCE.entityToDto(department);
		return ResponseEntity.ok(result);
	}

	@Override
	public ResponseEntity<DepartmentDTO> updateDepartment(DepartmentDTO departmentDTO) {
		logger.debug("REST request to update a department : {}", departmentDTO);
		validator.validateNull(departmentDTO.getId(), ENTITY_NAME);
		validator.validateEntityExistsInDb(departmentDTO.getId(), ENTITY_NAME, departmentRepository);
		Department existingDepartment = departmentRepository.findById(departmentDTO.getId()).get();
		Department tempDepartment = DepartmentMapper.INSTANCE.dtoToEntityForUpdate(departmentDTO, existingDepartment);
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

	@Override
	public ResponseEntity<DepartmentDTO> addDepartmentWithLandingZone(Object requestObject){
		logger.debug("REST request to add a department with adding its references in landingzone. Request object : {}", requestObject);
		logger.info("class {}", requestObject.getClass().getName());
		if(requestObject != null && requestObject.getClass().getName().equalsIgnoreCase("java.util.LinkedHashMap")){
			Map requestMap = (Map)requestObject;
			DepartmentDTO departmentDTO = new DepartmentDTO();
			departmentDTO.setName((String) requestMap.get("departmentName"));
			departmentDTO.setDescription((String) requestMap.get("departmentDescription"));
			Long organizationId = null;
			if(requestMap.get("orgId").getClass().getName().equalsIgnoreCase("java.lang.Integer")){
				organizationId =  ((Integer)requestMap.get("orgId")).longValue();
			}else{
				organizationId = (Long)requestMap.get("orgId");
			}
			departmentDTO.setOrganizationId(organizationId);
			departmentDTO.setStatus(Constants.ACTIVE);
			Department department = DepartmentMapper.INSTANCE.dtoToEntity(departmentDTO);
			department = departmentService.save(department);
			DepartmentDTO result = DepartmentMapper.INSTANCE.entityToDto(department);
			if(requestMap.get("landingZoneId").getClass().getName().equalsIgnoreCase("java.util.ArrayList")){
				List landingZoneList = (List)requestMap.get("landingZoneId");
				for(Object obj: landingZoneList){
					Long landingzoneId=null;
					if(obj.getClass().getName().equalsIgnoreCase("java.lang.Integer")){
						landingzoneId =  ((Integer)obj).longValue();
					}else{
						landingzoneId = (Long)obj;
					}
					Optional<Landingzone> optionalLandingzone = landingzoneService.findOne(landingzoneId);
					if(optionalLandingzone.isPresent()){
						Landingzone landingzone = optionalLandingzone.get();
						if(landingzone.getDepartment() == null){
							logger.info("Associating newly created department with landing-zone whose department is null");
						}else{
							logger.info("Creating new landing-zone with newly created department because provided landing-zone associated with other department");
							landingzone.setId(null);
						}
						landingzone.setDepartment(department);
						landingzone = landingzoneService.save(landingzone);

						ServiceQueue serviceQueue = new ServiceQueue();
						serviceQueue.setKey(Constants.LANDING_ZONE);
						serviceQueue.setValue(String.valueOf(landingzone.getId()));
						serviceQueue.setStatus(Constants.NEW);
						serviceQueueService.save(serviceQueue);
					}
				}
			}
			return ResponseEntity.ok(result);
		}
		return ResponseEntity.ok(null);
	}
}
