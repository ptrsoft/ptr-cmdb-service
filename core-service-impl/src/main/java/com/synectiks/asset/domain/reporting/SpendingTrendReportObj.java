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
public class SpendingTrendReportObj implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "tenure")
    public String tenure;

    @Column(name = "dates")
    public String dates;

    @Column(name = "total")
    public Long total;


}
