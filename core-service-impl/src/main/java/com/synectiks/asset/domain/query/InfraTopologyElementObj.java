package com.synectiks.asset.domain.query;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfraTopologyElementObj implements Serializable{

    public String arn;
    public String name;
}
