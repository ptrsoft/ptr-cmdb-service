package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface CloudElementCloudWiseQueryObj extends Serializable {

	@Value("#{target.cloud}")
	String getCloud();

	@Value("#{target.sum_values}")
	Long getSumValues();

	@Value("#{target.percentage}")
	Long getPercentage();
}
