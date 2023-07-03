package com.synectiks.asset.domain.query;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfraTopologyProductEnclaveObj implements Serializable{

    public String name;
    public String id;
    public String state;
    public List<InfraTopologyHostingTypeObj> hostingTypeList;

}
