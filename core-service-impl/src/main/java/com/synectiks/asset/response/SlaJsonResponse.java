package com.synectiks.asset.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlaJsonResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private String serviceId;
  
  
  public static SlaJsonResponse from(String serviceId, JsonNode jsonNode, String key) {
	  SlaJsonResponse vrj = SlaJsonResponse.builder().serviceId(serviceId).build();
	 
	  return vrj;
  }
  
}
