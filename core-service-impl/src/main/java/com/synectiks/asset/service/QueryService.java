package com.synectiks.asset.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.domain.query.CloudElementCloudWiseMonthlyQueryObj;
import com.synectiks.asset.domain.query.CloudElementCloudWiseQueryObj;
import com.synectiks.asset.domain.query.CloudElementCurrentQueryObj;
import com.synectiks.asset.domain.query.CloudElementSpendAnalyticsQueryObj;
import com.synectiks.asset.domain.query.CloudEnvironmentVpcQueryObj;
import com.synectiks.asset.domain.query.EnvironmentCountQueryObj;
import com.synectiks.asset.domain.query.EnvironmentQueryObj;
import com.synectiks.asset.domain.query.EnvironmentSummaryQueryObj;
import com.synectiks.asset.domain.query.InfraTopologyCategoryObj;
import com.synectiks.asset.domain.query.InfraTopologyElementObj;
import com.synectiks.asset.domain.query.InfraTopologyHostingTypeObj;
import com.synectiks.asset.domain.query.InfraTopologyObj;
import com.synectiks.asset.domain.query.InfraTopologyProductEnclaveObj;
import com.synectiks.asset.domain.query.InfraTopologyQueryObj;
import com.synectiks.asset.domain.query.InfraTopologySummaryQueryObj;
import com.synectiks.asset.repository.QueryRepository;

@Service
public class QueryService {

	private static final Logger logger = LoggerFactory.getLogger(QueryService.class);
	
    @Autowired
    private QueryRepository queryRepository;

	@Autowired
	private EntityManager entityManager;


	public List<EnvironmentCountQueryObj> getEnvironmentCounts(Long orgId){
       logger.debug("Getting cloud wise landing zone and their resource counts for an organization. Org Id: {}", orgId);
       return queryRepository.getCount(orgId);
   }

    public EnvironmentCountQueryObj getEnvironmentCounts(Long orgId, String cloud) {
        logger.debug("Getting cloud wise landing zone and their resource counts for an organization and given cloud. Org Id: {}, Cloud: {}", orgId,cloud);
        return queryRepository.getCount(cloud, orgId);
    }

    public List<EnvironmentQueryObj> getEnvironmentSummaryByFilter(Long orgId, Long departmentId, String product, String env, String cloud)  {
		String sql ="select ROW_NUMBER() OVER () AS id, cnv.cloud, replace(cast(ceo.landing_zone as text), '\"', '') as landing_zone, " +
				"count(ceo.product_enclave) as product_enclave, " +
				"(select count(c.obj -> 'associatedProduct')  " +
				" from cloud_element ce2, jsonb_array_elements(ce2.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj, pos) " +
				" where ce2.hardware_location -> 'landingZone' = ceo.landing_zone " +
				" ) as product, " +
				"(select count(c.obj -> 'associatedService')  " +
				" from cloud_element ce2, jsonb_array_elements(ce2.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj, pos) " +
				" where ce2.hardware_location -> 'landingZone' = ceo.landing_zone and upper(cast(c.obj -> 'serviceType' as text)) = '\"APP\"' " +
				" ) as app_service, " +
				"(select count(c.obj -> 'associatedService')  " +
				" from cloud_element ce2, jsonb_array_elements(ce2.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj, pos) " +
				" where ce2.hardware_location -> 'landingZone' = ceo.landing_zone and upper(cast(c.obj -> 'serviceType' as text)) = '\"DATA\"' " +
				" ) as data_service  " +
				"from (select ce.cloud_environment_id, ce.hardware_location -> 'landingZone' as landing_zone, " +
				" c2.obj -> 'associatedProduct' as associated_product, c2.obj -> 'associatedEnv' as associated_env, " +
				" count(ce.hardware_location -> 'productEnclave') as product_enclave " +
				" from  cloud_element ce, jsonb_array_elements(ce.hosted_services -> 'HOSTEDSERVICES') with ordinality c2(obj, pos) " +
				" group by ce.cloud_environment_id, ce.hardware_location -> 'landingZone', " +
				" c2.obj -> 'associatedProduct', c2.obj -> 'associatedEnv'" +
				" ) as ceo, " +
				"cloud_environment cnv, department dep, organization org " +
				"where (ceo.cloud_environment_id = cnv.id ) " +
				"and (cnv.department_id = dep.id) " +
				"and (dep.organization_id = org.id) " +
				"and org.id = ? " ;
		if(departmentId != null){
			sql = sql + "and dep.id = ? ";
		}
		if(!StringUtils.isBlank(product)){
			sql = sql + "and upper(replace(cast(ceo.associated_product as text), '\"', '')) = upper(?) ";
		}
		if(!StringUtils.isBlank(env)){
			sql = sql + "and upper(replace(cast(ceo.associated_env as text), '\"', '')) = upper(?) ";
		}
		if(!StringUtils.isBlank(cloud)){
			sql = sql + "and upper(cnv.cloud) = upper(?) ";
		}
		sql = sql + "group by cnv.cloud, ceo.landing_zone, ceo.product_enclave " ;

		Query query = entityManager.createNativeQuery(sql, EnvironmentSummaryQueryObj.class);
		logger.debug("Environment summary query {}",sql);

		int index = 0;
		query.setParameter(++index, orgId);
		if(departmentId != null){
			query.setParameter(++index, departmentId);
		}
		if(!StringUtils.isBlank(product)){
			query.setParameter(++index, product);
		}
		if(!StringUtils.isBlank(env)){
			query.setParameter(++index, env);
		}
		if(!StringUtils.isBlank(cloud)){
			query.setParameter(++index, cloud);
		}

		List<EnvironmentSummaryQueryObj> list = query.getResultList();
		return filterEnvironmentSummary(list);
    }

