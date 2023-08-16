package com.synectiks.asset.domain.query;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public interface InfraTopology3TierStatsQueryObj extends Serializable{

    @Value("#{target.web_count}")
    Long getWebCount();

    @Value("#{target.app_count}")
    Long getAppCount();

    @Value("#{target.data_count}")
    Long getDataCount();

    @Value("#{target.auxiliary_count}")
    Long getAuxiliaryCount();

}
