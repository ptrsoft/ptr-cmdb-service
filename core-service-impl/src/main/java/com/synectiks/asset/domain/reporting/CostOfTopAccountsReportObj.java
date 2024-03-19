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
public class CostOfTopAccountsReportObj implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "department")
    public String department;

    @Column(name = "total")
    public Long total;

    @Column(name = "dep_allocated_budget")
    public Long depAllocatedBudget;

    @Column(name = "budget_consumed")
    public Long budgetConsumed;

}
