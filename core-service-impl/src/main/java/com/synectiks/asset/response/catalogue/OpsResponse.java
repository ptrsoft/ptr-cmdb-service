package com.synectiks.asset.response.catalogue;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpsResponse implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private List<CloudDashboard> cloudDashBoards;
  private List<Datasource> dataSources;
  private List<ProvisioningTemplates> provisioningTemplates;
  private AlertRules slertRules;
  private Workflows workflows;
  private Collectors collectors;
  private Diagonostics diagonostics;
  private KubeOperators kubeOperators;
}
