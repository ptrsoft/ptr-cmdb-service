package com.synectiks.asset.domain.query;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvironmentQueryObj implements Serializable{

    public String cloud;
    public List<EnvironmentSummaryQueryObj> environmentSummaryList;

}
