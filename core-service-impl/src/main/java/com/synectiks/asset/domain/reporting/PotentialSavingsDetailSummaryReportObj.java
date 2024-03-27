package com.synectiks.asset.domain.reporting;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PotentialSavingsDetailSummaryReportObj implements Serializable{

    private Long id;

    public String instanceType;

    public String label;

    public String currentTotal;
    public String previousTotal;
    public String variance;

    public static PotentialSavingsDetailSummaryReportObj build(Long id, String instanceType, String label, String currentTotal,
                                                                String previousTotal, String variance){
        return PotentialSavingsDetailSummaryReportObj.builder()
                .id(id)
                .instanceType(instanceType)
                .label(label)
                .currentTotal(currentTotal)
                .previousTotal(previousTotal)
                .variance(variance)
                .build();

    }
}