    private List<EnvironmentQueryObj> filterEnvironmentSummary(List<EnvironmentSummaryQueryObj> list) {
        Set<String> cloudSet = list.stream().map(EnvironmentSummaryQueryObj::getCloud).collect(Collectors.toSet());
        List<EnvironmentQueryObj> environmentDtoList = new ArrayList<>();
        for (Object obj: cloudSet){
            String cloudName = (String)obj;
            logger.debug("Getting list for cloud: {}", cloudName);
            List<EnvironmentSummaryQueryObj> filteredList = list.stream().filter(l -> !StringUtils.isBlank(l.getCloud()) && l.getCloud().equalsIgnoreCase(cloudName)).collect(Collectors.toList());
            EnvironmentQueryObj dto = EnvironmentQueryObj.builder()
                    .cloud(cloudName)
                    .environmentSummaryList(filteredList)
                    .build();
            environmentDtoList.add(dto);
        }
        return environmentDtoList;
    }

	public List<String> getOrganizationProducts(Long orgId) {
		 logger.debug("Getting organization wise product list for an organization. Org Id: {}", orgId);
	     return queryRepository.getProduct(orgId);
	}

	public List<String> getOrgWiseLandingZone(Long orgId) {
		// TODO Auto-generated method stub
		 logger.debug("Getting organization wise landing-zone list for an organization. Org Id: {}", orgId);
	     return queryRepository.getOrgLandingZone(orgId);
	}

	public List<String> getOrgWiseProductEnclave(Long orgId) {
		logger.debug("Getting organization wise product-enclave list for an organization. Org Id: {}", orgId);
	     return queryRepository.getOrgWiseProductEnclave(orgId);
	}

	public List<Object> getOrgWiseServices(Long orgId) {
		logger.debug("Getting organization wise services list for an organization. Org Id: {}", orgId);
	     return queryRepository.getOrgWiseServices(orgId);
	}

	public List<String> getOrgDepProductWiseServices(Long orgId, Long depId) {
		logger.debug("Getting organization and deparment wise services list for an organization. Org Id: {}", orgId,depId);
	     return queryRepository.getOrgDepProductWiseServices(orgId,depId);
	}

	public List<String> getOrgDepLandingZoneWiseServices(Long orgId, Long depId) {
		logger.debug("Request to get list of landing zone of an Department an Organization");
    	return queryRepository.getDepartmentLandingZones(orgId,depId);
	}

