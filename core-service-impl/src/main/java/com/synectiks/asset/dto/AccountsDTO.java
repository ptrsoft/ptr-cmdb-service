package com.synectiks.asset.dto;

import javax.persistence.Lob;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.synectiks.asset.domain.Accounts} entity.
 */
public class AccountsDTO implements Serializable {
    
    private Long id;

    private String name;

    @Size(max = 5000)
    private String description;

    private String accountId;

    private String tenantId;

    private String accessKey;

    private String secretKey;

    private String region;

    private String bucket;

    private String endPoint;

    private String email;

    private String password;

    private String cloudType;

    @Size(max = 1000)
    private String sourceJsonRef;

    @Lob
    private byte[] sourceJson;

    private String sourceJsonContentType;
    private String status;

    private String hashiCorpVaultId;

    private Instant createdOn;

    private Instant updatedOn;

    private String updatedBy;

    private String createdBy;


    private Long organizationalUnitId;
    
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

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCloudType() {
        return cloudType;
    }

    public void setCloudType(String cloudType) {
        this.cloudType = cloudType;
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

    public String getHashiCorpVaultId() {
        return hashiCorpVaultId;
    }

    public void setHashiCorpVaultId(String hashiCorpVaultId) {
        this.hashiCorpVaultId = hashiCorpVaultId;
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

    public Long getOrganizationalUnitId() {
        return organizationalUnitId;
    }

    public void setOrganizationalUnitId(Long organizationalUnitId) {
        this.organizationalUnitId = organizationalUnitId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountsDTO)) {
            return false;
        }

        return id != null && id.equals(((AccountsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountsDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", accountId='" + getAccountId() + "'" +
            ", tenantId='" + getTenantId() + "'" +
            ", accessKey='" + getAccessKey() + "'" +
            ", secretKey='" + getSecretKey() + "'" +
            ", region='" + getRegion() + "'" +
            ", bucket='" + getBucket() + "'" +
            ", endPoint='" + getEndPoint() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", cloudType='" + getCloudType() + "'" +
            ", sourceJsonRef='" + getSourceJsonRef() + "'" +
            ", sourceJson='" + getSourceJson() + "'" +
            ", status='" + getStatus() + "'" +
            ", hashiCorpVaultId='" + getHashiCorpVaultId() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", organizationalUnitId=" + getOrganizationalUnitId() +
            "}";
    }
}
