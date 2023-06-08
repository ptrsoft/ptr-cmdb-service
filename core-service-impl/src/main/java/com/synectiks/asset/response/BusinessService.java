package com.synectiks.asset.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private String associatedOU;
	private String associatedDept;
	private List<App> app;
	private List<Data> data;
		
}
