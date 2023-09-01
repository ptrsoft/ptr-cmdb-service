package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface SlaAnalyticQueryObj extends Serializable{

    @Value("#{target.name}")
	String getName();
    
    @Value("#{target.performance}")
    String getPerformance();
    
    @Value("#{target.availability}")
    String getAvailability();
    
    @Value("#{target.reliability}")
    String getReliability();

    @Value("#{target.security}")
    String getSecurity();
    
    @Value("#{target.endUsage}")
    String getEndUsage();
}
