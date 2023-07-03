package com.synectiks.asset.domain.query;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfraTopologyObj implements Serializable{
    public String landingZone;
    public List<InfraTopologyProductEnclaveObj> productEnclaveList;
    public List<String> globalServiceList;
}
