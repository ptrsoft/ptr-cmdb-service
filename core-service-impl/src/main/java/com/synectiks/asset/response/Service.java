package com.synectiks.asset.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<CommonService> common;
	private List<BusinessService> business;
		
}
