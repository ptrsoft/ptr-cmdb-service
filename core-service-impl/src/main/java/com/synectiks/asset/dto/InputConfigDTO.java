package com.synectiks.asset.dto;

import javax.persistence.Lob;
import java.io.Serializable;

/**
 * A DTO for the {@link com.synectiks.asset.domain.InputConfig} entity.
 */
public class InputConfigDTO implements Serializable {
    
    private Long id;

    private String inputType;

    private String status;

    private String tenantId;

    @Lob
    private byte[] viewJson;

    private String viewJsonContentType;

    private Long accountsId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

    public Long getAccountsId() {
        return accountsId;
    }

    public void setAccountsId(Long accountsId) {
        this.accountsId = accountsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InputConfigDTO)) {
            return false;
        }

        return id != null && id.equals(((InputConfigDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InputConfigDTO{" +
            "id=" + getId() +
            ", inputType='" + getInputType() + "'" +
            ", status='" + getStatus() + "'" +
            ", tenantId='" + getTenantId() + "'" +
            ", viewJson='" + getViewJson() + "'" +
            ", accountsId=" + getAccountsId() +
            "}";
    }
}
