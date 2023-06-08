package com.synectiks.asset.business.domain;


import com.synectiks.asset.config.Constants;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Base abstract class for entities which will hold definitions for created, last modified, created by,
 * last modified by attributes.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
//@JsonIgnoreProperties(value = {"createdBy", "createdOn", "updatedBy", "updatedOn"}) // If we uncomment this, These properties will be ignored in response json
public abstract class AbstractAuditingEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public abstract T getId();

    @CreatedBy
    @Column(name = "created_by", nullable = false, length = 255, updatable = false)
//    @JsonIgnore
    protected String createdBy;

    @CreatedDate
    @Column(name = "created_on", updatable = false, columnDefinition = "TIMESTAMP")
//    @JsonIgnore
    protected LocalDateTime createdOn = LocalDateTime.now(ZoneId.of(Constants.DEFAULT_TIMEZONE));

    @LastModifiedBy
    @Column(name = "updated_by", length = 255)
//    @JsonIgnore
    protected String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_on", columnDefinition = "TIMESTAMP")
//    @JsonIgnore
    protected LocalDateTime updatedOn = LocalDateTime.now(ZoneId.of(Constants.DEFAULT_TIMEZONE));


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
}
