package com.synectiks.asset.domain.query;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;

public interface CloudElementCurrentQueryObj extends Serializable {

	@Value("#{target.sum_current_hour}")
	Long getSumCurrentHour();

	@Value("#{target.sum_previous_hour}")
	Long getSumPreviousHour();

	@Value("#{target.percentage}")
	Long getPercentage();

	@Value("#{target.sum_difference}")
	Long getSumDifference();

}
