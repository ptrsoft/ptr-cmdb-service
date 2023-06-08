package com.synectiks.asset.dto;

import javax.persistence.Lob;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.synectiks.asset.domain.CloudAsset} entity.
 */
public class CloudAssetDTO implements Serializable {
    
    private Long id;

    private String accountId;

    private String type;

    private String name;

    @Size(max = 5000)
    private String description;

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

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CloudAssetDTO)) {
            return false;
        }

        return id != null && id.equals(((CloudAssetDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CloudAssetDTO{" +
            "id=" + getId() +
            ", accountId='" + getAccountId() + "'" +
            ", type='" + getType() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", sourceJsonRef='" + getSourceJsonRef() + "'" +
            ", sourceJson='" + getSourceJson() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
