package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface CloudElementCostAnalyticQueryObj extends Serializable{

    @Value("#{target.element_type}")
	String getElementType();
    
    @Value("#{target.sum}")
    Long getSum();
    


}