	public List<String> getOrgDepProductEncWiseServices(Long orgId, Long depId) {
		logger.debug("Request to get list of product enclaves of an Department an Organization");
    	return queryRepository.getOrganizationDepartmentsProductEnclave(orgId,depId);
	}

	public List<Object> getOrgDepServicesWiseServices(Long orgId, Long depId) {
		logger.debug("Request to get list of services of an department an Organization");
		return queryRepository.getOrganizationDepartmentsMicroServices(orgId, depId);
	}

	
	public List<String> getOrgProductServices(Long orgId, String product) {

		logger.debug("Request to get list of services  of an Organization an Products");
			return queryRepository.getOrgProductServices(orgId, product);
		}

	public List<String> getOrgServiceTypeServices(Long orgId, String serviceType) {
		logger.debug("Request to get list of services  of an Organization an serviceType");
			return queryRepository.getOrganizationServiceTypeMicroServices(orgId,serviceType);
	}

	public List<Object> getOrgServiceCostServices(Long orgId, String serviceName) {
		logger.debug("Request to get list of services-cost of an serviceName  an Organization");
		return queryRepository.getOrganizationServiceNameMicroServices(orgId,serviceName);
	}

	public List<Object> getOrgServiceDailyCostServices(Long orgId, String serviceName) {
		logger.debug("Request to get list of services-cost-daily of an serviceName  an Organization");
		return queryRepository.getOrgServiceDailyCostServices(orgId,serviceName);
	}

	public List<Object> getOrgServiceWeeklyCostServices(Long orgId, String serviceName) {
		logger.debug("Request to get list of services-cost-weekly of an serviceName  an Organization");
		return queryRepository.getOrganizationServiceNameWeeklyMicroServices(orgId,serviceName);
	}

	public List<Object> getOrgServiceMonthlyCostServices(Long orgId, String serviceName) {
		logger.debug("Request to get list of services-cost-monthly of an serviceName  an Organization");
		return queryRepository.getOrgServiceMonthlyCostServices(orgId,serviceName);
	}

	public List<String> getOrgLandingZoneServices(Long orgId, String landingZone) {
		logger.debug("Request to get list of services of  an landingZone  an Organization");
		return queryRepository.getOrgLandingZoneServices(orgId,landingZone);
	}

	public List<String> getOrgLandingZoneMicroServices(Long orgId, String landingZone) {
		logger.debug("Request to get list of services of  an landingZone  an Organization");
		return queryRepository.getOrgLandingZoneMicroServices(orgId,landingZone);
	}

	public List<Object> getOrgServiceSlaServices(Long orgId, String name) {
		logger.debug("Request to get list of services-sla of an serviceName  an Organization");
		return queryRepository.getOrgServiceSlaServices(orgId,name);
	}

	public List<Object> getOrgServiceCureentSlaServices(Long orgId, String serviceName) {
		logger.debug("Request to get list of services-cureent-sla of an serviceName  an Organization");
		return queryRepository.getOrganizationServiceCurrentSlaMicroServices(orgId,serviceName);
	}

	public List<Object> getOrgServiceWeeklySlaServices(Long orgId, String serviceName) {
		logger.debug("Request to get list of services-weekly-sla of an serviceName  an Organization");
		return queryRepository.getOrgServiceWeeklySlaServices(orgId,serviceName);
	}

	public List<Object> getOrgServiceMonthlySlaServices(Long orgId, String serviceName) {
		logger.debug("Request to get list of services-monthly-sla of an serviceName  an Organization");
		return queryRepository.getOrgServiceMonthlySlaServices(orgId,serviceName);
	}

	public List<String> getOrgEnvServices(Long orgId, Long env) {
		logger.debug("Request to get list of services  of an Organization an Env");
		return queryRepository.getOrganizationEnvMicroServices(orgId, env);
	}

	public List<Object> getOrgProductEnvServices(Long orgId, String product, Long env) {
		logger.debug("Request to get list of services  of an Organization an product an Env");
			return queryRepository.getOrgProductEnvServices(orgId,product ,env);
	}

	public List<String> getOrgDepProductServices(Long orgId, Long depId, String product) {
		logger.debug("Request to get list of services of an department an product an Organization");
			return queryRepository.getOrganizationDepartmentsProductMicroServices(orgId, depId,product);	}

