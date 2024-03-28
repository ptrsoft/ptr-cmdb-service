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
public class SpendOverviewElementDetailReportObj implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "element_type")
    public String elementType;

    @Column(name = "instance_id")
    public String instanceId;

    @Column(name = "tags")
    public String tags;

    @Column(name = "instance_type")
    public String instanceType;

    @Column(name = "instance_status")
    public String instanceStatus;

    @Column(name = "pricing_model")
    public String pricingModel;

    @Column(name = "availability_zone")
    public String availabilityZone;

    @Column(name = "ondemand_cost_per_hr")
    public String ondemandCostPerHr;

    @Column(name = "ri_cost_per_hr")
    public String riCostPerHr;

    @Column(name = "usage_hours")
    public String usageHours;

    @Column(name = "add_ons")
    public String addOns;

    @Column(name = "total_spend")
    public String totalSpend;
}
