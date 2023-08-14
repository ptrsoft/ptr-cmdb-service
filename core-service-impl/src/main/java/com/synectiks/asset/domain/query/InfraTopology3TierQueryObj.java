package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface InfraTopology3TierQueryObj extends Serializable{

    @Value("#{target.cloud}")
    String getCloud();

    @Value("#{target.type}")
    String getType();

    @Value("#{target.product_enclave_id}")
	String getProductEnclaveId();

    @Value("#{target.product_enclave_name}")
    String getProductEnclaveName();

    @Value("#{target.product_count}")
    Long getProductCount();

    @Value("#{target.web_count}")
    Long getWebCount();

    @Value("#{target.app_count}")
    Long getAppCount();

    @Value("#{target.data_count}")
    Long getDataCount();

    @Value("#{target.auxiliary_count}")
    Long getAuxiliaryCount();

}
