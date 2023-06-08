package com.synectiks.asset.response;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class ServiceNameResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private String name;
//  private List<ServiceTagResponse> tagList;

  
//  public static ServiceNameResponse from(ServiceTagLink services) {
//	  return ServiceNameResponse.builder()
//			  .id(services.getServices().getId())
//			  .name(services.getServices().getName())
//			  .build();
//  }
  
  public static ServiceNameResponse from(Long id, String name) {
	  ServiceNameResponse snr = ServiceNameResponse.builder().build();
	  if(id != null) {
		  snr.setId(id);
	  }
	  if(!StringUtils.isBlank(name)) {
		  snr.setName(name);
	  }
	  return snr;
  }
}
