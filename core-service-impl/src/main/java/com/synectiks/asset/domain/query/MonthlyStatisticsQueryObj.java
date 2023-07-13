package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface MonthlyStatisticsQueryObj extends Serializable{

    @Value("#{target.month}")
    String getMonth();

    @Value("#{target.sum_all_values}")
	Long getSumAllValues();

}
