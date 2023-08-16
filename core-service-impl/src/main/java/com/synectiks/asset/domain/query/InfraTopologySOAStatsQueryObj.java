package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface InfraTopologySOAStatsQueryObj extends Serializable{

    @Value("#{target.app_count}")
    Long getAppCount();

    @Value("#{target.data_count}")
    Long getDataCount();

    @Value("#{target.other_count}")
    Long getOtherCount();

}
