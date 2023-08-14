package com.synectiks.asset.domain.query;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThreeTierQueryObj implements Serializable{

    public Long productCount;
    public Long webCount;
    public Long appCount;
    public Long dataCount;
    public Long auxiliaryCount;

}
