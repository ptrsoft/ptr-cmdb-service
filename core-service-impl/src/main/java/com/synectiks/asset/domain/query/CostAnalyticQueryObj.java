package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface CostAnalyticQueryObj extends Serializable{

    @Value("#{target.name}")
	String getName();
    
    @Value("#{target.total}")
    Long getTotal();
    
    @Value("#{target.percentage}")
    String getPercentage();

}