	public List<String> getOrgDepEnvironmentServices(Long orgId, Long depId, Long env) {
		logger.debug("Request to get list of services of an department  an env an Organization");
			return queryRepository.getOrganizationDepartmentsEnvMicroServices(orgId, depId,env);
	}

	public List<Object> getOrgDepProductEnvironmentServices(Long orgId, Long depId,String product, Long env) {
		logger.debug("Request to get list of services of an department an product an env an Organization");
			return queryRepository.getOrganizationDepartmentsProductEnvMicroServices(orgId,depId,product,env);
	}

	public List<String> getOrgDepServices(Long orgId, Long depId, String serviceType) {
		logger.debug("Request to get list of services of an department an serviceType  an Organization");
			return queryRepository.getOrganizationDepartmentsServiceTypeMicroServices(orgId,depId,serviceType);
	}

	public List<Object> getOrgDepServicesCost(Long orgId, Long depId, String serviceName) {
		logger.debug("Request to get list of services-cost of an depId an serviceName  an Organization");
		return queryRepository.getOrganizationDepartmentsServiceNameMicroServices(orgId,depId,serviceName);
	}

	public List<Object> getOrgDepServicesDailyCost(Long orgId, Long depId, String serviceName) {
		logger.debug("Request to get list of services-cost-daily of an depId an serviceName  an Organization");
		return queryRepository.getOrgDepServicesDailyCost(orgId,depId,serviceName);
	}

	public List<Object> getOrgDepServicesWeeklyCost(Long orgId, Long depId, String serviceName) {
		logger.debug("Request to get list of services-cost-weekly of an depId an serviceName  an Organization");
		return queryRepository.getOrgDepServicesWeeklyCost(orgId,depId,serviceName);
	}

	public List<Object> getOrgDepServicesMonthlyCost(Long orgId, Long depId, String serviceName) {
		logger.debug("Request to get list of services-cost-monthly of an depId an serviceName  an Organization");
		return queryRepository.getOrgDepServicesMonthlyCost(orgId,depId,serviceName);
	}

	public List<String> getOrgDepLandingZoneService(Long orgId, Long depId, String landingZone) {
		logger.debug("Request to get list of services of an department an landingZone  an Organization");
		return queryRepository.getOrgDepLandingZoneService(orgId,depId,landingZone);
	}

	public List<String> getOrgDepProductsService(Long orgId, Long depId, String landingZone) {
		logger.debug("Request to get list of services of an department an landingZone  an Organization");
		return queryRepository.getOrgDepProductsService(orgId,depId,landingZone);
	}

	public List<Object> getOrgDepServiceSla(Long orgId, Long depId, String name) {
		logger.debug("Request to get list of services-sla of an serviceName an department  an Organization");
		return queryRepository.getOrgDepServiceSla(orgId,depId,name);
	}

	public List<Object> getOrgDepServiceCureentSla(Long orgId, Long depId, String serviceName) {
		logger.debug("Request to get list of services-cureent-sla of an serviceName an department  an Organization");
		return queryRepository.getOrgDepServiceCureentSla(orgId,depId,serviceName);
	}

	public List<Object> getOrgDepServiceWeeklySla(Long orgId, Long depId, String serviceName) {
		logger.debug("Request to get list of services-weekly-sla of an serviceName an department  an Organization");
		return queryRepository.getOrgDepServiceWeeklySla(orgId,depId,serviceName);
	}

	public List<Object> getOrgDepServiceMonthlySla(Long orgId, Long depId, String serviceName) {
		logger.debug("Request to get list of services-monthly-sla of an serviceName an department  an Organization");
		return queryRepository.getOrgDepServiceMonthlySla(orgId,depId,serviceName);
	}

	public List<String> orgDepLandingZoneProductEnclave(Long orgId, Long depId, String landingZone) {
		logger.debug("Request to get list of product enclaves of landingZoneName an Department an Organization");
    	return queryRepository.getOrganizationDepartmentLandingzoneProductEnclave(orgId,depId,landingZone);
	}

