 package com.synectiks.asset.controller;

 import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synectiks.asset.api.controller.QueryApi;
import com.synectiks.asset.api.model.CloudElementCloudWiseAnalyticsDTO;
import com.synectiks.asset.api.model.CloudElementCloudWiseMonthlyAnalyticsDTO;
import com.synectiks.asset.api.model.CloudElementCurrentsDTO;
import com.synectiks.asset.api.model.CloudElementSpendAnalyticsDTO;
import com.synectiks.asset.api.model.CloudElementVpcDTO;
import com.synectiks.asset.api.model.EnvironmentCountQueryDTO;
import com.synectiks.asset.api.model.EnvironmentQueryDTO;
import com.synectiks.asset.api.model.InfraTopologyDTO;
import com.synectiks.asset.api.model.InfraTopologySummaryDTO;
import com.synectiks.asset.domain.query.CloudElementCloudWiseMonthlyQueryObj;
import com.synectiks.asset.domain.query.CloudElementCloudWiseQueryObj;
import com.synectiks.asset.domain.query.CloudElementCurrentQueryObj;
import com.synectiks.asset.domain.query.CloudElementSpendAnalyticsQueryObj;
import com.synectiks.asset.domain.query.CloudEnvironmentVpcQueryObj;
import com.synectiks.asset.domain.query.EnvironmentCountQueryObj;
import com.synectiks.asset.domain.query.EnvironmentQueryObj;
import com.synectiks.asset.domain.query.InfraTopologyObj;
import com.synectiks.asset.domain.query.InfraTopologySummaryQueryObj;
import com.synectiks.asset.mapper.query.CloudElementCloudWiseMonthlyQueryMapper;
import com.synectiks.asset.mapper.query.CloudElementCloudWiseQueryMapper;
import com.synectiks.asset.mapper.query.CloudElementCurrentQueryMapper;
import com.synectiks.asset.mapper.query.CloudElementSpendAnalyticQueryMapper;
import com.synectiks.asset.mapper.query.CloudEnvironmentVpcQueryMapper;
import com.synectiks.asset.mapper.query.EnvironmentCountQueryMapper;
import com.synectiks.asset.mapper.query.EnvironmentQueryMapper;
import com.synectiks.asset.mapper.query.InfraTopologyMapper;
import com.synectiks.asset.mapper.query.InfraTopologySummeryMapper;
import com.synectiks.asset.service.QueryService;


@RestController
@RequestMapping("/api")
public class QueryController implements QueryApi {

	private final Logger logger = LoggerFactory.getLogger(QueryController.class);

	@Autowired
	private QueryService queryService;

	@Override
	public ResponseEntity<List<EnvironmentCountQueryDTO>> getResourceCountsByOrg(Long orgId) {
		logger.debug("REST request to Get cloud wise landing zone and their resource counts for an organization: Org Id: {}", orgId);
		List<EnvironmentCountQueryObj> environmentCountQueryObjList = queryService.getEnvironmentCounts(orgId);
		List<EnvironmentCountQueryDTO> environmentCountQueryDTOList = EnvironmentCountQueryMapper.INSTANCE.toDtoList(environmentCountQueryObjList);
		return ResponseEntity.ok(environmentCountQueryDTOList);
	}

	@Override
	public ResponseEntity<EnvironmentCountQueryDTO> getResourceCountsByOrgAndCloud(Long orgId, String cloud) {
		logger.debug("REST request to Get cloud wise landing zone and their resource counts for an organization and cloud: Org Id: {}, Cloud: {}", orgId, cloud);
		EnvironmentCountQueryObj environmentCountQueryObj = queryService.getEnvironmentCounts(orgId, cloud);
		EnvironmentCountQueryDTO environmentCountQueryDTOList = EnvironmentCountQueryMapper.INSTANCE.toDto(environmentCountQueryObj);
		return ResponseEntity.ok(environmentCountQueryDTOList);
	}

	@Override
	public ResponseEntity<List<EnvironmentQueryDTO>> getResourceSummaryByOrg(Long orgId, Long departmentId, String product, String env) {
		logger.debug("REST request to get list of landing zone and its associated product-enclaves, products, app and data services for a given organization. Org id: {} ", orgId);
		List<EnvironmentQueryObj> environmentQueryObjList = queryService.getEnvironmentSummaryByFilter(orgId, departmentId, product, env, null);
		List<EnvironmentQueryDTO> environmentQueryDTOList = EnvironmentQueryMapper.INSTANCE.toDtoList(environmentQueryObjList);
		return ResponseEntity.ok(environmentQueryDTOList);
	}

