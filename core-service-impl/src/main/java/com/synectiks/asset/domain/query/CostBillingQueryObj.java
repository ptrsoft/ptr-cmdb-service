package com.synectiks.asset.domain.query;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;

public interface CostBillingQueryObj extends Serializable{

	@Value("#{target.landing_zone}")
	Long getLandingZone();
	
	@Value("#{target.element_name}")
	String getElementName();
	
    @Value("#{target.cost_json}")
	String getCostJson();
    
    
   

}
