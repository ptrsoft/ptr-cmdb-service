package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface CloudEnvironmentVpcQueryObj extends Serializable{


    @Value("#{target.product_enclave}")
	String getProductEnclave();

    @Value("#{target.product}")
    Long getProduct();

    @Value("#{target.app_service}")
    Long getAppService();

    @Value("#{target.data_service}")
    Long getDataService();


}
