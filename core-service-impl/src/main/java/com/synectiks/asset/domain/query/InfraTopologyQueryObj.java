package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface InfraTopologyQueryObj extends Serializable{

    @Value("#{target.landing_zone}")
    String getLandingZone();

    @Value("#{target.product_enclave}")
	String getProductEnclave();

    @Value("#{target.hosting_type}")
    String getHostingType();

    @Value("#{target.category}")
    String getCategory();

    @Value("#{target.element_type}")
    String getElementType();

    @Value("#{target.element_list}")
    String getElementList();


}