	public List<String> orgLandingZoneProductEnclave(Long orgId, String landingZone) {
		logger.debug("Request to get list of product enclaves of landing-zone and organization");
    	return queryRepository.orgLandingZoneProductEnclave(orgId,landingZone);
	}

	public List<CloudEnvironmentVpcQueryObj> orgVpcSummary(Long orgId, String landingZone, String product) {
		logger.debug("Request to get list of vpc for given organization, landing-zone and product");
    	return queryRepository.orgVpcSummary(orgId,landingZone,product);
	}

	public InfraTopologyObj getInfraTopology(Long orgId, String landingZone) throws JsonProcessingException {
		logger.debug("Getting list of cloud elements to form infra-topology-view for a given organization and landing-zone");
		List<InfraTopologyQueryObj> list = queryRepository.getInfraTopology(orgId,landingZone);
		return filterInfraTopologyData(list, landingZone);
	}

	private InfraTopologyObj filterInfraTopologyData(List<InfraTopologyQueryObj> list, String landingZone) throws JsonProcessingException {
		ObjectMapper objectMapper = Constants.instantiateMapper();
		Set<String> productEnclaveSet = list.stream().map(InfraTopologyQueryObj::getProductEnclave).collect(Collectors.toSet());

		List<InfraTopologyProductEnclaveObj> productEnclaveList = new ArrayList<>();
		for (String productEnclave: productEnclaveSet){
			List<InfraTopologyHostingTypeObj> hostingTypeList = new ArrayList<>();
			List<InfraTopologyQueryObj> filteredProductEnclaveList = list.stream().filter(l -> !StringUtils.isBlank(l.getProductEnclave()) && l.getProductEnclave().equalsIgnoreCase(productEnclave)).collect(Collectors.toList());
			Set<String> hostingTypeSet = filteredProductEnclaveList.stream().map(InfraTopologyQueryObj::getHostingType).collect(Collectors.toSet());

			for(String hostingType: hostingTypeSet){
				List<InfraTopologyCategoryObj> categoryList = new ArrayList<>();
				List<InfraTopologyQueryObj> filteredCategoryList = filteredProductEnclaveList.stream().filter(l -> !StringUtils.isBlank(l.getHostingType()) && l.getHostingType().equalsIgnoreCase(hostingType)).collect(Collectors.toList());

				for(InfraTopologyQueryObj catObj: filteredCategoryList){
					List<InfraTopologyElementObj> elementList = new ArrayList<>();
					JsonNode rootNode = objectMapper.readTree(catObj.getElementList());

					if(rootNode != null && rootNode.isArray()){
						Iterator<JsonNode> iterator = rootNode.iterator();
						while (iterator.hasNext()) {
							logger.debug("Creating element object");
							JsonNode jsonNode = iterator.next();
							InfraTopologyElementObj elementObj = InfraTopologyElementObj.builder()
									.arn(jsonNode.get("arn").asText())
									.name(jsonNode.get("name").asText())
									.build();
							elementList.add(elementObj);
						}
					}

					InfraTopologyCategoryObj categoryObj = InfraTopologyCategoryObj.builder()
							.category(catObj.getCategory())
							.elementType(catObj.getElementType())
							.elementList(elementList)
							.build();
					categoryList.add(categoryObj);
				}
				InfraTopologyHostingTypeObj hostingTypeObj = InfraTopologyHostingTypeObj.builder()
						.hostingType(hostingType)
						.category(categoryList)
						.build();
				hostingTypeList.add(hostingTypeObj);
			}
			InfraTopologyProductEnclaveObj productEnclaveObj = InfraTopologyProductEnclaveObj.builder()
					.name(productEnclave)
					.hostingTypeList(hostingTypeList)
					.build();
			productEnclaveList.add(productEnclaveObj);
		}

		InfraTopologyObj infraTopologyObj = InfraTopologyObj.builder()
				.landingZone(landingZone)
				.productEnclaveList(productEnclaveList)
				.build();
		return infraTopologyObj;
	}


	public CloudElementSpendAnalyticsQueryObj allSpendTodayAnalytics(Long orgId) {
		logger.debug("Get today's cost spent");
		return queryRepository.allSpendTodayAnalytics(orgId);
	}

