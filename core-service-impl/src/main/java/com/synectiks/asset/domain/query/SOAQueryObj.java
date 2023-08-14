package com.synectiks.asset.domain.query;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SOAQueryObj implements Serializable{

    public Long productCount;
    public Long appCount;
    public Long dataCount;
    public Long otherCount;

}
