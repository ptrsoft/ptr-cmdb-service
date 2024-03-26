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
public class CostOfTopAccountsDetailReportObj implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "account_id")
    public String accountId;

    @Column(name = "department")
    public String department;

    @Column(name = "vpc")
    public String vpc;

    @Column(name = "service_count")
    public Long serviceCount;

    @Column(name = "high_spending_region")
    public String highSpendingRegion;

    @Column(name = "spending")
    public Long spending;

    @Column(name = "variance")
    public String variance;

    @Column(name = "budget")
    public Long budget;

}
