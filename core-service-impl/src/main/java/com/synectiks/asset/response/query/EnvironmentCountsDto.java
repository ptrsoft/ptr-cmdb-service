package com.synectiks.asset.response.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface EnvironmentCountsDto extends Serializable{

    @Value("#{target.environments}")
	Long getEnvironments();

    @Value("#{target.assets}")
	Long getAssets();

    @Value("#{target.alerts}")
    Long getAlerts();

    @Value("#{target.totalbilling}")
    Long getTotalBilling();

    @Value("#{target.cloud}")
    String getCloud();

}
