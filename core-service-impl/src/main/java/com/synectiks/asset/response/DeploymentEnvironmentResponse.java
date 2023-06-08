package com.synectiks.asset.response;

import com.synectiks.asset.business.domain.DeploymentEnvironment;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeploymentEnvironmentResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private String name;

  public static DeploymentEnvironmentResponse from(DeploymentEnvironment depEnv) {
	  return DeploymentEnvironmentResponse.builder()
			  .id(depEnv.getId())
			  .name(depEnv.getName())
			  .build();
  }
  
  public static DeploymentEnvironmentResponse from(Long id, String name) {
	  DeploymentEnvironmentResponse der = DeploymentEnvironmentResponse.builder().build();
	  if(id != null) {
		  der.setId(id);
	  }
	  if(!StringUtils.isBlank(name)) {
		  der.setName(name);
	  }
	  return der;
  }
}
