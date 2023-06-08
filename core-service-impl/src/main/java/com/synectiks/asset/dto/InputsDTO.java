package com.synectiks.asset.dto;

import javax.persistence.Lob;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.synectiks.asset.domain.Inputs} entity.
 */
public class InputsDTO implements Serializable {
    
    private Long id;

    private String accountId;

    private String tenantId;

    private String inputSource;

    private String inputSourceId;

    private String name;

    @Size(max = 5000)
    private String description;

    private String status;

    @Size(max = 500)
    private String refUrl;

    private String type;

    @Lob
    private byte[] viewJson;

    private String viewJsonContentType;
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

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getInputSource() {
        return inputSource;
    }

    public void setInputSource(String inputSource) {
        this.inputSource = inputSource;
    }

    public String getInputSourceId() {
        return inputSourceId;
    }

    public void setInputSourceId(String inputSourceId) {
        this.inputSourceId = inputSourceId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRefUrl() {
        return refUrl;
    }

    public void setRefUrl(String refUrl) {
        this.refUrl = refUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        if (!(o instanceof InputsDTO)) {
            return false;
        }

        return id != null && id.equals(((InputsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InputsDTO{" +
            "id=" + getId() +
            ", accountId='" + getAccountId() + "'" +
            ", tenantId='" + getTenantId() + "'" +
            ", inputSource='" + getInputSource() + "'" +
            ", inputSourceId='" + getInputSourceId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", refUrl='" + getRefUrl() + "'" +
            ", type='" + getType() + "'" +
            ", viewJson='" + getViewJson() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
