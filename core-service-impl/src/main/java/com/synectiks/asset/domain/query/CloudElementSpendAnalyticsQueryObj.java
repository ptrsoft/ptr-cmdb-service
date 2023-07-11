package com.synectiks.asset.domain.query;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;

public interface CloudElementSpendAnalyticsQueryObj extends Serializable{

	@Value("#{target.sum_current_date}")
	Long getSumCurrentDate();

    @Value("#{target.sum_previous_date}")
    Long getSumPreviousDate();

    @Value("#{target.percentage}")
    String getPercentage();
 


}
