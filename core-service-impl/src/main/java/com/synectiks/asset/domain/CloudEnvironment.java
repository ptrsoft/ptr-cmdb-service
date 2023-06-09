package com.synectiks.asset.domain;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * CloudEnvironment
 */
@Entity
@Table(name = "cloud_environment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CloudEnvironment extends AbstractAuditingEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Size(max = 5000)
  @Column(name = "description", length = 5000)
  private String description;

  @Column(name = "account_id")
  private String accountId;

  @Column(name = "status")
  private String status;

  @Column(name = "cloud")
  private String cloud;

  @Column(name = "display_name")
  private String displayName;
  
  @Column(name = "role_arn")
  private String roleArn;
  
  @Column(name = "external_id")
  private String externalId;
  
  @ManyToOne
  private Department department;

}
