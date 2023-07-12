package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface MonthlyStatisticsQueryObj extends Serializable{

    @Value("#{target.month}")
    String getMonth();

    @Value("#{target.current_year_sum}")
	Long getCurrentYearSum();
    
    @Value("#{target.previous_year_sum}")
    Long getPreviousYearSum();
    
    @Value("#{target.difference}")
    Long getDifference();

    @Value("#{target.difference_percentage}")
    Long getDifferencePercentage();

    @Value("#{target.current_year_total_sum}")
    Long getCurrentYearTotalSum();

}
