package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface CloudWiseLandingzoneCountQueryObj extends Serializable{

    @Value("#{target.id}")
    Long getId();

    @Value("#{target.organization_id}")
    Long getOrganizationId();

    @Value("#{target.organization_name}")
    String getOrganizationName();

    @Value("#{target.cloud}")
    String getCloud();

    @Value("#{target.total_accounts}")
    Long getTotalAccounts();

}
