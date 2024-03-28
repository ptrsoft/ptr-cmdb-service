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
public class SpendOverviewElementSummaryReportObj implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "instance_desc")
    public String instance_desc;

    @Column(name = "current_total")
    public String current_total;

    @Column(name = "previous_total")
    public String previous_total;

    @Column(name = "variance")
    public String variance;

}
