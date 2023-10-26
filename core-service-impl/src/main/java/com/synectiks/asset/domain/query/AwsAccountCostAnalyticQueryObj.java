package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface AwsAccountCostAnalyticQueryObj extends Serializable{

    @Value("#{target.cloud}")
	String getCloud();
    
    @Value("#{target.landingzone_id}")
    Long getLandingzoneId();
    
    @Value("#{target.sum}")
    Long getSum();
    


}
