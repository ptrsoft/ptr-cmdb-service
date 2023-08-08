package com.synectiks.asset.domain;

import com.synectiks.asset.service.CustomeHashMapConverter;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

/**
 * A Module.
 */
@Entity
@Table(name = "module")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Module extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "module_nature")
	private String moduleNature;

	@Convert(converter = CustomeHashMapConverter.class)
	@Column(name = "service_topology", columnDefinition = "jsonb")
	private Map<String, Object> serviceTopology;

	@Convert(converter = CustomeHashMapConverter.class)
	@Column(name = "metadata", columnDefinition = "jsonb")
	private Map<String, Object> metadata;

	@Column(name = "status")
	private String status;

	@ManyToOne
	private Product product;

	@ManyToOne
	private ProductEnv productEnv;

}
