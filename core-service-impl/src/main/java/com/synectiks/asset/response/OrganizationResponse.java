package com.synectiks.asset.response;


import com.synectiks.asset.business.domain.Organization;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String description;
    private int totalDepartment;
    
    private List<DepartmentResponse> departmentList;

    public static OrganizationResponse from(Organization organization) {
    	return OrganizationResponse.builder()
    			.id(organization.getId())
    			.name(organization.getName())
    			.build();
    }
}

