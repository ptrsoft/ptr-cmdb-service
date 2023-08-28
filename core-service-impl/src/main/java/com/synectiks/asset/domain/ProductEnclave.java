package com.synectiks.asset.domain;

import com.synectiks.asset.service.CustomeHashMapConverter;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

/**
 * A ProductEnclave.
 */
@Entity
@Table(name = "product_enclave")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEnclave extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "instance_name")
	private String instanceName;

	@Column(name = "instance_id")
	private String instanceId;

	@Column(name = "state")
	private String state;

	@Convert(converter = CustomeHashMapConverter.class)
	@Column(name = "metadata", columnDefinition = "jsonb")
	private Map<String, Object> metadata;

	@Column(name = "status")
	private String status;

	@ManyToOne
	private Department department;

	@ManyToOne
	private Landingzone landingzone;
}
