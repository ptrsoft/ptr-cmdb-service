package com.synectiks.asset.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDetailResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;
  private String description;
  private String associatedOU;
  private String associatedDept;
  private String associatedProduct;
  private String associatedEnv;
  private String associatedLandingZone;
  private String associatedProductEnclave;
  private String associatedCluster;
  private String serviceNature; 
  private String associatedCommonService;
  private String associatedBusinessService;
  private String serviceType; 
  private String serviceHostingType;        
  private String associatedClusterNamespace;
  private String associatedManagedCloudServiceLocation;
  private String associatedCloudElementId;
  private String associatedCloudElement;
  private String associatedGlobalServiceLocation;  
  private ServiceDetailStats stats;
  
  private PerformanceResponse performance;
  private AvailabilityResponse availability;
  private SecurityResponse security;
  private DataProtectionResponse dataProtection;
  private UserExperianceResponse userExperiance;
  
}
