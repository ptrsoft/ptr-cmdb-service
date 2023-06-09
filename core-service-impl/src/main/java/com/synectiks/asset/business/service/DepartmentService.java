package com.synectiks.asset.business.service;

import com.synectiks.asset.business.domain.Department;
import com.synectiks.asset.repository.DepartmentRepository;
import com.synectiks.asset.util.JsonAndObjectConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

	private final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private JsonAndObjectConverterUtil jsonAndObjectConverterUtil;
	
	public Department save(Department department) {
		logger.debug("Request to save Department : {}", department);
		Department d = departmentRepository.save(department);
//		if(d != null){
//			Map<String, String> filter = new HashMap<>();
//			filter.put("departmentId", String.valueOf(d.getId()));
//			try {
//				List<Product> pList = serviceAllocationService.getProducts(filter);
//				d.setProducts(pList);
//			} catch (IOException e) {
//				logger.error("Error in getting products ", e.getMessage());
//			}
//		}
		return d;
	}

//	public Optional<Department> partialUpdate(Department department) {
//		logger.debug("Request to partially update Department : {}", department);
//
//		return departmentRepository.findById(department.getId()).map(existingDepartment -> {
//			if (department.getName() != null) {
//				existingDepartment.setName(department.getName());
//			}
//			if (department.getStatus() != null) {
//				existingDepartment.setStatus(department.getStatus());
//			}
//			if (department.getCreatedOn() != null) {
//				existingDepartment.setCreatedOn(department.getCreatedOn());
//			}
//			if (department.getCreatedBy() != null) {
//				existingDepartment.setCreatedBy(department.getCreatedBy());
//			}
//			if (department.getUpdatedOn() != null) {
//				existingDepartment.setUpdatedOn(department.getUpdatedOn());
//			}
//			if (department.getUpdatedBy() != null) {
//				existingDepartment.setUpdatedBy(department.getUpdatedBy());
//			}
////			Map<String, String> filter = new HashMap<>();
////			filter.put("departmentId", String.valueOf(existingDepartment.getId()));
////			try {
////				List<Product> pList = serviceAllocationService.getProducts(filter);
////				existingDepartment.setProducts(pList);
////			} catch (IOException e) {
////				logger.error("Error in getting products ", e.getMessage());
////			}
//			return existingDepartment;
//		}).map(departmentRepository::save);
//	}

	@Transactional(readOnly = true)
	public List<Department> findAll() {
		logger.debug("Request to get all Departments");
		List<Department> list = departmentRepository.findAll();
//		Map<String, String> filter = new HashMap<>();
//		for(Department department: list) {
//			filter.clear();
//			filter.put("departmentId", String.valueOf(department.getId()));
//			try {
//				List<Product> pList = serviceAllocationService.getProducts(filter);
//				department.setProducts(pList);
//			} catch (IOException e) {
//				logger.error("Error in getting products ", e.getMessage());
//			}
//		}
		return list;
	}

	@Transactional(readOnly = true)
	public Optional<Department> findOne(Long id) {
		logger.debug("Request to get Department : {}", id);
		Optional<Department> od = departmentRepository.findById(id);
//		if(od.isPresent()) {
//			Map<String, String> filter = new HashMap<>();
//			filter.put("departmentId", String.valueOf(od.get().getId()));
//			try {
//				List<Product> pList = serviceAllocationService.getProducts(filter);
//				od.get().setProducts(pList);
//			} catch (IOException e) {
//				logger.error("Error in getting products ", e.getMessage());
//			}
//		}
		return od;
		
	}

	public void delete(Long id) {
		logger.debug("Request to delete Department : {}", id);
		departmentRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Department> search(Department department)  {
		logger.debug("Request to get all departments on given filters");

//		Organization organization = null;
//		if (!StringUtils.isBlank(filter.get(Constants.ORGANIZATION_ID))) {
//			organization = new Organization();
//			organization.setId(Long.parseLong(filter.get(Constants.ORGANIZATION_ID)));
//			organization.setCreatedOn(null);
//			organization.setUpdatedOn(null);
//			filter.remove(Constants.ORGANIZATION_ID);
//		}
//
//		if (!StringUtils.isBlank(filter.get(Constants.ORGANIZATION_NAME))) {
//			if(organization != null) {
//				organization.setName(filter.get(Constants.ORGANIZATION_NAME));
//			}else {
//				organization = new Organization();
//				organization.setName(filter.get(Constants.ORGANIZATION_NAME));
//			}
//			organization.setCreatedOn(null);
//			organization.setUpdatedOn(null);
//			filter.remove(Constants.ORGANIZATION_NAME);
//		}
		
//		Department department = jsonAndObjectConverterUtil.convertSourceObjectToTarget(Constants.instantiateMapper(), filter, Department.class);
//
//		// unset default value if createdOn is not coming in filter
//		if (StringUtils.isBlank(filter.get(Constants.CREATED_ON))) {
//			department.setCreatedOn(null);
//		}
//		// unset default value if updatedOn is not coming in filter
//		if (StringUtils.isBlank(filter.get(Constants.UPDATED_ON))) {
//			department.setUpdatedOn(null);
//		}
//		if (organization != null) {
//			department.setOrganization(organization);
//		}
		List<Department> list = departmentRepository.findAll(Example.of(department), Sort.by(Sort.Direction.DESC, "name"));
		
//		Map<String, String> prdFilter = new HashMap<>();
//		for(Department dept: list) {
//			prdFilter.clear();
//			prdFilter.put("departmentId", String.valueOf(dept.getId()));
//			try {
//				List<Product> pList = serviceAllocationService.getProducts(prdFilter);
//				dept.setProducts(pList);
//			} catch (IOException e) {
//				logger.error("Error in getting products ", e.getMessage());
//			}
//		}
		
		return list;
	}

}
