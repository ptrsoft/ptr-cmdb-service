package com.synectiks.asset.domain.query;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfraTopologyHostingTypeObj implements Serializable{

    public String hostingType;
    public List<InfraTopologyCategoryObj> category;

}
