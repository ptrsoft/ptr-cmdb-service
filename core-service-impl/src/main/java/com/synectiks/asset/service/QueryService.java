package com.synectiks.asset.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.synectiks.asset.api.model.BillingDTO;
import com.synectiks.asset.api.model.EnvironmentQueryDTO;
import com.synectiks.asset.api.model.EnvironmentSummaryQueryDTO;
import com.synectiks.asset.domain.query.*;
import com.synectiks.asset.mapper.query.EnvironmentQueryMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.repository.QueryRepository;

@Service
public class QueryService {

	private static final Logger logger = LoggerFactory.getLogger(QueryService.class);
	
    @Autowired
    private QueryRepository queryRepository;

	@Autowired
	private EntityManager entityManager;


	public List<EnvironmentCountQueryObj> getEnvStats(Long orgId){
       logger.debug("Getting organization wise account's stats. Org Id: {}", orgId);
       return queryRepository.getEnvStats(orgId);
   }

    public EnvironmentCountQueryObj getEnvStats(Long orgId, String cloud) {
        logger.debug("Getting organization and cloud wise account's stats. Org Id: {}, Cloud: {}", orgId,cloud);
        return queryRepository.getEnvStats(cloud, orgId);
    }

	public EnvironmentCountQueryObj getEnvStats(Long orgId, String cloud, String landingZone) {
		logger.debug("Getting organization, cloud and landing-zone wise account's stats. Org Id: {}, cloud: {}, landing-zone: {}", orgId,cloud, landingZone);
		return queryRepository.getEnvStats(landingZone, cloud, orgId);
	}

    public List<EnvironmentQueryDTO> getEnvironmentSummaryList(Long orgId, Long departmentId, Long productId, String env, String cloud)  {
		StringBuilder primarySql = new StringBuilder(" select distinct sub_query.id, sub_query.cloud, sub_query.landing_zone, sub_query.product_enclave, sub_query.total_product, sub_query.total_product_prod_env from \n" +
				" (select l.id, l.cloud, l.department_id,be.product_id, l.landing_zone, coalesce(count(distinct pe.instance_id),0) as product_enclave, \n" +
				" coalesce(count(distinct be.product_id),0) as total_product,\n" +
				" (select count(distinct be2.product_env_id) from business_element be2 where be2.id = be.id\n" +
				" \t\t\tand be2.product_env_id in (select pe3.id from product_env pe3 where upper(pe3.\"name\") = upper('PROD'))) as total_product_prod_env\n" +
				" from landingzone l\n" +
				" inner join department d on l.department_id = d.id \n" +
				" inner join organization o on d.organization_id = o.id \n" +
				" left join cloud_element ce on ce.landingzone_id = l.id \n" +
				" left join product_enclave pe on pe.landing_zone = l.landing_zone and pe.department_id = d.id\n" +
				" left join business_element be on be.cloud_element_id = ce.id \n" +
				" left join product p on be.product_id = p.id and p.department_id = d.id and p.organization_id = o.id \n" +
				" left join product_env pe2 on pe2.product_id = p.id and be.product_env_id = pe2.id\n" +
				" where o.id = ? \n" +
				" and o.id = d.organization_id \n" +
				" and l.department_id = d.id\n" +
				" group by l.id,l.cloud, l.landing_zone, be.id, l.department_id,be.product_id) as sub_query\n" +
				" where 1 = 1 ");

		if(departmentId != null){
			primarySql.append(" and sub_query.department_id = ? ");
		}
		if(productId != null ){
			primarySql.append(" and sub_query.product_id = ?  ");
		}
		if(!StringUtils.isBlank(cloud)){
			primarySql.append(" and upper(sub_query.cloud) = upper(?) ");
		}
		logger.debug("Environment summary query {}",primarySql);

		Query query = entityManager.createNativeQuery(primarySql.toString(), EnvironmentSummaryQueryObj.class);

		int index = 0;
		query.setParameter(++index, orgId);
		if(departmentId != null) {
			query.setParameter(++index, departmentId);
		}
		if(productId != null){
			query.setParameter(++index, productId);
		}
		if(!StringUtils.isBlank(cloud)){
			query.setParameter(++index, cloud);
		}
		List<EnvironmentSummaryQueryObj> list = query.getResultList();
		return filterEnvironmentSummary(list);
    }

