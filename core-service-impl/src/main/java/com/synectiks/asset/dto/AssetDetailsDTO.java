package com.synectiks.asset.dto;

import javax.persistence.Lob;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.synectiks.asset.domain.AssetDetails} entity.
 */
public class AssetDetailsDTO implements Serializable {
    
    private Long id;

    private String name;

    @Size(max = 5000)
    private String description;

    private Integer totalInstances;

    @Size(max = 1000)
    private String viewJsonRef;

    @Lob
    private byte[] viewJson;

    private String viewJsonContentType;
    @Size(max = 1000)
    private String sourceJsonRef;

    @Lob
    private byte[] sourceJson;

    private String sourceJsonContentType;
    private String status;

    private Instant createdOn;

    private Instant updatedOn;

    private String updatedBy;

    private String createdBy;


    private Long cloudAssetId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalInstances() {
        return totalInstances;
    }

    public void setTotalInstances(Integer totalInstances) {
        this.totalInstances = totalInstances;
    }

    public String getViewJsonRef() {
        return viewJsonRef;
    }

    public void setViewJsonRef(String viewJsonRef) {
        this.viewJsonRef = viewJsonRef;
    }

    public byte[] getViewJson() {
        return viewJson;
    }

    public void setViewJson(byte[] viewJson) {
        this.viewJson = viewJson;
    }

    public String getViewJsonContentType() {
        return viewJsonContentType;
    }

    public void setViewJsonContentType(String viewJsonContentType) {
        this.viewJsonContentType = viewJsonContentType;
    }

    public String getSourceJsonRef() {
        return sourceJsonRef;
    }

    public void setSourceJsonRef(String sourceJsonRef) {
        this.sourceJsonRef = sourceJsonRef;
    }

    public byte[] getSourceJson() {
        return sourceJson;
    }

    public void setSourceJson(byte[] sourceJson) {
        this.sourceJson = sourceJson;
    }

    public String getSourceJsonContentType() {
        return sourceJsonContentType;
    }

    public void setSourceJsonContentType(String sourceJsonContentType) {
        this.sourceJsonContentType = sourceJsonContentType;
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

    public Long getCloudAssetId() {
        return cloudAssetId;
    }

    public void setCloudAssetId(Long cloudAssetId) {
        this.cloudAssetId = cloudAssetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetDetailsDTO)) {
            return false;
        }

        return id != null && id.equals(((AssetDetailsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssetDetailsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", totalInstances=" + getTotalInstances() +
            ", viewJsonRef='" + getViewJsonRef() + "'" +
            ", viewJson='" + getViewJson() + "'" +
            ", sourceJsonRef='" + getSourceJsonRef() + "'" +
            ", sourceJson='" + getSourceJson() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", cloudAssetId=" + getCloudAssetId() +
            "}";
    }
}
