package com.synectiks.asset.dto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;


/**
 * A DTO for the {@link com.synectiks.asset.business.domain.Catalogue} entity.
 */
public class CatalogueDTO implements Serializable {

  private Long id;

  private String name;

  @Size(max = 5000)
  private String description;

  private String uuid;

  private String dashboardNature;

  private String awsBucket;

  private String fileName;

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

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getDashboardNature() {
    return dashboardNature;
  }

  public void setDashboardNature(String dashboardNature) {
    this.dashboardNature = dashboardNature;
  }

  public String getAwsBucket() {
    return awsBucket;
  }

  public void setAwsBucket(String awsBucket) {
    this.awsBucket = awsBucket;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
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
    if (!(o instanceof CatalogueDTO)) {
      return false;
    }

    CatalogueDTO catalogueDTO = (CatalogueDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, catalogueDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "CatalogueDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", uuid='" + getUuid() + "'" +
            ", dashboardNature='" + getDashboardNature() + "'" +
            ", awsBucket='" + getAwsBucket() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