	public CloudElementSpendAnalyticsQueryObj allSpendYesterdaySpendAnalytics(Long orgId) {
		logger.debug("Get yesterday's spend ");
		return queryRepository.allSpendYesterdaySpendAnalytics(orgId);
	}

	public Long currentSpendRateAvePerHour(Long orgId) {
		logger.debug("Get current spend rate average par hour");
		Long perDay = queryRepository.currentSpendRatePerDay(orgId);
		if(perDay != null && perDay != 0){
			perDay = perDay/24;
		}
		return perDay;
	}

	public Long currentSpendRatePerDay(Long orgId) {
		logger.debug("Request to get current spend rate par day sum");
		return queryRepository.currentSpendRatePerDay(orgId);
	}

	public List<String> cloudWiseAnalytics(Long orgId) {
		logger.debug("Request to get list of total spend  sum");
		return queryRepository.cloudWiseAnalytics(orgId);
	}

	public List<CloudElementCloudWiseQueryObj> spendTotal(Long orgId) {
		logger.debug("Request to get list of total spend  sum");
		return queryRepository.spendTotal(orgId);
	}

	public List<CloudElementCloudWiseMonthlyQueryObj> eachMonthTotal(Long orgId) {
		logger.debug("Request to get list of total spend  sum");
		return queryRepository.eachMonthTotal(orgId);
	}

