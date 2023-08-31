package com.synectiks.asset.domain.query;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.Map;

public interface InfraTopologyQueryObj extends Serializable{
    @Value("#{target.id}")
    Long getId();

    @Value("#{target.instance_id}")
    String getInstanceId();

    @Value("#{target.instance_name}")
    String getInstanceName();

    @Value("#{target.three_tier}")
    ObjectNode getThreeTier();

    @Value("#{target.soa}")
    ObjectNode getSoa();

}
