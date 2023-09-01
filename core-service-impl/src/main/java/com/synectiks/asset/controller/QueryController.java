 package com.synectiks.asset.controller;

 import com.synectiks.asset.api.controller.QueryApi;
 import com.synectiks.asset.api.model.*;
 import com.synectiks.asset.domain.query.*;
 import com.synectiks.asset.mapper.query.*;
 import com.synectiks.asset.service.QueryService;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RestController;

 import java.util.List;


@RestController
@RequestMapping("/api")
public class QueryController implements QueryApi {

	private final Logger logger = LoggerFactory.getLogger(QueryController.class);

	@Autowired
	private QueryService queryService;

	@Override
	public ResponseEntity<List<EnvironmentCountQueryDTO>> getEnvStatsByOrg(Long orgId) {
		logger.debug("REST request to get organization wise account's stats: Org Id: {}", orgId);
		List<EnvironmentCountQueryObj> environmentCountQueryObjList = queryService.getEnvStats(orgId);
		List<EnvironmentCountQueryDTO> environmentCountQueryDTOList = EnvironmentCountQueryMapper.INSTANCE.toDtoList(environmentCountQueryObjList);
		return ResponseEntity.ok(environmentCountQueryDTOList);
	}

	@Override
	public ResponseEntity<EnvironmentCountQueryDTO> getEnvStatsByOrgCloud(Long orgId, String cloud) {
		logger.debug("REST request to get organization and cloud wise account's stats. Org Id: {}, Cloud: {}", orgId, cloud);
		EnvironmentCountQueryObj environmentCountQueryObj = queryService.getEnvStats(orgId, cloud);
		EnvironmentCountQueryDTO environmentCountQueryDTOList = EnvironmentCountQueryMapper.INSTANCE.toDto(environmentCountQueryObj);
		return ResponseEntity.ok(environmentCountQueryDTOList);
	}

	@Override
	public ResponseEntity<EnvironmentCountQueryDTO> getEnvStatsByOrgCloudLandingZone(Long orgId, String cloud, String landingZone) {
		logger.debug("REST request to get organization, cloud and landing-zone wise account's stats. Org Id: {}, Cloud: {}, LandingZone {}", orgId, cloud, landingZone);
		EnvironmentCountQueryObj environmentCountQueryObj = queryService.getEnvStats(orgId, cloud, landingZone);
		EnvironmentCountQueryDTO environmentCountQueryDTOList = EnvironmentCountQueryMapper.INSTANCE.toDto(environmentCountQueryObj);
		return ResponseEntity.ok(environmentCountQueryDTOList);
	}

	@Override
	public ResponseEntity<List<EnvironmentQueryDTO>> getEnvironmentSummaryList(Long orgId, Long departmentId, Long productId, String env, String cloud) {
		logger.debug("REST request to get environment summary list for a given organization. Org id: {} ", orgId);
		List<EnvironmentQueryDTO> environmentQueryDTOList = queryService.getEnvironmentSummaryList(orgId, departmentId, productId, env, cloud);
		return ResponseEntity.ok(environmentQueryDTOList);
	}

	@Override
	public ResponseEntity<List<String>> getOrgWiseProducts(Long orgId) {
		logger.debug("REST request to Get organization wise products for an organization: Org Id: {}", orgId);
		List<String> organizationProductsQueryObjList = queryService.getOrganizationProducts(orgId);
		return ResponseEntity.ok(organizationProductsQueryObjList);
	}
	
	@Override
	public ResponseEntity<List<String>> getOrgWiseLandingZone(Long orgId) {
		logger.debug("REST request to Get organization wise landing-zone for an organization: Org Id: {}", orgId);
		List<String> organizationLandingZoneQueryObjList = queryService.getOrgWiseLandingZone(orgId);
		return ResponseEntity.ok(organizationLandingZoneQueryObjList);
	}
	
