package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface TotalBudgetQueryObj extends Serializable{


    @Value("#{target.monthly_cost_sum}")
	Long getMonthlyCostSum();
    
    @Value("#{target.budget_total_sum}")
    Long getBudgetTotalSum();
    
    @Value("#{target.difference}")
    Long getDifference();

    @Value("#{target.percentage}")
    Long getPercentage();

 

}
