package com.synectiks.asset.domain.query;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface ApplicationTopologyQueryObj extends Serializable{

    @Value("#{target.landing_zone_id}")
    Long getLandingZoneId();

    @Value("#{target.landing_zone}")
    String getLandingZone();

    @Value("#{target.department_id}")
    Long getDepartmentId();

    @Value("#{target.application}")
    String getApplication();

    @Value("#{target.lob}")
    String getLob();

    @Value("#{target.environment}")
    String getEnvironment();

    @Value("#{target.app_type}")
    String getAppType();

    @Value("#{target.sle}")
    ObjectNode getSle();

    @Value("#{target.end_usage}")
    ObjectNode getEndUsage();

    @Value("#{target.cost}")
    ObjectNode getCost();

}
