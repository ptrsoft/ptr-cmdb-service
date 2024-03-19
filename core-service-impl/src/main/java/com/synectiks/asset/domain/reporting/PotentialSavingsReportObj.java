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
public class PotentialSavingsReportObj implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "instance_type")
    public String instanceType;

    @Column(name = "total")
    public String total;

}
