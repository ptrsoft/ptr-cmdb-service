package com.synectiks.asset.domain.query;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.service.CustomeHashMapConverter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Column;
import javax.persistence.Convert;
import java.io.Serializable;
import java.util.Map;

public interface CloudElementTagQueryObj extends Serializable{
    @Value("#{target.id}")
    Long getId();

    @Value("#{target.landingzone_id}")
    Long getLandingzoneId();

    @Value("#{target.instance_id}")
    String getInstanceId();

    @Value("#{target.tag}")
    ObjectNode getTag();

}
