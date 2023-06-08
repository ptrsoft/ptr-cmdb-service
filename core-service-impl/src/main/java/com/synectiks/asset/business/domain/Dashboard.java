package com.synectiks.asset.business.domain;

import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dashboard implements Serializable {

	private static final long serialVersionUID = 1L;

	private String cloudName;
	private String elementType;
	private String tenantId;
	private String accountId;
	private String inputType;
	private String fileName;

	private String inputSourceId;
	private String title;
	private String slug;
	private String uid;
	private String uuid;
	private String data;
	private boolean isCloud = true;

	private int orgId = 1;
	private int gnetId = 0;
	private int version = 1;
	private String pluginId;
	private int folderId = 0;
	private boolean isFolder = false;
	private boolean hasAcl = false;
	private String sourceJsonRef;
	private Long id;
	private String url;
	private DashboardMeta dashboardMeta;
	private String cloudElementId;

}
