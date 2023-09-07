package com.synectiks.asset.service;

import com.synectiks.asset.api.model.BudgetDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.Budget;
import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.repository.BudgetRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

	private final Logger logger = LoggerFactory.getLogger(BudgetService.class);

	@Autowired
	private BudgetRepository budgetRepository;

	@Autowired
	private EntityManager entityManager;

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
	public List<Budget> search(BudgetDTO budgetDTO) {
		logger.info("Search budget");
		StringBuilder primarySql = new StringBuilder("select b.* from budget b \n" +
				" left join organization o on o.id = b.organization_id \n" +
				" where 1 = 1 ");
		if(budgetDTO.getId() != null){
			primarySql.append(" and b.id = ? ");
		}
		if(budgetDTO.getAllocatedBudget() != null){
			primarySql.append(" and b.allocated_budget = ? ");
		}
		if(budgetDTO.getOrganizationId() != null){
			primarySql.append(" and o.id = ? ");
		}
		if(!StringUtils.isBlank(budgetDTO.getStatus())){
			primarySql.append(" and upper(b.status) = upper(?) ");
		}
		if(!StringUtils.isBlank(budgetDTO.getCreatedBy())){
			primarySql.append(" and upper(b.created_by) = upper(?) ");
		}
		if(!StringUtils.isBlank(budgetDTO.getUpdatedBy())){
			primarySql.append(" and upper(b.updated_by) = upper(?) ");
		}
		Query query = entityManager.createNativeQuery(primarySql.toString(), Budget.class);
		int index = 0;
		if(budgetDTO.getId() != null){
			query.setParameter(++index, budgetDTO.getId());
		}
		if(budgetDTO.getAllocatedBudget() != null){
			query.setParameter(++index, budgetDTO.getAllocatedBudget());
		}
		if(budgetDTO.getOrganizationId() != null){
			query.setParameter(++index, budgetDTO.getOrganizationId());
		}
		if(!StringUtils.isBlank(budgetDTO.getStatus())){
			query.setParameter(++index, budgetDTO.getStatus());
		}
		if(!StringUtils.isBlank(budgetDTO.getCreatedBy())){
			query.setParameter(++index, budgetDTO.getCreatedBy());
		}
		if(!StringUtils.isBlank(budgetDTO.getUpdatedBy())){
			query.setParameter(++index, budgetDTO.getUpdatedBy());
		}
		return query.getResultList();
	}


}
