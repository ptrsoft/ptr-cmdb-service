package com.synectiks.asset.domain;

import com.synectiks.asset.service.CustomeHashMapConverter;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

/**
 * A CloudElementCost.
 */
@Entity
@Table(name = "cloud_element_cost")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CloudElementCost extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Convert(converter = CustomeHashMapConverter.class)
	@Column(name = "sla_json", columnDefinition = "jsonb")
	private Map<String, Object> slaJson;

	@Convert(converter = CustomeHashMapConverter.class)
	@Column(name = "cost_json", columnDefinition = "jsonb")
	private Map<String, Object> costJson;

	@Convert(converter = CustomeHashMapConverter.class)
	@Column(name = "compliance_json", columnDefinition = "jsonb")
	private Map<String, Object> complianceJson;

	@Column(name = "status")
	private String status;

	@Column(name = "cloud_element_id")
	private Long cloudElementId;

}
