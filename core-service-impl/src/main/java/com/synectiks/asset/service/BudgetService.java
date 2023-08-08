package com.synectiks.asset.service;

import com.synectiks.asset.domain.Budget;
import com.synectiks.asset.repository.BudgetRepository;
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
public class BudgetService {

	private final Logger logger = LoggerFactory.getLogger(BudgetService.class);

	@Autowired
	private BudgetRepository budgetRepository;

	public Budget save(Budget budget) {
		logger.debug("Request to save budget : {}", budget);
		return budgetRepository.save(budget);
	}

	@Transactional(readOnly = true)
	public List<Budget> findAll() {
		logger.debug("Request to get all DbCategories");
		return budgetRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Budget> findOne(Long id) {
		logger.debug("Request to get budget : {}", id);
		return budgetRepository.findById(id);
	}

	public void delete(Long id) {
		logger.debug("Request to delete budget : {}", id);
		budgetRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Budget> search(Budget budget) {
		logger.debug("Get all budget on given filters");
		return budgetRepository.findAll(Example.of(budget), Sort.by(Sort.Direction.DESC, "name"));
	}


}
