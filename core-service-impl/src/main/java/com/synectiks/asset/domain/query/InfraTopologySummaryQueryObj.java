package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface InfraTopologySummaryQueryObj extends Serializable{

    @Value("#{target.hosting_type}")
    String getHostingType();

    @Value("#{target.element_type}")
	String getElementType();
    
    @Value("#{target.category}")
    String getCategory();
    
    @Value("#{target.db_category}")
    String getDbCategory();

    @Value("#{target.total_elements}")
    String getTotalElements();


}
