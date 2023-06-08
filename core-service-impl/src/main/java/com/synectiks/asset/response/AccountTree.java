package com.synectiks.asset.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountTree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String account;
	private List<Vpc> vpcs;


}
