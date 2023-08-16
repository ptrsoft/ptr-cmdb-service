package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface InfraTopologyCloudElementQueryObj extends Serializable{

    @Value("#{target.id}")
    Long getId();

    @Value("#{target.element_type}")
    String getElementType();

    @Value("#{target.instance_id}")
	String getInstanceId();

    @Value("#{target.instance_name}")
    String getInstanceName();

    @Value("#{target.category}")
    String getCategory();

}
