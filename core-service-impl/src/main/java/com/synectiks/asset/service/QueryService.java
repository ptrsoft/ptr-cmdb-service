package com.synectiks.asset.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synectiks.asset.domain.query.EnvironmentCountQueryObj;
import com.synectiks.asset.domain.query.EnvironmentQueryObj;
import com.synectiks.asset.domain.query.EnvironmentSummaryQueryObj;
import com.synectiks.asset.repository.QueryRepository;

@Service
public class QueryService {

	private static final Logger logger = LoggerFactory.getLogger(QueryService.class);
    @Autowired
    private QueryRepository queryRepository;

   public List<EnvironmentCountQueryObj> getEnvironmentCounts(Long orgId){
       logger.debug("Getting cloud wise landing zone and their resource counts for an organization. Org Id: {}", orgId);
       return queryRepository.getCount(orgId);
   }

    public EnvironmentCountQueryObj getEnvironmentCounts(Long orgId, String cloud) {
        logger.debug("Getting cloud wise landing zone and their resource counts for an organization and given cloud. Org Id: {}, Cloud: {}", orgId,cloud);
        return queryRepository.getCount(cloud, orgId);
    }

    public List<EnvironmentQueryObj> getEnvironmentSummary(Long orgId)  {
        logger.debug("Getting organization wise environment summary. Org id: {}", orgId);
        List<EnvironmentSummaryQueryObj> list = queryRepository.getEnvironmentSummary(orgId);
        return filterEnvironmentSummary(list);
    }

    public List<EnvironmentQueryObj> getEnvironmentSummary(Long orgId, String cloud)  {
        logger.debug("Getting organization and cloud wise environment summary. Org Id: {}, Cloud: {}", orgId,cloud);
        List<EnvironmentSummaryQueryObj> list = queryRepository.getEnvironmentSummary(orgId, cloud);
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

	


}


