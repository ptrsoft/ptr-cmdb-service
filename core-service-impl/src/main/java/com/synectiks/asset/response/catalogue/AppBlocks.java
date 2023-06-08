package com.synectiks.asset.response.catalogue;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppBlocks implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<CommonMicroservices> commonMicroservices;
	private List<Generators> generators;
	private List<Workflows> workflows;
}
