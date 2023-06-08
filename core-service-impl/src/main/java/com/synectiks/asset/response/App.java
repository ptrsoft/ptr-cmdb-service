package com.synectiks.asset.response;

import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class App implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private Long dbid;
	private String name;
	private Long serviceDetailId;
	private String description;
	private String associatedCloudElement;
	private String associatedClusterNamespace;
	private String associatedManagedCloudServiceLocation;
	private String associatedGlobalServiceLocation;
	private String serviceHostingType;
	private String associatedCloudElementId;
	private String appType;
	
	private String associatedOU;
	private String associatedDept;
	private String associatedProduct;
	private String associatedEnv;
	private String serviceType;
	

	private PerformanceResponse performance;
	private AvailabilityResponse availability;
	private SecurityResponse security;
	private DataProtectionResponse dataProtection;
	private UserExperianceResponse userExperiance;
	
	private Map<String, Object> slaJson;
	
}
