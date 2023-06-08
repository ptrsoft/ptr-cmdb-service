package com.synectiks.asset.response;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private String name;
  private String description;
  private String status;
  
  private List<DeploymentEnvironmentResponse> deploymentEnvironmentList;
  
  public static ProductResponse from(String product) {
	  return ProductResponse.builder()
//			  .id(product.getId())
			  .name(product)
//			  .description(product.getDescription())
//			  .status(product.getStatus())
			  .build();
  }
  
  public static ProductResponse from(Long id, String name, String description, String status) {
	  ProductResponse pr = ProductResponse.builder().build();
	  if(id != null) {
		  pr.setId(id);
	  }
	  if(!StringUtils.isBlank(name)) {
		  pr.setName(name);
	  }
	  if(!StringUtils.isBlank(description)) {
		  pr.setDescription(description);
	  }
	  if(!StringUtils.isBlank(status)) {
		  pr.setStatus(status);
	  }
	  return pr;
  }
}