	public List<InfraTopologySummaryQueryObj> getInfraTopologySummary(Long orgId, String landingZone, String productEnclave)  {
		logger.debug("Getting list of cloud elements to form infra-topology-view for a given organization and landing-zone");
		
		return queryRepository.getInfraTopologySummary(orgId,landingZone,productEnclave);

	}
	
//	private InfraTopologySummaryQueryObj filterInfraTopologyData1(List<InfraTopologySummaryQueryObj> list, String landingZone,
//			String productEnclave) {
//	
//		ObjectMapper objectMapper = Constants.instantiateMapper();
//		Set<String> productEnclaveSet = list.stream().map(InfraTopologySummaryQueryObj::getHostingType).collect(Collectors.toSet());

//		List<InfraTopologyProductEnclaveObj> productEnclaveList = new ArrayList<>();
//		for (String productEnclave1: productEnclaveSet){
//			List<InfraTopologyHostingTypeObj> hostingTypeList = new ArrayList<>();
//			List<InfraTopologyQueryObj> filteredProductEnclaveList = list.stream().filter(l -> !StringUtils.isBlank(l.getProductEnclave()) && l.getProductEnclave().equalsIgnoreCase(productEnclave1)).collect(Collectors.toList());
//			Set<String> hostingTypeSet = filteredProductEnclaveList.stream().map(InfraTopologyQueryObj::getHostingType).collect(Collectors.toSet());
//
//			for(String hostingType: hostingTypeSet){
//				List<InfraTopologyCategoryObj> categoryList = new ArrayList<>();
//				List<InfraTopologyQueryObj> filteredCategoryList = filteredProductEnclaveList.stream().filter(l -> !StringUtils.isBlank(l.getHostingType()) && l.getHostingType().equalsIgnoreCase(hostingType)).collect(Collectors.toList());
//
//				for(InfraTopologyQueryObj catObj: filteredCategoryList){
//					List<InfraTopologyElementObj> elementList = new ArrayList<>();
//					JsonNode rootNode = objectMapper.readTree(catObj.getElementList());
//
//					if(rootNode != null && rootNode.isArray()){
//						Iterator<JsonNode> iterator = rootNode.iterator();
//						while (iterator.hasNext()) {
//							logger.debug("Creating element object");
//							JsonNode jsonNode = iterator.next();
//							InfraTopologyElementObj elementObj = InfraTopologyElementObj.builder()
//									.arn(jsonNode.get("arn").asText())
//									.name(jsonNode.get("name").asText())
//									.build();
//							elementList.add(elementObj);
//						}
//					}
//
//					InfraTopologyCategoryObj categoryObj = InfraTopologyCategoryObj.builder()
//							.category(catObj.getCategory())
//							.elementType(catObj.getElementType())
//							.elementList(elementList)
//							.build();
//					categoryList.add(categoryObj);
//				}
//				InfraTopologyHostingTypeObj hostingTypeObj = InfraTopologyHostingTypeObj.builder()
//						.hostingType(hostingType)
//						.category(categoryList)
//						.build();
//				hostingTypeList.add(hostingTypeObj);
//			}
//			InfraTopologyProductEnclaveObj productEnclaveObj = InfraTopologyProductEnclaveObj.builder()
//					.name(productEnclave1)
//					.hostingTypeList(hostingTypeList)
//					.build();
//			productEnclaveList.add(productEnclaveObj);
//		}

//		InfraTopologySummaryQueryObj infraTopologyObj = InfraTopologyObj.builder()
//				.landingZone(landingZone)
//				.productEnclaveList(productEnclaveList)
//				.build();
//		return infraTopologyObj;
//	}

//	private InfraTopologyObj filterInfraTopologyData1(List<InfraTopologyQueryObj> list, String landingZone,String productEnclave) throws JsonProcessingException {
//		ObjectMapper objectMapper = Constants.instantiateMapper();
//		Set<String> productEnclaveSet = list.stream().map(InfraTopologyQueryObj::getProductEnclave).collect(Collectors.toSet());
//
//		List<InfraTopologyProductEnclaveObj> productEnclaveList = new ArrayList<>();
//		for (String productEnclave1: productEnclaveSet){
//			List<InfraTopologyHostingTypeObj> hostingTypeList = new ArrayList<>();
//			List<InfraTopologyQueryObj> filteredProductEnclaveList = list.stream().filter(l -> !StringUtils.isBlank(l.getProductEnclave()) && l.getProductEnclave().equalsIgnoreCase(productEnclave1)).collect(Collectors.toList());
//			Set<String> hostingTypeSet = filteredProductEnclaveList.stream().map(InfraTopologyQueryObj::getHostingType).collect(Collectors.toSet());
//
//			for(String hostingType: hostingTypeSet){
//				List<InfraTopologyCategoryObj> categoryList = new ArrayList<>();
//				List<InfraTopologyQueryObj> filteredCategoryList = filteredProductEnclaveList.stream().filter(l -> !StringUtils.isBlank(l.getHostingType()) && l.getHostingType().equalsIgnoreCase(hostingType)).collect(Collectors.toList());
//
//				for(InfraTopologyQueryObj catObj: filteredCategoryList){
//					List<InfraTopologyElementObj> elementList = new ArrayList<>();
//					JsonNode rootNode = objectMapper.readTree(catObj.getElementList());
//
//					if(rootNode != null && rootNode.isArray()){
//						Iterator<JsonNode> iterator = rootNode.iterator();
//						while (iterator.hasNext()) {
//							logger.debug("Creating element object");
//							JsonNode jsonNode = iterator.next();
//							InfraTopologyElementObj elementObj = InfraTopologyElementObj.builder()
//									.arn(jsonNode.get("arn").asText())
//									.name(jsonNode.get("name").asText())
//									.build();
//							elementList.add(elementObj);
//						}
//					}
//
//					InfraTopologyCategoryObj categoryObj = InfraTopologyCategoryObj.builder()
//							.category(catObj.getCategory())
//							.elementType(catObj.getElementType())
//							.elementList(elementList)
//							.build();
//					categoryList.add(categoryObj);
//				}
//				InfraTopologyHostingTypeObj hostingTypeObj = InfraTopologyHostingTypeObj.builder()
//						.hostingType(hostingType)
//						.category(categoryList)
//						.build();
//				hostingTypeList.add(hostingTypeObj);
//			}
//			InfraTopologyProductEnclaveObj productEnclaveObj = InfraTopologyProductEnclaveObj.builder()
//					.name(productEnclave1)
//					.hostingTypeList(hostingTypeList)
//					.build();
//			productEnclaveList.add(productEnclaveObj);
//		}
//
//		InfraTopologyObj infraTopologyObj = InfraTopologyObj.builder()
//				.landingZone(landingZone)
//				.productEnclaveList(productEnclaveList)
//				.build();
//		return infraTopologyObj;
//	}

}


