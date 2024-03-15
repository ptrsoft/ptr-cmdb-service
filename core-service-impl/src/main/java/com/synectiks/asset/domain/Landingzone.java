package com.synectiks.asset.domain;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A Landingzone
 */
@Entity
@Table(name = "landingzone")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Landingzone extends AbstractAuditingEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Size(max = 5000)
  @Column(name = "description", length = 5000)
  private String description;

  @Column(name = "landing_zone")
  private String landingZone;

  @Column(name = "cloud")
  private String cloud;

  @Column(name = "display_name")
  private String displayName;

  @Column(name = "role_arn")
  private String roleArn;

  @Column(name = "external_id")
  private String externalId;

  @Column(name = "status")
  private String status;

  @ManyToOne
  private Department department;

  @ManyToOne
  private Organization organization;

}