	@Override
	public ResponseEntity<List<EnvironmentQueryDTO>> getResourceSummaryByOrgAndCloud(Long orgId, String cloud) {
		logger.debug("REST request to get list of landing zone and its associated product-enclaves, products, app and data services for a given organization and cloud. Org id: {}, Cloud: {} ", orgId,cloud);
		List<EnvironmentQueryObj> environmentQueryObjList = queryService.getEnvironmentSummaryByFilter(orgId, null, null, null, cloud);
		List<EnvironmentQueryDTO> environmentQueryDTOList = EnvironmentQueryMapper.INSTANCE.toDtoList(environmentQueryObjList);
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
	
	@Override
	public ResponseEntity<InfraTopologyDTO> getInfraTopology(Long orgId, String landingZone) {
		try{
			InfraTopologyObj infraTopologyObj = queryService.getInfraTopology(orgId, landingZone);
			InfraTopologyDTO infraTopologyDTO = InfraTopologyMapper.INSTANCE.toDto(infraTopologyObj);
			return ResponseEntity.ok(infraTopologyDTO);
		}catch (Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	@Override
	public ResponseEntity<CloudElementSpendAnalyticsDTO> allSpendTodayAnalytics(Long orgId) {
		logger.debug("REST request to get today's cost spent");
		CloudElementSpendAnalyticsQueryObj environmentQueryObj = queryService.allSpendTodayAnalytics(orgId);
	    CloudElementSpendAnalyticsDTO dtoList = CloudElementSpendAnalyticQueryMapper.INSTANCE.toDto(environmentQueryObj);
		return ResponseEntity.ok(dtoList);
	}
	
	@Override
	public ResponseEntity<CloudElementSpendAnalyticsDTO> allSpendYesterdaySpendAnalytics(Long orgId) {
		logger.debug("REST request to get yesterday's spend ");
		CloudElementSpendAnalyticsQueryObj environmentQueryObj = queryService.allSpendYesterdaySpendAnalytics(orgId);
	    CloudElementSpendAnalyticsDTO dtoList = CloudElementSpendAnalyticQueryMapper.INSTANCE.toDto(environmentQueryObj);
		return ResponseEntity.ok(dtoList);
	}
	@Override
	public ResponseEntity<Long> currentSpendRateAvePerHour(Long orgId) {
		logger.debug("REST request to get current spend rate average par hour");
		Long aveSpendRatePerHour = queryService.currentSpendRateAvePerHour(orgId);
		return ResponseEntity.ok(aveSpendRatePerHour);
	}
	@Override
	public ResponseEntity<Long> currentSpendRatePerDay(Long orgId) {
		logger.debug("REST request to get current spend rate par day");
		Long currentSpendRatePerDay = queryService.currentSpendRatePerDay(orgId);
		return ResponseEntity.ok(currentSpendRatePerDay);
	}
	
	@Override
	public ResponseEntity<List<String>> cloudWiseAnalytics(Long orgId) {
		logger.debug(
				"REST request to get list of  cloud wise  rate");
		List<String> listOfToday = queryService.cloudWiseAnalytics(orgId);
		return ResponseEntity.ok(listOfToday);
	}
	
	@Override
	public ResponseEntity<List<CloudElementCloudWiseAnalyticsDTO>> spendTotal(Long orgId) {
		logger.debug("REST request to Get organization and cloudName wise cloud-cost for an organization: Org Id: {}", orgId);
		List<CloudElementCloudWiseQueryObj> environmentQueryObjList = queryService.spendTotal(orgId);
	    List<CloudElementCloudWiseAnalyticsDTO> dtoList = CloudElementCloudWiseQueryMapper.INSTANCE.toDtoList(environmentQueryObjList);
		return ResponseEntity.ok(dtoList);
	}
	
	@Override
	public ResponseEntity<List<CloudElementCloudWiseMonthlyAnalyticsDTO>> eachMonthTotal(Long orgId) {
		logger.debug("REST request to Get organization a wise cloud-cost for an organization: Org Id: {}", orgId);
		List<CloudElementCloudWiseMonthlyQueryObj> environmentQueryObjList = queryService.eachMonthTotal(orgId);
	    List<CloudElementCloudWiseMonthlyAnalyticsDTO> dtoList = CloudElementCloudWiseMonthlyQueryMapper.INSTANCE.toDtoList(environmentQueryObjList);
		return ResponseEntity.ok(dtoList);
	}
	
	@Override
	public ResponseEntity<List<InfraTopologySummaryDTO>> getInfraTopologySummary(Long orgId, String landingZone, String productEnclave) {
		try{
			List<InfraTopologySummaryQueryObj> infraTopologySummaryQueryObj = queryService.getInfraTopologySummary(orgId, landingZone,productEnclave);
			List<InfraTopologySummaryDTO> infraTopologyDTO = InfraTopologySummeryMapper.INSTANCE.toDtoList(infraTopologySummaryQueryObj);
			return ResponseEntity.ok(infraTopologyDTO);
		}catch (Exception e){
			e.printStackTrace();
			return ResponseEntity.badRequest().body(null);
		}
	}
}

