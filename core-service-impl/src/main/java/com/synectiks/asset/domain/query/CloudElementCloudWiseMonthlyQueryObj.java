package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface CloudElementCloudWiseMonthlyQueryObj extends Serializable{


	   @Value("#{target.cloud}")
		String getCloud();

	   @Value("#{target.month}")
	   String getMonth();
	   
	   @Value("#{target.sum_values}")
	   Long getSumValues();
}
