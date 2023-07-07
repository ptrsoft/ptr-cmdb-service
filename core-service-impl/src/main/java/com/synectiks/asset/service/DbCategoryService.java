package com.synectiks.asset.service;

import com.synectiks.asset.domain.DbCategory;
import com.synectiks.asset.repository.DbCategoryRepository;
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
public class DbCategoryService {

	private final Logger logger = LoggerFactory.getLogger(DbCategoryService.class);

	@Autowired
	private DbCategoryRepository dbCategoryRepository;

	public DbCategory save(DbCategory DbCategory) {
		logger.debug("Request to save DbCategory : {}", DbCategory);
		return dbCategoryRepository.save(DbCategory);
	}

	@Transactional(readOnly = true)
	public List<DbCategory> findAll() {
		logger.debug("Request to get all DbCategories");
		return dbCategoryRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<DbCategory> findOne(Long id) {
		logger.debug("Request to get DbCategory : {}", id);
		return dbCategoryRepository.findById(id);
	}

	public void delete(Long id) {
		logger.debug("Request to delete DbCategory : {}", id);
		dbCategoryRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<DbCategory> search(DbCategory DbCategory) {
		logger.debug("Get all DbCategory on given filters");
		return dbCategoryRepository.findAll(Example.of(DbCategory), Sort.by(Sort.Direction.DESC, "name"));
	}


}
