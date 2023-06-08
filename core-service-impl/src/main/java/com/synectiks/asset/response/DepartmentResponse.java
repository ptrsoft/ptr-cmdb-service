package com.synectiks.asset.response;

import com.synectiks.asset.business.domain.Department;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private String name;
  private String status;
  private int totalProduct;
  private List<ProductResponse> productList;
  
  public static DepartmentResponse from(Department department) {
	  return DepartmentResponse.builder()
			  .id(department.getId())
			  .name(department.getName())
			  .status(department.getStatus())
			  .build();
  }
  
  public static DepartmentResponse from(Long id, String name, String status) {
	  DepartmentResponse dr = DepartmentResponse.builder().build();
	  if(id != null) {
		  dr.setId(id);
	  }
	  if(!StringUtils.isBlank(name)) {
		  dr.setName(name);
	  }
	  if(!StringUtils.isBlank(status)) {
		  dr.setStatus(status);
	  }
	  return dr;
  }
  
}
