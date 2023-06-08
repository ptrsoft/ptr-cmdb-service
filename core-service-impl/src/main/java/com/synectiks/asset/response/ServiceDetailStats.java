package com.synectiks.asset.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDetailStats implements Serializable {

  private static final long serialVersionUID = 1L;

  private String totalCostSoFar;
  private String lastDayCost;
  private String lastWeekCost;
  private String lastMonthCost;
  
  public static ServiceDetailStats from(Integer totalCostSoFar, Integer lastDayCost, Integer lastWeekCost, Integer lastMonthCost) {
	  return ServiceDetailStats.builder()
			  .totalCostSoFar(String.valueOf(totalCostSoFar))
			  .lastDayCost(String.valueOf(lastDayCost))
			  .lastWeekCost(String.valueOf(lastWeekCost))
			  .lastMonthCost(String.valueOf(lastMonthCost))
			  .build();
  }
  
  public static ServiceDetailStats fromString(String totalCostSoFar, String lastDayCost, String lastWeekCost, String lastMonthCost) {
	  return ServiceDetailStats.builder()
			  .totalCostSoFar(totalCostSoFar)
			  .lastDayCost(lastDayCost)
			  .lastWeekCost(lastWeekCost)
			  .lastMonthCost(lastMonthCost)
			  .build();
  }
}
