package com.synectiks.asset.response.catalogue;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	private ComplianceRules complianceRules;
	private CompliancePolicies compliancePolicies;
	private ComplianceAuditors complianceAuditors;
	private CodeSecurityTemplates codeSecurityTemplates;
	private ConainerSecurityTemplates conainerSecurityTemplates;
	private DataSecurityTemplates dataSecurityTemplates;
}
