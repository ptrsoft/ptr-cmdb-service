package com.synectiks.asset.domain.reporting;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SpendOverviewReportObj implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "service_category")
    public String serviceCategory;

    @Column(name = "total")
    public Long total;

    @Column(name = "percentage")
    public String percentage;


}
