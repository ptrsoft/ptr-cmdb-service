package com.synectiks.asset.service;

import com.synectiks.asset.domain.Department;
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
		return departmentRepository.save(department);
	}

	@Transactional(readOnly = true)
	public List<Department> findAll() {
		logger.debug("Request to get all Departments");
		return departmentRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Department> findOne(Long id) {
		logger.debug("Request to get Department : {}", id);
		return departmentRepository.findById(id);
	}

	public void delete(Long id) {
		logger.debug("Request to delete Department : {}", id);
		departmentRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Department> search(Department department)  {
		logger.debug("Request to get all departments on given filters");
		return departmentRepository.findAll(Example.of(department), Sort.by(Sort.Direction.DESC, "name"));
	}

}
