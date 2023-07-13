package com.synectiks.asset.domain;

import com.synectiks.asset.service.CustomeHashMapConverter;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

/**
 * A CloudElement.
 */
@Entity
@Table(name = "cloud_element")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CloudElement extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

//	@Column(name = "instance_id")
//	private String instanceId;

	@Column(name = "element_type")
	private String elementType;

//	@Column(name = "arn")
//	private String arn;

	@Convert(converter = CustomeHashMapConverter.class)
	@Column(name = "cloud_identity", columnDefinition = "jsonb")
	private Map<String, Object> cloudIdentity;
	
	@Convert(converter = CustomeHashMapConverter.class)
	@Column(name = "hardware_location", columnDefinition = "jsonb")
	private Map<String, Object> hardwareLocation;
	
	@Convert(converter = CustomeHashMapConverter.class)
	@Column(name = "hosted_services", columnDefinition = "jsonb")
	private Map<String, Object> hostedServices;
	
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
	
	@Column(name = "tag_status")
	private String tagStatus;
	
	@Column(name = "status")
	private String status;

	@ManyToOne
	private CloudEnvironment cloudEnvironment;

}
