package com.synectiks.asset.business.domain;

import com.synectiks.asset.business.service.CatalogueConverter;
import com.synectiks.asset.response.catalogue.CatalogueResponse;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Inventory of dashboards
 */
@Entity
@Table(name = "catalogue")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Catalogue implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  @Column(name = "id")
  private Long id;

  @Convert(converter = CatalogueConverter.class)
  @Column(columnDefinition = "jsonb")
  private CatalogueResponse details;

}
