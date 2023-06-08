package com.synectiks.asset.response.catalogue;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DevResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private AppBlocks appBlocks;
	private LibraryAndSDKs libraryAndSDKs;
	private BuildDeployTemplates buildDeployTemplates;
	private ISVSolutions iSVSolutions;
	private DataFlows dataFlows;
}
