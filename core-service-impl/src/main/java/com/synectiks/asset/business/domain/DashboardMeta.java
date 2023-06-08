package com.synectiks.asset.business.domain;

import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardMeta implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean isStarred = false;
	private boolean isHome = false;
	private boolean isSnapshot = false;
	private String type = "db";
	private boolean canSave = false;
	private boolean canEdit = false;
	private boolean canAdmin = false;
	private boolean canStar = false;
	private String slug;
	private String url;
	private int version;
	private boolean hasAcl = false;
	private boolean isFolder = false;
	private Long folderId;
	private String folderTitle;
	private String folderUrl;
	private boolean provisioned = false;
	private String provisionedExternalId;

}
