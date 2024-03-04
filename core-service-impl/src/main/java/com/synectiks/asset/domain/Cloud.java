package com.synectiks.asset.domain;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Cloud.
 */
@Entity
@Table(name = "cloud")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cloud extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "element_type")
	private String elementType;

	@Column(name = "name")
	private String name;

	@Column(name = "is_cron_scheduled")
	private String isCronScheduled;

	@Column(name = "list_query")
	private String listQuery;

	@Column(name = "config_query")
	private String configQuery;

	@Column(name = "config_key")
	private String configKey;

	@Column(name = "ui_mapping")
	private String uiMapping;

	@Column(name = "status")
	private String status;

}