	@Override
	public ResponseEntity<List<String>> getOrgWiseProductEnclave(Long orgId) {
		logger.debug("REST request to Get organization wise product enclaves for an organization: Org Id: {}", orgId);
		List<String> organizationProductEnclaveQueryObjList = queryService.getOrgWiseProductEnclave(orgId);
		return ResponseEntity.ok(organizationProductEnclaveQueryObjList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgWiseServices(Long orgId) {
		logger.debug("REST request to Get organization wise services for an organization: Org Id: {}", orgId);
		List<Object> organizationServicesQueryObjList = queryService.getOrgWiseServices(orgId);
		return ResponseEntity.ok(organizationServicesQueryObjList);
	}

	@Override
	public ResponseEntity<List<String>> getOrgDepProductWiseServices(Long orgId,Long depId) {
		logger.debug("REST request to Get organization and department wise product  for an organization: Org Id: {}", orgId);
		List<String> orgaDepObjList = queryService.getOrgDepProductWiseServices(orgId,depId);
		return ResponseEntity.ok(orgaDepObjList);
	}
	
	@Override
	public ResponseEntity<List<String>> getOrgDepLandingZoneWiseServices(Long orgId,Long depId) {
		logger.debug("REST request to Get organization and department wise landing zone for an organization: Org Id: {}", orgId);
		List<String> orgaDepObjList = queryService.getOrgDepLandingZoneWiseServices(orgId,depId);
		return ResponseEntity.ok(orgaDepObjList);
	}
	
	@Override
	public ResponseEntity<List<String>> getOrgDepProductEncWiseServices(Long orgId,Long depId) {
		logger.debug("REST request to Get organization and department wise product- enclaves for an organization: Org Id: {}", orgId);
		List<String> orgaDepObjList = queryService.getOrgDepProductEncWiseServices(orgId,depId);
		return ResponseEntity.ok(orgaDepObjList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgDepServicesWiseServices(Long orgId,Long depId) {
		logger.debug("REST request to Get organization and department wise services for an organization: Org Id: {}", orgId);
		List<Object> orgaDepObjList = queryService.getOrgDepServicesWiseServices(orgId,depId);
		return ResponseEntity.ok(orgaDepObjList);
	}
	
	@Override
	public ResponseEntity<List<String>> getOrgProductServices(Long orgId,String product) {
		logger.debug("REST request to Get organization and product wise services for an organization: Org Id: {}", orgId);
		List<String> microServiceList = queryService.getOrgProductServices(orgId,product);
       return ResponseEntity.ok(microServiceList);
	}
	
	@Override
	public ResponseEntity<List<String>> getOrgEnvServices(Long orgId,Long env) {
		logger.debug("REST request to Get organization and env wise services for an organization: Org Id: {}", orgId,env);
		List<String> microServiceList = queryService.getOrgEnvServices(orgId,env);
       return ResponseEntity.ok(microServiceList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgProductEnvServices(Long orgId, String product,Long env) {
		logger.debug("REST request to Get organization and product and env wise services for an organization: Org Id: {}", orgId,product,env);
		List<Object> microServiceList = queryService.getOrgProductEnvServices(orgId,product,env);
       return ResponseEntity.ok(microServiceList);
	}
	
	@Override
	public ResponseEntity<List<String>> getOrgServiceTypeServices(Long orgId,String serviceType) {
		logger.debug("REST request to Get organization and service name wise services for an organization: Org Id: {}", orgId,serviceType);
		List<String> microServiceList = queryService.getOrgServiceTypeServices(orgId,serviceType);
       return ResponseEntity.ok(microServiceList);
	}
	
	
	@Override
	public ResponseEntity<List<Object>> getOrgServiceCostServices(Long orgId,String serviceName) {
		logger.debug("REST request to Get organization and serviceName wise services-cost for an organization: Org Id: {}", orgId,serviceName);
		List<Object> microServiceList = queryService.getOrgServiceCostServices(orgId,serviceName);
       return ResponseEntity.ok(microServiceList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgServiceDailyCostServices(Long orgId,String serviceName) {
		logger.debug("REST request to Get organization and serviceName wise services-daily-cost for an organization: Org Id: {}", orgId,serviceName);
		List<Object> microServiceList = queryService.getOrgServiceDailyCostServices(orgId,serviceName);
       return ResponseEntity.ok(microServiceList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgServiceWeeklyCostServices(Long orgId,String serviceName) {
		logger.debug("REST request to Get organization and serviceName wise services-wekekly-cost for an organization: Org Id: {}", orgId,serviceName);
		List<Object> microServiceList = queryService.getOrgServiceWeeklyCostServices(orgId,serviceName);
       return ResponseEntity.ok(microServiceList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgServiceMonthlyCostServices(Long orgId,String serviceName) {
		logger.debug("REST request to Get organization and serviceName wise services-monthly-cost for an organization: Org Id: {}", orgId,serviceName);
		List<Object> microServiceList = queryService.getOrgServiceMonthlyCostServices(orgId,serviceName);
       return ResponseEntity.ok(microServiceList);
	}
	
	@Override
	public ResponseEntity<List<String>> getOrgLandingZoneServices(Long orgId,String landingZone) {
		logger.debug("REST request to Get organization and landingZone wise  serivces for an organization: Org Id: {}", orgId,landingZone);
		List<String> microServiceList = queryService.getOrgLandingZoneServices(orgId,landingZone);
       return ResponseEntity.ok(microServiceList);
	}
	
	@Override
	public ResponseEntity<List<String>> getOrgLandingZoneMicroServices(Long orgId,String landingZone) {
		logger.debug("REST request to Get organization and landingZone wise  produicts for an organization: Org Id: {}", orgId,landingZone);
		List<String> microServiceList = queryService.getOrgLandingZoneMicroServices(orgId,landingZone);
       return ResponseEntity.ok(microServiceList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgServiceSlaServices(Long orgId,String name) {
		logger.debug("REST request to Get organization and service name wise  service-sla for an organization: Org Id: {}", orgId,name);
		List<Object> microServiceList = queryService.getOrgServiceSlaServices(orgId,name);
       return ResponseEntity.ok(microServiceList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgServiceCureentSlaServices(Long orgId,String serviceName) {
		logger.debug("REST request to Get organization and service name wise  service-cureent-sla for an organization: Org Id: {}", orgId,serviceName);
		List<Object> microServiceList = queryService.getOrgServiceCureentSlaServices(orgId,serviceName);
       return ResponseEntity.ok(microServiceList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgServiceWeeklySlaServices(Long orgId,String serviceName) {
		logger.debug("REST request to Get organization and service name wise  service-weekly-sla for an organization: Org Id: {}", orgId,serviceName);
		List<Object> microServiceList = queryService.getOrgServiceWeeklySlaServices(orgId,serviceName);
       return ResponseEntity.ok(microServiceList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgServiceMonthlySlaServices(Long orgId,String serviceName) {
		logger.debug("REST request to Get organization and service name wise  service-monthly-sla for an organization: Org Id: {}", orgId,serviceName);
		List<Object> microServiceList = queryService.getOrgServiceMonthlySlaServices(orgId,serviceName);
       return ResponseEntity.ok(microServiceList);
	}
	
	@Override
	public ResponseEntity<List<String>> getOrgDepProductServices(Long orgId,Long depId,String product) {
		logger.debug("REST request to Get organization and department and product wise services   for an organization: Org Id: {}", orgId);
		List<String> orgaDepObjList = queryService.getOrgDepProductServices(orgId,depId,product);
		return ResponseEntity.ok(orgaDepObjList);
	}
	
	@Override
	public ResponseEntity<List<String>> getOrgDepEnvironmentServices(Long orgId,Long depId,Long env) {
		logger.debug("REST request to Get organization and department and env wise services   for an organization: Org Id: {}", orgId,depId,env);
		List<String> orgaDepEnvObj = queryService.getOrgDepEnvironmentServices(orgId,depId,env);
		return ResponseEntity.ok(orgaDepEnvObj);
	}
	
	@Override
	public ResponseEntity<List<String>> getOrgDepServices(Long orgId,Long depId,String serviceType) {
		logger.debug("REST request to Get organization and department and service name and  wise services for an organization: Org Id: {}", orgId,depId,serviceType);
		List<String> orgaDepServiceObjList = queryService.getOrgDepServices(orgId,depId,serviceType);
		return ResponseEntity.ok(orgaDepServiceObjList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgDepServicesCost(Long orgId,Long depId,String serviceName) {
		logger.debug("REST request to Get organization and department and service name and  wise services-cost for an organization: Org Id: {}", orgId,depId,serviceName);
		List<Object> orgaDepServiceCostObjList = queryService.getOrgDepServicesCost(orgId,depId,serviceName);
		return ResponseEntity.ok(orgaDepServiceCostObjList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgDepServicesDailyCost(Long orgId,Long depId,String serviceName) {
		logger.debug("REST request to Get organization and department and service name and  wise services-daliy-cost for an organization: Org Id: {}", orgId,depId,serviceName);
		List<Object> orgaDepDaliyCostObjList = queryService.getOrgDepServicesDailyCost(orgId,depId,serviceName);
		return ResponseEntity.ok(orgaDepDaliyCostObjList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgDepServicesWeeklyCost(Long orgId,Long depId,String serviceName) {
		logger.debug("REST request to Get organization and department and service name and  wise services-weekly-cost for an organization: Org Id: {}", orgId,depId,serviceName);
		List<Object> orgaDepWeeklyObjList = queryService.getOrgDepServicesWeeklyCost(orgId,depId,serviceName);
		return ResponseEntity.ok(orgaDepWeeklyObjList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgDepServicesMonthlyCost(Long orgId,Long depId,String serviceName) {
		logger.debug("REST request to Get organization and department and service name and  wise services-monthly-cost for an organization: Org Id: {}", orgId,depId,serviceName);
		List<Object> orgaDepMonthlyObjList = queryService.getOrgDepServicesMonthlyCost(orgId,depId,serviceName);
		return ResponseEntity.ok(orgaDepMonthlyObjList);
	}
	
	@Override
	public ResponseEntity<List<String>> getOrgDepLandingZoneService(Long orgId,Long depId,String landingZone) {
		logger.debug("REST request to Get organization and department and service name and landingZone wise services for an organization: Org Id: {}", orgId,depId,landingZone);
		List<String> orgaDepLandingZoneObjList = queryService.getOrgDepLandingZoneService(orgId,depId,landingZone);
		return ResponseEntity.ok(orgaDepLandingZoneObjList);
	}
	
	@Override
	public ResponseEntity<List<String>> getOrgDepProductsService(Long orgId,Long depId,String landingZone) {
		logger.debug("REST request to Get organization and department and service name and landingZone wise services for an organization: Org Id: {}", orgId,depId,landingZone);
		List<String> orgaDepLandingZoneObjList = queryService.getOrgDepProductsService(orgId,depId,landingZone);
		return ResponseEntity.ok(orgaDepLandingZoneObjList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgDepServiceSla(Long orgId,Long depId,String name) {
		logger.debug("REST request to Get organization and department and service name  wise services-sla for an organization: Org Id: {}", orgId,depId,name);
		List<Object> orgaDepSlaObjList = queryService.getOrgDepServiceSla(orgId,depId,name);
		return ResponseEntity.ok(orgaDepSlaObjList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgDepServiceCureentSla(Long orgId,Long depId,String serviceName) {
		logger.debug("REST request to Get organization and department and service name  wise services-cureent-sla for an organization: Org Id: {}", orgId,depId,serviceName);
		List<Object> orgaDepSlaObjList = queryService.getOrgDepServiceCureentSla(orgId,depId,serviceName);
		return ResponseEntity.ok(orgaDepSlaObjList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgDepServiceWeeklySla(Long orgId,Long depId,String serviceName) {
		logger.debug("REST request to Get organization and department and service name  wise services-weekly-sla for an organization: Org Id: {}", orgId,depId,serviceName);
		List<Object> orgaDepSlaObjList = queryService.getOrgDepServiceWeeklySla(orgId,depId,serviceName);
		return ResponseEntity.ok(orgaDepSlaObjList);
	}
	
	@Override
	public ResponseEntity<List<Object>> getOrgDepServiceMonthlySla(Long orgId,Long depId,String serviceName) {
		logger.debug("REST request to Get organization and department and service name  wise services-monthly-sla for an organization: Org Id: {}", orgId,depId,serviceName);
		List<Object> orgaDepSlaObjList = queryService.getOrgDepServiceMonthlySla(orgId,depId,serviceName);
		return ResponseEntity.ok(orgaDepSlaObjList);
	}
	
	@Override
	public ResponseEntity<List<String>> orgLandingZoneProductEnclave(Long orgId,String landingZone) {
		logger.debug(
				"REST request to get list of  product-enclaveof given Organization  and landingZone. Organization id :{}",
				orgId, landingZone);
		List<String> orgaDepSlaObjList = queryService.orgLandingZoneProductEnclave(orgId,landingZone);
		return ResponseEntity.ok(orgaDepSlaObjList);
	}
	
	@Override
	public ResponseEntity<List<String>> orgDepLandingZoneProductEnclave(Long orgId,Long depId,String landingZone) {
		logger.debug(
				"REST request to get list of  product-enclaveof given Organization and departments and landingZone. Organization id :{}",
				orgId, depId, landingZone);
		List<String> orgaDepSlaObjList = queryService.orgDepLandingZoneProductEnclave(orgId,depId,landingZone);
		return ResponseEntity.ok(orgaDepSlaObjList);
	}
	
	@Override
	public ResponseEntity<List<CloudElementVpcDTO>> orgVpcSummary(Long orgId,String landingZone,String product) {
		logger.debug(
				"REST request to get list of  vpc of given Organization  and landingZone and product. Organization id :{}",
				orgId, product, landingZone);
		List<CloudEnvironmentVpcQueryObj> environmentQueryObjList = queryService.orgVpcSummary(orgId,landingZone,product);
	    List<CloudElementVpcDTO> dtoList = CloudEnvironmentVpcQueryMapper.INSTANCE.toDtoList(environmentQueryObjList);
		return ResponseEntity.ok(dtoList);
	}
	
//	@Override
//	public ResponseEntity<InfraTopologyDTO> getInfraTopology(Long orgId, String landingZone) {
//		logger.debug("Rest request to get infra-topology of an organization and landing zone. Org id: {}, Landing-zone: {}", orgId, landingZone);
//		InfraTopologyObj infraTopologyObj = queryService.getInfraTopology(orgId, landingZone);
//		InfraTopologyDTO infraTopologyDTO = InfraTopologyMapper.INSTANCE.toDto(infraTopologyObj);
//		return ResponseEntity.ok(infraTopologyDTO);
//	}

	@Override
	public ResponseEntity<InfraTopologyDTO> getInfraTopologyByLandingZoneId(Long orgId, Long landingZoneId) {
		logger.debug("Rest request to get infra-topology of an organization and landing zone. Org id: {}, Landing-zone-id: {}", orgId, landingZoneId);
		InfraTopologyObj infraTopologyObj = queryService.getInfraTopologyByLandingZoneId(orgId, landingZoneId);
		InfraTopologyDTO infraTopologyDTO = InfraTopologyMapper.INSTANCE.toDto(infraTopologyObj);
		return ResponseEntity.ok(infraTopologyDTO);
	}

	@Override
	public ResponseEntity<List<InfraTopologyCategoryWiseViewDTO>> getInfraTopologyCategoryWiseSummary(Long orgId, String landingZone, String productEnclave) {
		List<InfraTopologyCategoryWiseViewQueryObj> infraTopologyCategoryWiseViewQueryObjList = queryService.getInfraTopologyCategoryWiseView(orgId, landingZone,productEnclave);
		List<InfraTopologyCategoryWiseViewDTO> infraTopologyCategoryWiseViewDTOList = InfraTopologyCategoryWiseViewMapper.INSTANCE.toDtoList(infraTopologyCategoryWiseViewQueryObjList);
		return ResponseEntity.ok(infraTopologyCategoryWiseViewDTOList);
	}

	@Override
	public ResponseEntity<List<InfraTopologyGlobalServiceCategoryWiseViewDTO>> getInfraTopologyGlobalServiceCategoryWiseSummary(Long orgId, Long landingZoneId) {
		List<InfraTopologyGlobalServiceCategoryWiseViewQueryObj> infraTopologyGlobalServiceCategoryWiseViewQueryObjList = queryService.getInfraTopologyGlobalServiceCategoryWiseView(orgId, landingZoneId);
		List<InfraTopologyGlobalServiceCategoryWiseViewDTO> infraTopologyGlobalServiceCategoryWiseViewDTOList = InfraTopologyGlobalServiceCategoryWiseViewMapper.INSTANCE.toDtoList(infraTopologyGlobalServiceCategoryWiseViewQueryObjList);
		return ResponseEntity.ok(infraTopologyGlobalServiceCategoryWiseViewDTOList);
	}

	@Override
	public ResponseEntity<List<InfraTopologyCloudElementDTO>> getInfraTopologyCloudElementList(Long orgId, String landingZone, String productEnclave) {
		List<InfraTopologyCloudElementQueryObj> cloudElementList = queryService.getInfraTopologyCloudElementList(orgId, landingZone,productEnclave);
		List<InfraTopologyCloudElementDTO> cloudElementDTOList = InfraTopologyCloudElementMapper.INSTANCE.toDtoList(cloudElementList);
		for(InfraTopologyCloudElementDTO dto: cloudElementDTOList){
			InfraTopology3TierStatsQueryObj threeTierQueryObj = queryService.getInfraTopology3TierStats(orgId, landingZone,productEnclave, dto.getInstanceId());
			dto.setThreeTier(InfraTopology3TierStatsMapper.INSTANCE.toDto(threeTierQueryObj));

			InfraTopologySOAStatsQueryObj soaQueryObj = queryService.getInfraTopologySOAStats(orgId, landingZone,productEnclave, dto.getInstanceId());
			dto.setSoa(InfraTopologySOAStatsMapper.INSTANCE.toDto(soaQueryObj));
		}
		return ResponseEntity.ok(cloudElementDTOList);
	}

	@Override
	public ResponseEntity<CloudElementSpendAnalyticsDTO> spendTodayAnalytics(Long orgId) {
		logger.debug("REST request to get today's cost spent");
		CloudElementSpendAnalyticsQueryObj environmentQueryObj = queryService.getSpendTodayAnalytics(orgId);
	    CloudElementSpendAnalyticsDTO dtoList = CloudElementSpendAnalyticQueryMapper.INSTANCE.toDto(environmentQueryObj);
		return ResponseEntity.ok(dtoList);
	}
	
	@Override
	public ResponseEntity<CloudElementSpendAnalyticsDTO> spendYesterdayAnalytics(Long orgId) {
		logger.debug("REST request to get yesterday's spend ");
		CloudElementSpendAnalyticsQueryObj environmentQueryObj = queryService.getSpendYesterdayAnalytics(orgId);
	    CloudElementSpendAnalyticsDTO dtoList = CloudElementSpendAnalyticQueryMapper.INSTANCE.toDto(environmentQueryObj);
		return ResponseEntity.ok(dtoList);
	}
	@Override
	public ResponseEntity<Long> currentSpendRateAvePerHour(Long orgId) {
		logger.debug("REST request to get current spend rate average par hour");
		Long aveSpendRatePerHour = queryService.getCurrentSpendRateAvePerHour(orgId);
		return ResponseEntity.ok(aveSpendRatePerHour);
	}
	@Override
	public ResponseEntity<Long> currentSpendRatePerDay(Long orgId) {
		logger.debug("REST request to get current spend rate par day");
		Long currentSpendRatePerDay = queryService.getCurrentSpendRatePerDay(orgId);
		return ResponseEntity.ok(currentSpendRatePerDay);
	}
	
	@Override
	public ResponseEntity<Long> totalSpendAnalytics(Long orgId) {
		logger.debug("REST request to get total spend of an organization. Org Id: {}", orgId);
		Long totalSpend = queryService.getTotalSpendAnalytics(orgId);
		return ResponseEntity.ok(totalSpend);
	}
	
	@Override
	public ResponseEntity<List<CloudElementCloudWiseAnalyticsDTO>> cloudWiseTotalSpend(Long orgId) {
		logger.debug("REST request to Get total cloud wise spend rate for an organization: Org Id: {}", orgId);
		List<CloudElementCloudWiseQueryObj> environmentQueryObjList = queryService.getCloudWiseTotalSpend(orgId);
	    List<CloudElementCloudWiseAnalyticsDTO> dtoList = CloudElementCloudWiseQueryMapper.INSTANCE.toDtoList(environmentQueryObjList);
		return ResponseEntity.ok(dtoList);
	}
	
	@Override
	public ResponseEntity<List<CloudElementCloudWiseMonthlyAnalyticsDTO>> cloudWiseMonthlySpend(Long orgId) {
		logger.debug("REST request to Get monthly cloud-wise spend rate for an organization: Org Id: {}", orgId);
		List<CloudElementCloudWiseMonthlyQueryObj> environmentQueryObjList = queryService.getCloudWiseSpendMonthly(orgId);
	    List<CloudElementCloudWiseMonthlyAnalyticsDTO> dtoList = CloudElementCloudWiseMonthlyQueryMapper.INSTANCE.toDtoList(environmentQueryObjList);
		return ResponseEntity.ok(dtoList);
	}
	

	@Override
	public ResponseEntity<List<MonthlyStatisticsDTO>> monthlyStatistics(Long orgId) {
		logger.debug("REST request to Get monthly statistics for an organization: Org Id: {}", orgId);
		List<MonthlyStatisticsQueryObj> monthlyStatisticsQueryObj = queryService.monthlyStatisticsQueryObj(orgId);
		List<MonthlyStatisticsDTO> monthlyStatisticsDTOList = MonthlyStatisticyMapper.INSTANCE.toDtoList(monthlyStatisticsQueryObj);
		return ResponseEntity.ok(monthlyStatisticsDTOList);
	}
	
	@Override
	public ResponseEntity<TotalBudgetDTO> getTotalBudget(Long orgId) {
		logger.debug("REST request to Get total budget for an organization: Org Id: {}", orgId);
		TotalBudgetQueryObj totalBudgetQueryObj = queryService.getTotalBudget(orgId);
		TotalBudgetDTO totalBudgetDTOList = TotalBudgetMapper.INSTANCE.toDto(totalBudgetQueryObj);
		return ResponseEntity.ok(totalBudgetDTOList);
	}

	@Override
	public ResponseEntity<List<CostAnalyticDTO>> 	getProductWiseCostNonAassociate(Long orgId) {
		logger.debug("REST request to Get product wise cost non associate for an organization: Org Id: {}", orgId);
		List<CostAnalyticQueryObj> costAnalyticQueryObj = queryService.getProductWiseCostNonAssociate(orgId);
		List<CostAnalyticDTO> costAnalyticDTOList = CostAnalyticMapper.INSTANCE.toDtoList(costAnalyticQueryObj);
		return ResponseEntity.ok(costAnalyticDTOList);
	}
	
	@Override
	public ResponseEntity<List<CostAnalyticDTO>> 	getProductWiseCostAassociate(Long orgId) {
		logger.debug("REST request to Get product wise cost associate for an organization: Org Id: {}", orgId);
		List<CostAnalyticQueryObj> costAnalyticQueryObj = queryService.getProductWiseCostAssociate(orgId);
		List<CostAnalyticDTO> costAnalyticDTOList = CostAnalyticMapper.INSTANCE.toDtoList(costAnalyticQueryObj);
		return ResponseEntity.ok(costAnalyticDTOList);
	}

	@Override
	public ResponseEntity<List<CostAnalyticDTO>> getProductionVsOthersCostNonAssociate(Long orgId) {
		logger.debug("REST request to Get production vs others non associate cost for an organization: Org Id: {}", orgId);
		List<CostAnalyticQueryObj> costAnalyticQueryObj = queryService.getProductionVsOthersCostNonAssociate(orgId);
		List<CostAnalyticDTO> costAnalyticDTOList = CostAnalyticMapper.INSTANCE.toDtoList(costAnalyticQueryObj);
		return ResponseEntity.ok(costAnalyticDTOList);
	}
    
	@Override
	public ResponseEntity<List<CostAnalyticDTO>> getProductionVsOthersCostAssociate(Long orgId) {
		logger.debug("REST request to Get production vs others cost associate for an organization: Org Id: {}", orgId);
		List<CostAnalyticQueryObj> costAnalyticQueryObj = queryService.getProductionVsOthersCostAssociate(orgId);
		List<CostAnalyticDTO> costAnalyticDTOList = CostAnalyticMapper.INSTANCE.toDtoList(costAnalyticQueryObj);
		return ResponseEntity.ok(costAnalyticDTOList);
	} 
	
	@Override
	public ResponseEntity<List<CostAnalyticDTO>> getServiceTypeWiseCostNonAssociate(Long orgId) {
		logger.debug("REST request to Get service type wise cost non associate for an organization: Org Id: {}", orgId);
		List<CostAnalyticQueryObj> costAnalyticQueryObj = queryService.getServiceTypeWiseCostNonAssociate(orgId);
		List<CostAnalyticDTO> costAnalyticDTOList = CostAnalyticMapper.INSTANCE.toDtoList(costAnalyticQueryObj);
		return ResponseEntity.ok(costAnalyticDTOList);
	}
	
	@Override
	public ResponseEntity<List<CostAnalyticDTO>> getServiceTypeWiseCostAssociate(Long orgId) {
		logger.debug("REST request to Get service type wise cost  associate for an organization: Org Id: {}", orgId);
		List<CostAnalyticQueryObj> costAnalyticQueryObj = queryService.getServiceTypeWiseCostAssociate(orgId);
		List<CostAnalyticDTO> costAnalyticDTOList = CostAnalyticMapper.INSTANCE.toDtoList(costAnalyticQueryObj);
		return ResponseEntity.ok(costAnalyticDTOList);
	}
	
	@Override
	public ResponseEntity<List<SlaAnalyticDTO>> getSlaWiseCostNonAssociate(Long orgId) {
		logger.debug("REST request to Get sla non associate an organization: Org Id: {}", orgId);
		List<SlaAnalyticQueryObj> slaWiseCostQueryObj = queryService.getSlaWiseCostNonAssociate(orgId);
		List<SlaAnalyticDTO> slaAnalyticDTOList = SlaAnalyticMapper.INSTANCE.toDtoList(slaWiseCostQueryObj);
		return ResponseEntity.ok(slaAnalyticDTOList);
	} 
	
	@Override
	public ResponseEntity<List<SlaAnalyticDTO>> getSlaWiseCostAssociate(Long orgId) {
		logger.debug("REST request to Get sla  associate an organization: Org Id: {}", orgId);
		List<SlaAnalyticQueryObj> slaWiseCostQueryObj = queryService.getSlaWiseCostAssociate(orgId);
		List<SlaAnalyticDTO> slaAnalyticDTOList = SlaAnalyticMapper.INSTANCE.toDtoList(slaWiseCostQueryObj);
		return ResponseEntity.ok(slaAnalyticDTOList);
	} 
	
	@Override
	public ResponseEntity<List<CostBillingDTO>> dataGeneratorOrgBilling(Long orgId,String entity ){
		logger.debug("REST request to Get billing for an organization: Org Id: {}", orgId);
		List<CostBillingQueryObj> costBillingQueryObj = queryService.getDataGeneratorOrgBilling(orgId,entity);
		List<CostBillingDTO> billingAnalyticDTOList = CostBillingMapper.INSTANCE.toDtoList(costBillingQueryObj);
		return ResponseEntity.ok(billingAnalyticDTOList);
		
	}
	@Override
	public ResponseEntity<List<CostBillingDTO>> orgAndElementNameBilling(Long orgId ,String entity,String elementName){
		logger.debug("REST request to Get billing for an elementName an  organization: Org Id: {}", orgId);
		List<CostBillingQueryObj> costSummaryQueryObj = queryService.getOrgAndElementNameBilling(orgId,entity,elementName);
		List<CostBillingDTO> costAnalyticDTOList = CostBillingMapper.INSTANCE.toDtoList(costSummaryQueryObj);
		return ResponseEntity.ok(costAnalyticDTOList);
		
	}
	
	@Override
	public ResponseEntity<List<CostBillingDTO>> orgAndLandingZoneBilling(Long orgId ,Long landingZone,String entity){
		logger.debug("REST request to Get billing for an landingZone an  organization: Org Id: {}", orgId);
		List<CostBillingQueryObj> costSummaryQueryObj = queryService.getOrgAndLandingZoneBilling(orgId,entity,landingZone);
		List<CostBillingDTO> costAnalyticDTOList = CostBillingMapper.INSTANCE.toDtoList(costSummaryQueryObj);
		return ResponseEntity.ok(costAnalyticDTOList);
		
	}
	
	@Override
	public ResponseEntity<List<CostBillingDTO>> orgAndElementNameAndLandingZoneBilling(Long orgId ,String elementName ,Long landingZone,String entity){
		logger.debug("REST request to Get billing for an landingZone an element an  organization: Org Id: {}", orgId);
		List<CostBillingQueryObj> costSummaryQueryObj = queryService.getOrgAndElementNameAndLandingZoneBilling(orgId,entity,landingZone,elementName);
		List<CostBillingDTO> costAnalyticDTOList = CostBillingMapper.INSTANCE.toDtoList(costSummaryQueryObj);
		return ResponseEntity.ok(costAnalyticDTOList);
		
	}

}

