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
import com.synectiks.asset.api.model.MicroServiceDTO;
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
	
//	@Override
//	public ResponseEntity<List<MicroServiceDTO>> getOrgWiseServices(Long orgId) {
//		logger.debug("REST request to Get organization wise services for an organization: Org Id: {}", orgId);
//		List<MicroServiceDTO> organizationServicesQueryObjList = queryService.getOrgWiseServices(orgId);
//		List<EnvironmentCountQueryDTO> environmentCountQueryDTOList = ServicesQueryMapper.INSTANCE.toDtoList(organizationServicesQueryObjList);
//		return ResponseEntity.ok(organizationServicesQueryObjList);
//	}

}