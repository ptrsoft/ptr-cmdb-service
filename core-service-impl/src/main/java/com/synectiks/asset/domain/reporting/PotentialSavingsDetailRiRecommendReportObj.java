package com.synectiks.asset.domain.reporting;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PotentialSavingsDetailRiRecommendReportObj implements Serializable{


    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "element_type")
    public String elementType;

    @Column(name = "instance_id")
    public String instanceId;

    @Column(name = "recommendation")
    public String recommendation;

    @Column(name = "current_instance")
    public String currentInstance;

    @Column(name = "recommended_instance")
    public String recommendedInstance;

    @Column(name = "terms")
    public String terms;

    @Column(name = "payment_mode")
    public String paymentMode;

    @Column(name = "upfront_cost")
    public String upfrontCost;

    @Column(name = "per_hour_cost")
    public String perHourCost;

    @Column(name = "estimated_savings")
    public String estimatedSavings;

    @Column(name = "total_spend")
    public String totalSpend;


}
