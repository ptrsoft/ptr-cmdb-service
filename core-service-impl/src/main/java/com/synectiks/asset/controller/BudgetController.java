package com.synectiks.asset.controller;

import com.synectiks.asset.api.controller.BudgetApi;
import com.synectiks.asset.api.model.BudgetDTO;
import com.synectiks.asset.domain.Budget;
import com.synectiks.asset.mapper.BudgetMapper;
import com.synectiks.asset.repository.BudgetRepository;
import com.synectiks.asset.service.BudgetService;
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
public class BudgetController implements BudgetApi {

    private final Logger logger = LoggerFactory.getLogger(BudgetController.class);

    private static final String ENTITY_NAME = "Budget";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private Validator validator;

    @Override
    public ResponseEntity<BudgetDTO> getBudget(Long id) {
        logger.debug("REST request to get budget : ID: {}", id);
        Optional<Budget> oObj = budgetService.findOne(id);
        BudgetDTO budgetDTO = BudgetMapper.INSTANCE.entityToDto(oObj.orElse(null));
        return ResponseUtil.wrapOrNotFound(Optional.of(budgetDTO));
    }

    @Override
    public ResponseEntity<List<BudgetDTO>> getBudgetList(){
        logger.debug("REST request to get all budgets");
        List<Budget> budgetList = budgetService.findAll();
        List<BudgetDTO> budgetDTOList = BudgetMapper.INSTANCE.entityToDtoList(budgetList);
        return ResponseEntity.ok(budgetDTOList);
    }

    @Override
    public ResponseEntity<BudgetDTO> addBudget(BudgetDTO budgetDTO){
        logger.debug("REST request to add budget : {}", budgetDTO);
        validator.validateNotNull(budgetDTO.getId(), ENTITY_NAME);
        Budget budget = BudgetMapper.INSTANCE.dtoToEntity(budgetDTO);
        budget = budgetService.save(budget);
        BudgetDTO result = BudgetMapper.INSTANCE.entityToDto(budget);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<BudgetDTO> updateBudget(BudgetDTO budgetDTO) {
        logger.debug("REST request to update budget : {}", budgetDTO);
        validator.validateNull(budgetDTO.getId(), ENTITY_NAME);
        validator.validateEntityExistsInDb(budgetDTO.getId(), ENTITY_NAME, budgetRepository);
        Budget existingBudget = budgetRepository.findById(budgetDTO.getId()).get();
        Budget tempBudget = BudgetMapper.INSTANCE.dtoToEntityForUpdate(budgetDTO,existingBudget);
        Budget budget = budgetService.save(tempBudget);
        BudgetDTO result = BudgetMapper.INSTANCE.entityToDto(budget);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<List<BudgetDTO>> searchBudget(BudgetDTO budgetDTO) {
        logger.debug("REST request to get all budgets on given filters : {} ", budgetDTO);
        List<Budget> budgetList = budgetService.search(budgetDTO);
        List<BudgetDTO> budgetDTOList = BudgetMapper.INSTANCE.entityToDtoList(budgetList);
        return ResponseEntity.ok(budgetDTOList);
    }

}
