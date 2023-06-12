package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface EnvironmentCountQueryObj extends Serializable{

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
