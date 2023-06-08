package com.synectiks.asset.response.catalogue;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Datasource implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private String description;
	private String type;
	private String dataType;
	private String associatedWorkflowTemplate;
	private String associatedCloud;
	private String associatedCreds;
	private String associatedApplicationLocation;
	private String associatedTargetDs;

}
  

