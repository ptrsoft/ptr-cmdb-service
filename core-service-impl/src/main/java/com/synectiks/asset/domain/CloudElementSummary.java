package com.synectiks.asset.domain;

import com.synectiks.asset.service.CustomeHashMapConverter;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

/**
 * CloudElementSummary
 */
@Entity
@Table(name = "cloud_element_summary")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CloudElementSummary extends AbstractAuditingEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Convert(converter = CustomeHashMapConverter.class)
  @Column(name = "summary_json", columnDefinition = "jsonb")
  private Map<String, Object> summaryJson;
  
  @ManyToOne
  private CloudEnvironment cloudEnvironment;
  
}
