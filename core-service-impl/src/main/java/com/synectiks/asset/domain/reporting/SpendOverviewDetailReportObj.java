package com.synectiks.asset.domain.reporting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SpendOverviewDetailReportObj implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "service_name")
    public String serviceName;

    @Column(name = "last_month_spend")
    public String lastMonthSpend;

    @Column(name = "this_month_spend")
    public String thisMonthSpend;

    @Column(name = "forecasted_spend")
    public String forecastedSpend;

    @Column(name = "avg_daily_spend")
    public String avgDailySpend;

    @Column(name = "variance")
    public String variance;


}
