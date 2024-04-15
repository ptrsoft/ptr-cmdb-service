package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface LandingzoneWiseServiceView extends Serializable{

    @Value("#{target.service_id}")
    Long getServiceId();

    @Value("#{target.product_id}")
    Long getProductId();

    @Value("#{target.product}")
    String getProduct();

    @Value("#{target.product_type}")
    String getProductType();

    @Value("#{target.environment}")
    String getEnvironment();

    @Value("#{target.module_id}")
    Long getModuleId();

    @Value("#{target.module}")
    String getModule();

    @Value("#{target.service_name}")
    String getServiceName();

    @Value("#{target.service_type}")
    String getServiceType();

    @Value("#{target.sle}")
    String getSle();

    @Value("#{target.cost}")
    String getCost();

}
