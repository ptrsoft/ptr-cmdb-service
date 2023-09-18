package com.synectiks.asset.handler;

import com.synectiks.asset.domain.Department;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.domain.Organization;

public interface CloudHandler {
    public void save(Organization organization, Department department, Landingzone landingZone, String awsRegion);
    default String getUrl() {
        return null;
    }
}
