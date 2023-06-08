package com.synectiks.asset.response.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface EnvironmentSummaryDto extends Serializable{

    @Value("#{target.cloud}")
    String getCloud();

    @Value("#{target.landing_zone}")
    String getLandingZone();

    @Value("#{target.product_enclave}")
	Long getProductEnclave();

    @Value("#{target.product}")
    Long getProduct();

    @Value("#{target.app_service}")
    Long getAppService();

    @Value("#{target.data_service}")
    Long getDataService();


}
