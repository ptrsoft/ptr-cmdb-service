package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface CloudCostAnalyticQueryObj extends Serializable{

    @Value("#{target.cloud}")
	String getCloud();
    
    @Value("#{target.sum}")
    Long getSum();
    


}
