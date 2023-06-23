package com.synectiks.asset.domain.query;

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
public class EnvironmentSummaryQueryObj implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "cloud")
    private String cloud;

    @Column(name = "landing_zone")
    private String landingZone;

    @Column(name = "product_enclave")
    private Long productEnclave;

    @Column(name = "product")
    private Long product;

    @Column(name = "app_service")
    private Long appService;

    @Column(name = "data_service")
    private Long dataService;


}
