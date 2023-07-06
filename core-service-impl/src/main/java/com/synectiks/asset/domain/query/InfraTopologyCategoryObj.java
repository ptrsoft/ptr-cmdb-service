package com.synectiks.asset.domain.query;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfraTopologyCategoryObj implements Serializable{

    public String category;
    public String elementType;
    public String total;
    public String cpuUtilization;
    public String memory;
    public String networkBytes;
    public String networkBytesOut;
    public String cpuReservation;
    public String memoryReservation;
    public List<InfraTopologyElementObj> elementList;

}
