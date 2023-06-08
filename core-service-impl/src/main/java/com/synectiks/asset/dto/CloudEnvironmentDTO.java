package com.synectiks.asset.dto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.synectiks.asset.business.domain.CloudEnvironment} entity.
 */
public class CloudEnvironmentDTO implements Serializable {

  private Long id;

  private String name;

  @Size(max = 5000)
  private String description;

  private String accountId;

  private String accessKey;

  private String secretKey;

  private String region;

  private String vaultId;

  private Long orgId;

  private String status;

  private Instant createdOn;

  private Instant updatedOn;

  private String updatedBy;

  private String createdBy;

  private CloudDTO cloudId;

  private DepartmentDTO departmentid;

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

  public String getVaultId() {
    return vaultId;
  }

  public void setVaultId(String vaultId) {
    this.vaultId = vaultId;
  }

  public Long getOrgId() {
    return orgId;
  }

  public void setOrgId(Long orgId) {
    this.orgId = orgId;
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

  public CloudDTO getCloudId() {
    return cloudId;
  }

  public void setCloudId(CloudDTO cloudId) {
    this.cloudId = cloudId;
  }

  public DepartmentDTO getDepartmentid() {
    return departmentid;
  }

  public void setDepartmentid(DepartmentDTO departmentid) {
    this.departmentid = departmentid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CloudEnvironmentDTO)) {
      return false;
    }

    CloudEnvironmentDTO cloudEnvironmentDTO = (CloudEnvironmentDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, cloudEnvironmentDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "CloudEnvironmentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", accountId='" + getAccountId() + "'" +
            ", accessKey='" + getAccessKey() + "'" +
            ", secretKey='" + getSecretKey() + "'" +
            ", region='" + getRegion() + "'" +
            ", vaultId='" + getVaultId() + "'" +
            ", orgId=" + getOrgId() +
            ", status='" + getStatus() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", cloudId=" + getCloudId() +
            ", departmentid=" + getDepartmentid() +
            "}";
    }
}
