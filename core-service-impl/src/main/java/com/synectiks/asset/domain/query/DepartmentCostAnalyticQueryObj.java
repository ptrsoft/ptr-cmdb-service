package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface DepartmentCostAnalyticQueryObj extends Serializable{

    @Value("#{target.product}")
	String getProduct();
    
    @Value("#{target.product_env}")
	String getProductEnv();
    
    @Value("#{target.total}")
    Long getTotal();
    
    @Value("#{target.percentage}")
    String getPercentage();

}
