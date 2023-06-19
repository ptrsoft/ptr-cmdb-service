package com.synectiks.asset.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synectiks.asset.api.controller.QueryApi;
import com.synectiks.asset.api.model.EnvironmentCountQueryDTO;
import com.synectiks.asset.api.model.EnvironmentQueryDTO;
import com.synectiks.asset.domain.query.EnvironmentCountQueryObj;
import com.synectiks.asset.domain.query.EnvironmentQueryObj;
import com.synectiks.asset.mapper.query.EnvironmentCountQueryMapper;
import com.synectiks.asset.mapper.query.EnvironmentQueryMapper;
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
	public ResponseEntity<List<EnvironmentQueryDTO>> getResourceSummaryByOrg(Long orgId) {
		logger.debug("REST request to get list of landing zone and its associated product-enclaves, products, app and data services for a given organization. Org id: {} ", orgId);
		List<EnvironmentQueryObj> environmentQueryObjList = queryService.getEnvironmentSummary(orgId);
		List<EnvironmentQueryDTO> environmentQueryDTOList = EnvironmentQueryMapper.INSTANCE.toDtoList(environmentQueryObjList);
		return ResponseEntity.ok(environmentQueryDTOList);
	}

	@Override
	public ResponseEntity<List<EnvironmentQueryDTO>> getResourceSummaryByOrgAndCloud(Long orgId, String cloud) {
		logger.debug("REST request to get list of landing zone and its associated product-enclaves, products, app and data services for a given organization. Org id: {} ", orgId);
		List<EnvironmentQueryObj> environmentQueryObjList = queryService.getEnvironmentSummary(orgId, cloud);
		List<EnvironmentQueryDTO> environmentQueryDTOList = EnvironmentQueryMapper.INSTANCE.toDtoList(environmentQueryObjList);
		return ResponseEntity.ok(environmentQueryDTOList);
	}
	
	@Override
	public ResponseEntity<List<String>> getOrgWiseProducts(Long orgId) {
		logger.debug("REST request to Get organization wise products for an organization: Org Id: {}", orgId);
		List<String> organizationProductsQueryObjList = queryService.getOrganizationProducts(orgId);
//		List<EnvironmentCountQueryDTO> environmentCountQueryDTOList = EnvironmentCountQueryMapper.INSTANCE.toDtoList(environmentCountQueryObjList);
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
//		List<EnvironmentCountQueryDTO> environmentCountQueryDTOList = ServicesQueryMapper.INSTANCE.toDtoList(organizationServicesQueryObjList);
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
}