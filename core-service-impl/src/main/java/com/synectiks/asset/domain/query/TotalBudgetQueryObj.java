package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface TotalBudgetQueryObj extends Serializable{


    @Value("#{target.total_budget}")
	Long getTotalBudget();
    
    @Value("#{target.budget_used}")
    Long getBudgetUsed();
    
    @Value("#{target.remaining_budget}")
    Long getRemainingBudget();

    @Value("#{target.remaining_budget_percentage}")
    Long getRemainingBudgetPercentage();

 

}
