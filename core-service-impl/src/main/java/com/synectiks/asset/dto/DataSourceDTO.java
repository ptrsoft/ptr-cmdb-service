package com.synectiks.asset.dto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.synectiks.asset.domain.DataSource} entity.
 */
public class DataSourceDTO implements Serializable {

  private Long id;

  private String name;

  @Size(max = 5000)
  private String description;

  private String type;

  private String status;

  private Instant createdOn;

  private Instant updatedOn;

  private String updatedBy;

  private String createdBy;

  private CloudEnvironmentDTO cloudEnvironment;

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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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

  public CloudEnvironmentDTO getCloudEnvironment() {
    return cloudEnvironment;
  }

  public void setCloudEnvironment(CloudEnvironmentDTO cloudEnvironment) {
    this.cloudEnvironment = cloudEnvironment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DataSourceDTO)) {
      return false;
    }

    DataSourceDTO dataSourceDTO = (DataSourceDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, dataSourceDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "DataSourceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", cloudEnvironment=" + getCloudEnvironment() +
            "}";
    }
}
