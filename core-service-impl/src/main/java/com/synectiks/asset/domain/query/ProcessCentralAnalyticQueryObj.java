package com.synectiks.asset.domain.query;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface ProcessCentralAnalyticQueryObj extends Serializable{

    @Value("#{target.dev_central}")
    ObjectNode getDevCentral();
    
    @Value("#{target.sec_central}")
    ObjectNode getSecCentral();
    
    @Value("#{target.ops_central}")
    ObjectNode getOpsCentral();
    

}