    private List<EnvironmentQueryDTO> filterEnvironmentSummary(List<EnvironmentSummaryQueryObj> list) {
        Set<String> cloudSet = list.stream().map(EnvironmentSummaryQueryObj::getCloud).collect(Collectors.toSet());
        List<EnvironmentQueryObj> environmentQueryObjList = new ArrayList<>();
        for (Object obj: cloudSet){
            String cloudName = (String)obj;
            logger.debug("Getting list for cloud: {}", cloudName);
            List<EnvironmentSummaryQueryObj> filteredList = list.stream().filter(l -> !StringUtils.isBlank(l.getCloud()) && l.getCloud().equalsIgnoreCase(cloudName)).collect(Collectors.toList());
            EnvironmentQueryObj dto = EnvironmentQueryObj.builder()
                    .cloud(cloudName)
                    .environmentSummaryList(filteredList)
                    .build();
            environmentQueryObjList.add(dto);
        }
		List<EnvironmentQueryDTO> environmentQueryDTOList = EnvironmentQueryMapper.INSTANCE.toDtoList(environmentQueryObjList);
		for(EnvironmentQueryDTO environmentQueryDTO: environmentQueryDTOList){
			for(EnvironmentSummaryQueryDTO environmentSummaryQueryDTO: environmentQueryDTO.getEnvironmentSummaryList()){
				BillingDTO billingDTO = new BillingDTO();
				billingDTO.setLandingZone(environmentSummaryQueryDTO.getLandingZone());
				billingDTO.countryCode("US");
				billingDTO.setCurrencyCode("USD");
				billingDTO.setCurrencySymbol("$");
				billingDTO.setTotal("0");
				environmentSummaryQueryDTO.setOverallCost(billingDTO);
			}
		}
		return environmentQueryDTOList;
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

	public InfraTopologyObj getInfraTopology(Long orgId, String landingZone) {
		logger.debug("Getting infra-topology-view for a given organization and landing-zone");
		List<InfraTopology3TierQueryObj> threeTierlist = queryRepository.getInfraTopology3TierView(orgId,landingZone);
		List<InfraTopologySOAQueryObj> soaList = queryRepository.getInfraTopologySOAView(orgId,landingZone);
		return filterInfraTopologyData(threeTierlist, soaList, landingZone);
	}

	private InfraTopologyObj filterInfraTopologyData(List<InfraTopology3TierQueryObj> threeTierlist, List<InfraTopologySOAQueryObj> soaList, String landingZone) {
		Set<String> productEnclave3TierSet = threeTierlist.stream().map(InfraTopology3TierQueryObj::getProductEnclaveId).collect(Collectors.toSet());
		Set<String> productEnclaveSOASet = soaList.stream().map(InfraTopologySOAQueryObj::getProductEnclaveId).collect(Collectors.toSet());
		productEnclave3TierSet.addAll(productEnclaveSOASet);

		List<InfraTopologyProductEnclaveObj> productEnclaveList = new ArrayList<>();

		for (String productEnclave: productEnclave3TierSet){
			InfraTopologyProductEnclaveObj productEnclaveObj = InfraTopologyProductEnclaveObj.builder()
					.id(productEnclave)
					.build();

			List<InfraTopology3TierQueryObj> filtered3TierList = threeTierlist.stream().filter(l -> !StringUtils.isBlank(l.getProductEnclaveId()) && l.getProductEnclaveId().equalsIgnoreCase(productEnclave)).collect(Collectors.toList());
			for(InfraTopology3TierQueryObj threeTierObj :filtered3TierList ){
				ThreeTierQueryObj threeTierQueryObj = ThreeTierQueryObj.builder()
						.productCount(threeTierObj.getProductCount())
						.webCount(threeTierObj.getWebCount())
						.appCount(threeTierObj.getAppCount())
						.dataCount(threeTierObj.getDataCount())
						.auxiliaryCount(threeTierObj.getAuxiliaryCount())
						.build();
				productEnclaveObj.setThreeTier(threeTierQueryObj);
				productEnclaveObj.setName(threeTierObj.getProductEnclaveName());
			}

			List<InfraTopologySOAQueryObj> filteredSOAList = soaList.stream().filter(l -> !StringUtils.isBlank(l.getProductEnclaveId()) && l.getProductEnclaveId().equalsIgnoreCase(productEnclave)).collect(Collectors.toList());
			for(InfraTopologySOAQueryObj soaObj :filteredSOAList ){
				SOAQueryObj soaQueryObj = SOAQueryObj.builder()
						.productCount(soaObj.getProductCount())
						.appCount(soaObj.getAppCount())
						.dataCount(soaObj.getDataCount())
						.otherCount(soaObj.getOtherCount())
						.build();
				productEnclaveObj.setSoa(soaQueryObj);
				productEnclaveObj.setName(soaObj.getProductEnclaveName());
			}
			productEnclaveList.add(productEnclaveObj);
		}

		InfraTopologyObj infraTopologyObj = InfraTopologyObj.builder()
				.landingZone(landingZone)
				.productEnclaveList(productEnclaveList)
				.build();
		return infraTopologyObj;
	}


	public CloudElementSpendAnalyticsQueryObj getSpendTodayAnalytics(Long orgId) {
		logger.debug("Get today's cost spent");
		return queryRepository.spendTodayAnalytics(orgId);
	}

	public CloudElementSpendAnalyticsQueryObj getSpendYesterdayAnalytics(Long orgId) {
		logger.debug("Get yesterday's cost spend ");
		return queryRepository.spendYesterdayAnalytics(orgId);
	}

	public Long getCurrentSpendRateAvePerHour(Long orgId) {
		logger.debug("Get current spend rate average par hour");
		Long perDay = queryRepository.currentSpendRatePerDay(orgId);
		if(perDay != null && perDay != 0){
			perDay = perDay/24;
		}
		return perDay;
	}

	public Long getCurrentSpendRatePerDay(Long orgId) {
		logger.debug("Request to get current spend rate par day sum");
		return queryRepository.currentSpendRatePerDay(orgId);
	}

	public Long getTotalSpendAnalytics(Long orgId) {
		logger.debug("Request to get total spend of an organization");
		return queryRepository.totalSpendAnalytics(orgId);
	}

	public List<CloudElementCloudWiseQueryObj> getCloudWiseTotalSpend(Long orgId) {
		logger.debug("Request to get list of cloud wise spend of an organization");
		return queryRepository.cloudWiseTotalSpend(orgId);
	}

	public List<CloudElementCloudWiseMonthlyQueryObj> getCloudWiseSpendMonthly(Long orgId) {
		logger.debug("Get monthly cloud-wise spend of an organization");
		return queryRepository.cloudWiseMonthlySpend(orgId);
	}

//	public List<InfraTopologySummaryQueryObj> getInfraTopologySummary(Long orgId, String landingZone, String productEnclave)  {
//		logger.debug("Getting list of cloud elements to form infra-topology-view for a given organization and landing-zone");
//		return queryRepository.getInfraTopologySummary(orgId,landingZone,productEnclave);
//	}
	
	public List<MonthlyStatisticsQueryObj> monthlyStatisticsQueryObj(Long orgId)  {
		logger.debug("Getting list of monthly statistics for given organization");
		return queryRepository.monthlyStatisticsQueryObj(orgId);
	}

	public TotalBudgetQueryObj getTotalBudget(Long orgId) {
		logger.debug("Getting total budget of organization");
		return queryRepository.getTotalBudget(orgId);
	}

	public List<CostAnalyticQueryObj> getProductWiseCost(Long orgId) {
		logger.debug("Get product wise cost ");
		return queryRepository.getProductWiseCost(orgId);
	}

	public List<CostAnalyticQueryObj> getProductionVsOthersCost(Long orgId) {
		logger.debug("Get production vs others cost ");
		return queryRepository.getProductionVsOthersCost(orgId);
	}

	public List<CostAnalyticQueryObj> getServiceTypeWiseCost(Long orgId) {
		logger.debug("Get service type wise cost ");
		return queryRepository.getServiceTypeWiseCost(orgId);
	}



	public List<CostBillingQueryObj> getDataGeneratorOrgBilling(Long orgId , String entity ) {
		logger.debug("Get  org  wise billing ");
	 return queryRepository.getDataGeneratorOrgBilling(orgId,entity);
	}

	public List<CostBillingQueryObj> getOrgAndElementNameBilling(Long orgId,String entity, String elementName) {
		logger.debug("Get  org and elementName  wise billing ");
		 return queryRepository.getOrgAndElementNameBilling(orgId,entity,elementName);
	}

	public List<CostBillingQueryObj> getOrgAndLandingZoneBilling(Long orgId, String entity, Long landingZone) {
		logger.debug("Get  org and landingZone  wise billing ");
		 return queryRepository.getOrgAndLandingZoneBilling(orgId,entity,landingZone);
	}

	public List<CostBillingQueryObj> getOrgAndElementNameAndLandingZoneBilling(Long orgId, String entity,
			Long landingZone, String elementName) {
		logger.debug("Get  org and landingZone  wise billing ");
		 return queryRepository.getOrgAndElementNameAndLandingZoneBilling(orgId,entity,landingZone,elementName);
	}

//	public EnvironmentCountQueryObj getResourceCountsByOrgAndCloudAndLandingZone(Long orgId, String cloud,Long landingZone) {
//		  logger.debug("Getting cloud wise landing zone and their resource counts for an organization and given cloud. Org Id: {}, Cloud: {}", orgId,cloud);
//	        return queryRepository.getResourceCountsByOrgAndCloudAndLandingZone(cloud, orgId,landingZone);
//	}

}


