package com.synectiks.asset.response.query;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvironmentDto implements Serializable{

    public String cloud;
    public List<EnvironmentSummaryDto> environmentSummaryList;

}
