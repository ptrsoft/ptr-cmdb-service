package com.synectiks.asset.dto;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.synectiks.asset.business.domain.ServiceAllocation} entity.
 */
public class DepartmentProductDTO implements Serializable {

  private Long id;

  @Size(max = 5000)
  private String description;

  private String status;

  private Instant createdOn;

  private Instant updatedOn;

  private String updatedBy;

  private String createdBy;

  private ProductDTO product;

  private DepartmentDTO department;

  private CloudEnvironmentDTO cloudEnvironment;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public ProductDTO getProduct() {
    return product;
  }

  public void setProduct(ProductDTO product) {
    this.product = product;
  }

  public DepartmentDTO getDepartment() {
    return department;
  }

  public void setDepartment(DepartmentDTO department) {
    this.department = department;
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
    if (!(o instanceof DepartmentProductDTO)) {
      return false;
    }

    DepartmentProductDTO departmentProductDTO = (DepartmentProductDTO) o;
    if (this.id == null) {
      return false;
    }
    return Objects.equals(this.id, departmentProductDTO.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }

  // prettier-ignore
    @Override
    public String toString() {
        return "DepartmentProductDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdOn='" + getCreatedOn() + "'" +
            ", updatedOn='" + getUpdatedOn() + "'" +
            ", updatedBy='" + getUpdatedBy() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", product=" + getProduct() +
            ", department=" + getDepartment() +
            ", cloudEnvironment=" + getCloudEnvironment() +
            "}";
    }
}
