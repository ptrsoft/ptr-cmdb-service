package com.synectiks.asset.domain;

import com.synectiks.asset.service.CustomeHashMapConverter;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

/**
 * A MicroService.
 */
@Entity
@Table(name = "micro_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MicroService extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "department")
	private String department;

	@Column(name = "product")
	private String product;

	@Column(name = "environment")
	private String environment;

	@Column(name = "service_type")
	private String serviceType;

	@Convert(converter = CustomeHashMapConverter.class)
	@Column(name = "service_topology", columnDefinition = "jsonb")
	private Map<String, Object> serviceTopology;
	
	@Convert(converter = CustomeHashMapConverter.class)
	@Column(name = "business_location", columnDefinition = "jsonb")
	private Map<String, Object> businessLocation;
	
	@Convert(converter = CustomeHashMapConverter.class)
	@Column(name = "sla_json", columnDefinition = "jsonb")
	private Map<String, Object> slaJson;
	
	@Convert(converter = CustomeHashMapConverter.class)
	@Column(name = "cost_json", columnDefinition = "jsonb")
	private Map<String, Object> costJson;
	
	@Convert(converter = CustomeHashMapConverter.class)
	@Column(name = "view_json", columnDefinition = "jsonb")
	private Map<String, Object> viewJson;
	
	@Convert(converter = CustomeHashMapConverter.class)
	@Column(name = "config_json", columnDefinition = "jsonb")
	private Map<String, Object> configJson;
	
	@Convert(converter = CustomeHashMapConverter.class)
	@Column(name = "compliance_json", columnDefinition = "jsonb")
	private Map<String, Object> complianceJson;
	
	@Column(name = "status")
	private String status;

	@Column(name = "department_id")
	private Long departmentId;

	@Column(name = "deployment_environment_id")
	private String  deploymentEnvironmentId;
}
