package com.synectiks.asset.response;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private String name;
  private String description;
  private String status;
//  private ServiceBillingResponse serviceBilling;
//  private PerformanceResponse performance;
//  private AvailabilityResponse availability;
//  private SecurityResponse security;
//  private DataProtectionResponse dataProtection;
//  private UserExperianceResponse userExperiance;
//  private List<ServiceTagResponse> tagList;
  
//  public static ServiceResponse from(Services services) {
//	  return ServiceResponse.builder()
//			  .id(services.getId())
//			  .name(services.getName())
//			  .description(services.getDescription())
//			  .status(services.getStatus())
//			  .build();
//  }
  
  public static ServiceResponse from(Long id, String name, String description, String status) {
	  ServiceResponse sr = ServiceResponse.builder().build();
	  if(id != null) {
		  sr.setId(id);
	  }
	  if(!StringUtils.isBlank(name)) {
		  sr.setName(name);
	  }
	  if(!StringUtils.isBlank(description)) {
		  sr.setDescription(description);
	  }
	  if(!StringUtils.isBlank(description)) {
		  sr.setDescription(description);
	  }
	  if(!StringUtils.isBlank(status)) {
		  sr.setStatus(status);
	  }
	  return sr;
  }
}
