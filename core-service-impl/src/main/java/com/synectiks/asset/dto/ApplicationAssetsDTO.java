package com.synectiks.asset.dto;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.synectiks.asset.domain.ApplicationAssets} entity.
 */
public class ApplicationAssetsDTO implements Serializable {
    
    private Long id;

    private String tenantId;

    private String dashboardUuid;

    private String fileName;

    private String cloudType;

    private String elementType;

    private String elementSubType;

    private String inputType;

    private String dashboardNature;

    private String status;

    private Instant createdOn;

    private Instant updatedOn;

    private String updatedBy;

    private String createdBy;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getDashboardUuid() {
        return dashboardUuid;
    }

    public void setDashboardUuid(String dashboardUuid) {
        this.dashboardUuid = dashboardUuid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCloudType() {
        return cloudType;
    }

    public void setCloudType(String cloudType) {
        this.cloudType = cloudType;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getElementSubType() {
        return elementSubType;
    }

    public void setElementSubType(String elementSubType) {
        this.elementSubType = elementSubType;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getDashboardNature() {
        return dashboardNature;
    }

    public void setDashboardNature(String dashboardNature) {
        this.dashboardNature = dashboardNature;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationAssetsDTO)) {
            return false;
        }

        return id != null && id.equals(((ApplicationAssetsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationAssetsDTO{" +
            "id=" + getId() +
            ", tenantId='" + getTenantId() + "'" +
            ", dashboardUuid='" + getDashboardUuid() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", cloudType='" + getCloudType() + "'" +
            ", elementType='" + getElementType() + "'" +
            ", elementSubType='" + getElementSubType() + "'" +
            ", inputType='" + getInputType() + "'" +
            ", dashboardNature='" + getDashboardNature() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
