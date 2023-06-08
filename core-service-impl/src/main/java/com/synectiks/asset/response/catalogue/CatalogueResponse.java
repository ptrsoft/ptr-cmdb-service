package com.synectiks.asset.response.catalogue;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogueResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private DevResponse dev;
	private SecResponse sec;
	private OpsResponse ops;
	
}
