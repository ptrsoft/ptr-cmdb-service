package com.synectiks.asset.service;

import com.synectiks.asset.api.model.BillingDTO;
import com.synectiks.asset.api.model.EnvironmentQueryDTO;
import com.synectiks.asset.api.model.EnvironmentSummaryQueryDTO;
import com.synectiks.asset.config.Constants;
import com.synectiks.asset.config.ReportingQueryConstants;
import com.synectiks.asset.domain.BusinessElement;
import com.synectiks.asset.domain.Landingzone;
import com.synectiks.asset.domain.query.*;
import com.synectiks.asset.domain.reporting.*;
import com.synectiks.asset.mapper.query.EnvironmentQueryMapper;
import com.synectiks.asset.repository.QueryRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QueryService {

	private static final Logger logger = LoggerFactory.getLogger(QueryService.class);

	@Autowired
	private QueryRepository queryRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private LandingzoneService landingzoneService;

	@Autowired
	private BusinessElementService businessElementService;

	public List<EnvironmentCountQueryObj> getEnvStats(Long orgId) {
		logger.debug("Getting organization wise account's stats. Org Id: {}", orgId);
		return queryRepository.getEnvStats(orgId);
	}

	public EnvironmentCountQueryObj getEnvStats(Long orgId, String cloud) {
		logger.debug("Getting organization and cloud wise account's stats. Org Id: {}, Cloud: {}", orgId, cloud);
		return queryRepository.getEnvStats(cloud, orgId);
	}

	public EnvironmentCountQueryObj getEnvStats(Long orgId, String cloud, String landingZone) {
		logger.debug(
				"Getting organization, cloud and landing-zone wise account's stats. Org Id: {}, cloud: {}, landing-zone: {}",
				orgId, cloud, landingZone);
		return queryRepository.getEnvStats(landingZone, cloud, orgId);
	}

	public List<EnvironmentQueryDTO> getEnvironmentSummaryList(Long orgId, Long departmentId, Long productId,
			String env, String cloud) {
		String primarySql = "select\n" + "subq1.id, \n" + "subq1.cloud,\n" + "subq1.landing_zone,\n"
				+ "subq1.product_enclave_count as product_enclave,\n" + "subq2.product_count as total_product,\n"
				+ "subq3.env_product_count as total_product_prod_env\n" + "FROM\n" + "(\n"
				+ "select l.id, l.cloud, l.landing_zone,COUNT(DISTINCT pe.instance_id) AS product_enclave_count\n"
				+ "from landingzone l\n" + "inner join department d on l.department_id = d.id\n"
				+ "inner join organization o on d.organization_id = o.id\n"
				+ "left join cloud_element ce on ce.landingzone_id = l.id\n"
				+ "left join product_enclave pe on pe.landingzone_id = l.id and pe.department_id = d.id\n"
				+ "left join business_element be on be.cloud_element_id = ce.id\n"
				+ "left join product p on be.product_id = p.id and p.department_id = d.id and p.organization_id = o.id\n"
				+ "where o.id = ?\n" + "and o.id = d.organization_id \n" + " ##CONDITION## " + " \n" + "GROUP by\n"
				+ "l.id,\n" + "l.cloud,\n" + "l.landing_zone\n" + ") AS subq1\n" + "JOIN\n" + "(\n"
				+ "select l.cloud, l.landing_zone,\n" + "coalesce(count(distinct be.product_id),0) as product_count\n"
				+ "from landingzone l\n" + "inner join department d on l.department_id = d.id\n"
				+ "inner join organization o on d.organization_id = o.id\n"
				+ "left join cloud_element ce on ce.landingzone_id = l.id\n"
				+ "left join product_enclave pe on pe.landingzone_id = l.id and pe.department_id = d.id\n"
				+ "left join business_element be on be.cloud_element_id = ce.id\n"
				+ "left join product p on be.product_id = p.id and p.department_id = d.id and p.organization_id = o.id\n"
				+ "left join product_env pe2 on pe2.product_id = p.id and be.product_env_id = pe2.id\n"
				+ "where o.id = ?\n" + "and o.id = d.organization_id\n" + "and l.department_id = d.id\n"
				+ "group by l.cloud, l.landing_zone\n" + ") AS subq2\n"
				+ "ON subq1.cloud = subq2.cloud AND subq1.landing_zone = subq2.landing_zone\n" + "JOIN\n"
				+ "(select distinct l.cloud, l.landing_zone,\n"
				+ "coalesce(count(distinct be.product_env_id),0) as env_product_count\n" + "from product_env pe \n"
				+ "left join product p on p.id = pe.product_id \n"
				+ "inner join department d on p.department_id = d.id\n"
				+ "inner join organization o on d.organization_id = o.id\n"
				+ "left join landingzone l on l.department_id = d.id \n"
				+ "left join cloud_element ce on ce.landingzone_id = l.id\n"
				+ "left join product_enclave penc on penc.landingzone_id = l.id and penc.department_id = d.id\n"
				+ "left join business_element be on be.cloud_element_id = ce.id and be.product_env_id = pe.id \n"
				+ "where o.id = ?\n" + "and o.id = d.organization_id\n"
				+ "and l.department_id = d.id and upper(pe.\"name\") = upper('prod')\n"
				+ "group by l.cloud, l.landing_zone\n" + "\n" + ") AS subq3\n"
				+ "ON subq1.cloud = subq3.cloud AND subq1.landing_zone = subq3.landing_zone\n";

		StringBuilder sb = new StringBuilder("");
		if (departmentId != null) {
			sb.append(" and d.id = ? ");
		}
		if (productId != null) {
			sb.append(" and p.id = ?  ");
		}
		if (!StringUtils.isBlank(cloud)) {
			sb.append(" and upper(l.cloud) = upper(?) ");
		}
		primarySql = primarySql.replaceAll("##CONDITION##", sb.toString());
		logger.debug("Environment summary query {}", primarySql);

		Query query = entityManager.createNativeQuery(primarySql.toString(), EnvironmentSummaryQueryObj.class);

		int index = 0;
		query.setParameter(++index, orgId);
		if (departmentId != null) {
			query.setParameter(++index, departmentId);
		}
		if (productId != null) {
			query.setParameter(++index, productId);
		}
		if (!StringUtils.isBlank(cloud)) {
			query.setParameter(++index, cloud);
		}
		query.setParameter(++index, orgId);
		query.setParameter(++index, orgId);
		List<EnvironmentSummaryQueryObj> list = query.getResultList();
		return filterEnvironmentSummary(list);
	}

	private List<EnvironmentQueryDTO> filterEnvironmentSummary(List<EnvironmentSummaryQueryObj> list) {
		Set<String> cloudSet = list.stream().map(EnvironmentSummaryQueryObj::getCloud).collect(Collectors.toSet());
		List<EnvironmentQueryObj> environmentQueryObjList = new ArrayList<>();
		for (Object obj : cloudSet) {
			String cloudName = (String) obj;
			logger.debug("Getting list for cloud: {}", cloudName);
			List<EnvironmentSummaryQueryObj> filteredList = list.stream()
					.filter(l -> !StringUtils.isBlank(l.getCloud()) && l.getCloud().equalsIgnoreCase(cloudName))
					.collect(Collectors.toList());
			EnvironmentQueryObj dto = EnvironmentQueryObj.builder().cloud(cloudName)
					.environmentSummaryList(filteredList).build();
			environmentQueryObjList.add(dto);
		}
		List<EnvironmentQueryDTO> environmentQueryDTOList = EnvironmentQueryMapper.INSTANCE
				.toDtoList(environmentQueryObjList);
		for (EnvironmentQueryDTO environmentQueryDTO : environmentQueryDTOList) {
			for (EnvironmentSummaryQueryDTO environmentSummaryQueryDTO : environmentQueryDTO
					.getEnvironmentSummaryList()) {
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
		logger.debug("Getting organization and deparment wise services list for an organization. Org Id: {}", orgId,
				depId);
		return queryRepository.getOrgDepProductWiseServices(orgId, depId);
	}

	public List<String> getOrgDepLandingZoneWiseServices(Long orgId, Long depId) {
		logger.debug("Request to get list of landing zone of an Department an Organization");
		return queryRepository.getDepartmentLandingZones(orgId, depId);
	}

	public List<String> getOrgDepProductEncWiseServices(Long orgId, Long depId) {
		logger.debug("Request to get list of product enclaves of an Department an Organization");
		return queryRepository.getOrganizationDepartmentsProductEnclave(orgId, depId);
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
		return queryRepository.getOrganizationServiceTypeMicroServices(orgId, serviceType);
	}

	public List<Object> getOrgServiceCostServices(Long orgId, String serviceName) {
		logger.debug("Request to get list of services-cost of an serviceName  an Organization");
		return queryRepository.getOrganizationServiceNameMicroServices(orgId, serviceName);
	}

	public List<Object> getOrgServiceDailyCostServices(Long orgId, String serviceName) {
		logger.debug("Request to get list of services-cost-daily of an serviceName  an Organization");
		return queryRepository.getOrgServiceDailyCostServices(orgId, serviceName);
	}

	public List<Object> getOrgServiceWeeklyCostServices(Long orgId, String serviceName) {
		logger.debug("Request to get list of services-cost-weekly of an serviceName  an Organization");
		return queryRepository.getOrganizationServiceNameWeeklyMicroServices(orgId, serviceName);
	}

	public List<Object> getOrgServiceMonthlyCostServices(Long orgId, String serviceName) {
		logger.debug("Request to get list of services-cost-monthly of an serviceName  an Organization");
		return queryRepository.getOrgServiceMonthlyCostServices(orgId, serviceName);
	}

	public List<String> getOrgLandingZoneServices(Long orgId, String landingZone) {
		logger.debug("Request to get list of services of  an landingZone  an Organization");
		return queryRepository.getOrgLandingZoneServices(orgId, landingZone);
	}

	public List<String> getOrgLandingZoneMicroServices(Long orgId, String landingZone) {
		logger.debug("Request to get list of services of  an landingZone  an Organization");
		return queryRepository.getOrgLandingZoneMicroServices(orgId, landingZone);
	}

	public List<Object> getOrgServiceSlaServices(Long orgId, String name) {
		logger.debug("Request to get list of services-sla of an serviceName  an Organization");
		return queryRepository.getOrgServiceSlaServices(orgId, name);
	}

	public List<Object> getOrgServiceCureentSlaServices(Long orgId, String serviceName) {
		logger.debug("Request to get list of services-cureent-sla of an serviceName  an Organization");
		return queryRepository.getOrganizationServiceCurrentSlaMicroServices(orgId, serviceName);
	}

	public List<Object> getOrgServiceWeeklySlaServices(Long orgId, String serviceName) {
		logger.debug("Request to get list of services-weekly-sla of an serviceName  an Organization");
		return queryRepository.getOrgServiceWeeklySlaServices(orgId, serviceName);
	}

	public List<Object> getOrgServiceMonthlySlaServices(Long orgId, String serviceName) {
		logger.debug("Request to get list of services-monthly-sla of an serviceName  an Organization");
		return queryRepository.getOrgServiceMonthlySlaServices(orgId, serviceName);
	}

	public List<String> getOrgEnvServices(Long orgId, Long env) {
		logger.debug("Request to get list of services  of an Organization an Env");
		return queryRepository.getOrganizationEnvMicroServices(orgId, env);
	}

	public List<Object> getOrgProductEnvServices(Long orgId, String product, Long env) {
		logger.debug("Request to get list of services  of an Organization an product an Env");
		return queryRepository.getOrgProductEnvServices(orgId, product, env);
	}

	public List<String> getOrgDepProductServices(Long orgId, Long depId, String product) {
		logger.debug("Request to get list of services of an department an product an Organization");
		return queryRepository.getOrganizationDepartmentsProductMicroServices(orgId, depId, product);
	}

	public List<String> getOrgDepEnvironmentServices(Long orgId, Long depId, Long env) {
		logger.debug("Request to get list of services of an department  an env an Organization");
		return queryRepository.getOrganizationDepartmentsEnvMicroServices(orgId, depId, env);
	}

	public List<Object> getOrgDepProductEnvironmentServices(Long orgId, Long depId, String product, Long env) {
		logger.debug("Request to get list of services of an department an product an env an Organization");
		return queryRepository.getOrganizationDepartmentsProductEnvMicroServices(orgId, depId, product, env);
	}

	public List<String> getOrgDepServices(Long orgId, Long depId, String serviceType) {
		logger.debug("Request to get list of services of an department an serviceType  an Organization");
		return queryRepository.getOrganizationDepartmentsServiceTypeMicroServices(orgId, depId, serviceType);
	}

	public List<Object> getOrgDepServicesCost(Long orgId, Long depId, String serviceName) {
		logger.debug("Request to get list of services-cost of an depId an serviceName  an Organization");
		return queryRepository.getOrganizationDepartmentsServiceNameMicroServices(orgId, depId, serviceName);
	}

	public List<Object> getOrgDepServicesDailyCost(Long orgId, Long depId, String serviceName) {
		logger.debug("Request to get list of services-cost-daily of an depId an serviceName  an Organization");
		return queryRepository.getOrgDepServicesDailyCost(orgId, depId, serviceName);
	}

	public List<Object> getOrgDepServicesWeeklyCost(Long orgId, Long depId, String serviceName) {
		logger.debug("Request to get list of services-cost-weekly of an depId an serviceName  an Organization");
		return queryRepository.getOrgDepServicesWeeklyCost(orgId, depId, serviceName);
	}

	public List<Object> getOrgDepServicesMonthlyCost(Long orgId, Long depId, String serviceName) {
		logger.debug("Request to get list of services-cost-monthly of an depId an serviceName  an Organization");
		return queryRepository.getOrgDepServicesMonthlyCost(orgId, depId, serviceName);
	}

	public List<String> getOrgDepLandingZoneService(Long orgId, Long depId, String landingZone) {
		logger.debug("Request to get list of services of an department an landingZone  an Organization");
		return queryRepository.getOrgDepLandingZoneService(orgId, depId, landingZone);
	}

	public List<String> getOrgDepProductsService(Long orgId, Long depId, String landingZone) {
		logger.debug("Request to get list of services of an department an landingZone  an Organization");
		return queryRepository.getOrgDepProductsService(orgId, depId, landingZone);
	}

	public List<Object> getOrgDepServiceSla(Long orgId, Long depId, String name) {
		logger.debug("Request to get list of services-sla of an serviceName an department  an Organization");
		return queryRepository.getOrgDepServiceSla(orgId, depId, name);
	}

	public List<Object> getOrgDepServiceCureentSla(Long orgId, Long depId, String serviceName) {
		logger.debug("Request to get list of services-cureent-sla of an serviceName an department  an Organization");
		return queryRepository.getOrgDepServiceCureentSla(orgId, depId, serviceName);
	}

	public List<Object> getOrgDepServiceWeeklySla(Long orgId, Long depId, String serviceName) {
		logger.debug("Request to get list of services-weekly-sla of an serviceName an department  an Organization");
		return queryRepository.getOrgDepServiceWeeklySla(orgId, depId, serviceName);
	}

	public List<Object> getOrgDepServiceMonthlySla(Long orgId, Long depId, String serviceName) {
		logger.debug("Request to get list of services-monthly-sla of an serviceName an department  an Organization");
		return queryRepository.getOrgDepServiceMonthlySla(orgId, depId, serviceName);
	}

	public List<String> orgDepLandingZoneProductEnclave(Long orgId, Long depId, String landingZone) {
		logger.debug("Request to get list of product enclaves of landingZoneName an Department an Organization");
		return queryRepository.getOrganizationDepartmentLandingzoneProductEnclave(orgId, depId, landingZone);
	}

	public List<String> orgLandingZoneProductEnclave(Long orgId, String landingZone) {
		logger.debug("Request to get list of product enclaves of landing-zone and organization");
		return queryRepository.orgLandingZoneProductEnclave(orgId, landingZone);
	}

	public List<CloudEnvironmentVpcQueryObj> orgVpcSummary(Long orgId, String landingZone, String product) {
		logger.debug("Request to get list of vpc for given organization, landing-zone and product");
		return queryRepository.orgVpcSummary(orgId, landingZone, product);
	}

	public List<InfraTopologyCloudElementQueryObj> getInfraTopologyCloudElementList(Long orgId, String landingZone,
			String productEnclaveInstanceId) {
		logger.debug(
				"Getting infra-topology cloud elements list for a given organization, landing-zone and product-enclave");
		return queryRepository.getInfraTopologyCloudElementList(orgId, landingZone, productEnclaveInstanceId);
	}

	public InfraTopology3TierStatsQueryObj getInfraTopology3TierStats(Long orgId, String landingZone,
			String productEnclaveInstanceId, String cloudElementInstanceId) {
		logger.debug(
				"Getting infra-topology 3 tier statistics for a given organization, landing-zone, product-enclave and cloud-element");
		return queryRepository.getInfraTopology3TierStats(orgId, landingZone, productEnclaveInstanceId,
				cloudElementInstanceId);
	}

	public InfraTopologySOAStatsQueryObj getInfraTopologySOAStats(Long orgId, String landingZone,
			String productEnclaveInstanceId, String cloudElementInstanceId) {
		logger.debug(
				"Getting infra-topology SOA statistics for a given organization, landing-zone, product-enclave and cloud-element");
		return queryRepository.getInfraTopologySOAStats(orgId, landingZone, productEnclaveInstanceId,
				cloudElementInstanceId);
	}

	public List<InfraTopologyCategoryWiseViewQueryObj> getInfraTopologyCategoryWiseView(Long orgId, String landingZone,
			String productEnclaveInstanceId) {
		logger.debug(
				"Getting infra-topology category-wise(app/data/data-lake/service-mesh) view for a given organization, landing-zone and product-enclave");
		List<InfraTopologyCategoryWiseViewQueryObj> infraTopologyCategoryWiseViewQueryObjList = queryRepository
				.getInfraTopologyCategoryWiseView(orgId, landingZone, productEnclaveInstanceId);
		for (InfraTopologyCategoryWiseViewQueryObj infraTopologyCategoryWiseViewQueryObj : infraTopologyCategoryWiseViewQueryObjList) {
			if ("ECS".equalsIgnoreCase(infraTopologyCategoryWiseViewQueryObj.getElementType())
					|| "EKS".equalsIgnoreCase(infraTopologyCategoryWiseViewQueryObj.getElementType())) {
				infraTopologyCategoryWiseViewQueryObj.getMetadata().put("cpuUtilization", 0);
				infraTopologyCategoryWiseViewQueryObj.getMetadata().put("memory", 0);
				infraTopologyCategoryWiseViewQueryObj.getMetadata().put("networkBytesIn", 0);
				infraTopologyCategoryWiseViewQueryObj.getMetadata().put("networkBytesOut", 0);
				infraTopologyCategoryWiseViewQueryObj.getMetadata().put("cpuReservation", 0);
				infraTopologyCategoryWiseViewQueryObj.getMetadata().put("memoryReservation", 0);
			}
		}
		return infraTopologyCategoryWiseViewQueryObjList;
	}

	public List<InfraTopologyGlobalServiceCategoryWiseViewQueryObj> getInfraTopologyGlobalServiceCategoryWiseView(
			Long orgId, Long landingZoneId) {
		logger.debug(
				"Getting infra-topology category-wise(app/data/data-lake/service-mesh) view of global services for a given organization, landing-zone");
		List<InfraTopologyGlobalServiceCategoryWiseViewQueryObj> infraTopologyGlobalServiceCategoryWiseViewQueryObjList = queryRepository
				.getInfraTopologyGlobalServiceCategoryWiseView(orgId, landingZoneId);
		return infraTopologyGlobalServiceCategoryWiseViewQueryObjList;
	}

//	public InfraTopologyObj getInfraTopology(Long orgId, String landingZone) {
//		logger.debug("Getting infra-topology-view for a given organization and landing-zone");
//		List<InfraTopology3TierQueryObj> threeTierlist = queryRepository.getInfraTopology3TierView(orgId,landingZone);
//		List<InfraTopologySOAQueryObj> soaList = queryRepository.getInfraTopologySOAView(orgId,landingZone);
//		return filterInfraTopologyData(threeTierlist, soaList, landingZone);
//	}
	public InfraTopologyObj getInfraTopologyByLandingZoneId(Long orgId, Long landingZoneId) {
		logger.debug("Getting infra-topology-view for a given organization and landing-zone, Landing zone id: {}",
				landingZoneId);

		InfraTopologyObj infraTopologyObj = InfraTopologyObj.builder().build();
		Optional<Landingzone> o = landingzoneService.findOne(landingZoneId);
		if (!o.isPresent()) {
			logger.error("Landing zone not found for given landing zone id {} ", landingZoneId);
			return infraTopologyObj;
		}
		infraTopologyObj.setLandingZone(o.get().getLandingZone());

		List<InfraTopologyQueryObj> infraTopologyQueryObjList = queryRepository.getInfraTopologyByLandingZoneId(orgId,
				landingZoneId);
		List<InfraTopologyProductEnclaveObj> infraTopologyProductEnclaveObjList = filterInfraTopologyAssociatedProductEnclaveServiceData(
				infraTopologyQueryObjList);

		List<InfraTopologyQueryObj> infraTopologyGlobalQueryObjList = queryRepository
				.getInfraTopologyGlobalByLandingZoneId(orgId, landingZoneId);
		List<InfraTopologyProductEnclaveObj> infraTopologyGlobalObjList = filterInfraTopologyAssociatedProductEnclaveServiceData(
				infraTopologyGlobalQueryObjList);

		infraTopologyObj.setProductEnclaveList(infraTopologyProductEnclaveObjList);
		infraTopologyObj.setGlobalServiceList(infraTopologyGlobalObjList);
		return infraTopologyObj;
	}

	private List<InfraTopologyProductEnclaveObj> filterInfraTopologyAssociatedProductEnclaveServiceData(
			List<InfraTopologyQueryObj> infraTopologyQueryObjList) {
		List<InfraTopologyProductEnclaveObj> productEnclaveObjList = new ArrayList<>();
		for (InfraTopologyQueryObj queryObj : infraTopologyQueryObjList) {
			InfraTopologyProductEnclaveObj productEnclaveObj = InfraTopologyProductEnclaveObj.builder()
					.id(queryObj.getId()).instanceId(queryObj.getInstanceId()).instanceName(queryObj.getInstanceName())
					.build();
			ThreeTierQueryObj threeTierQueryObj = ThreeTierQueryObj.builder()
					.productCount(queryObj.getThreeTier().get("productCount").asLong())
					.webCount(queryObj.getThreeTier().get("webCount").asLong())
					.appCount(queryObj.getThreeTier().get("appCount").asLong())
					.dataCount(queryObj.getThreeTier().get("dataCount").asLong())
					.auxiliaryCount(queryObj.getThreeTier().get("auxiliaryCount").asLong()).build();
			productEnclaveObj.setThreeTier(threeTierQueryObj);

			SOAQueryObj soaQueryObj = SOAQueryObj.builder().productCount(queryObj.getSoa().get("productCount").asLong())
					.appCount(queryObj.getSoa().get("appCount").asLong())
					.dataCount(queryObj.getSoa().get("dataCount").asLong())
					.otherCount(queryObj.getSoa().get("otherCount").asLong()).build();
			productEnclaveObj.setSoa(soaQueryObj);
			productEnclaveObjList.add(productEnclaveObj);
		}
		return productEnclaveObjList;
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
		if (perDay != null && perDay != 0) {
			perDay = perDay / 24;
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

	public List<MonthlyStatisticsQueryObj> monthlyStatisticsQueryObj(Long orgId) {
		logger.debug("Getting list of monthly statistics for given organization");
		return queryRepository.monthlyStatisticsQueryObj(orgId);
	}

	public TotalBudgetQueryObj getTotalBudget(Long orgId) {
		logger.debug("Getting total budget of organization");
		return queryRepository.getTotalBudget(orgId);
	}

	public List<CostAnalyticQueryObj> getProductWiseCostNonAssociate(Long orgId) {
		logger.debug("Get product wise cost non associate ");
		return queryRepository.getProductWiseCostNonAssociate(orgId);
	}

	public List<CostAnalyticQueryObj> getProductWiseCostAssociate(Long orgId) {
		logger.debug("Get product wise cost associate");
		return queryRepository.getProductWiseCostAssociate(orgId);
	}

	public List<CostAnalyticQueryObj> getProductionVsOthersCostAssociate(Long orgId) {
		logger.debug("Get production vs others associate cost ");
		return queryRepository.getProductionVsOthersCostAssociate(orgId);
	}

	public List<DepartmentCostAnalyticQueryObj> getDepartmentCost(Long orgId) {
		logger.debug("Get department wise cost detail ");
		return queryRepository.getDepartmentCost(orgId);
	}

	public List<CloudElementCostAnalyticQueryObj> getCloudElementWiseCostDetail() {
		logger.debug("Get cloud element wise cost detail ");
		return queryRepository.getCloudElementWiseCostDetail();
	}
	public List<CloudCostAnalyticQueryObj> getCloudWiseCostDetail() {
		logger.debug("Get cloud  wise cost detail ");
		return queryRepository.getCloudWiseCostDetail();
	}
	public List<AwsAccountCostAnalyticQueryObj> getAwsAccountWiseCostDetail() {
		logger.debug("Get cloud aws account wise cost detail ");
		return queryRepository.getAwsAccountWiseCostDetail();
	}
	public List<CostAnalyticQueryObj> getProductionVsOthersCostNonAssociate(Long orgId) {
		logger.debug("Get production vs others non associate cost ");
		return queryRepository.getProductionVsOthersCostNonAssociate(orgId);
	}

	public List<CostAnalyticQueryObj> getServiceTypeWiseCostNonAssociate(Long orgId) {
		logger.debug("Get service type wise cost non associate ");
		return queryRepository.getServiceTypeWiseCostNonAssociate(orgId);
	}

	public List<CostAnalyticQueryObj> getServiceTypeWiseCostAssociate(Long orgId) {
		logger.debug("Get service type wise cost  associate ");
		return queryRepository.getServiceTypeWiseCostAssociate(orgId);
	}

	public List<CostBillingQueryObj> getDataGeneratorOrgBilling(Long orgId, String entity) {
		logger.debug("Get  org  wise billing ");
		return queryRepository.getDataGeneratorOrgBilling(orgId, entity);
	}

	public List<CostBillingQueryObj> getOrgAndElementNameBilling(Long orgId, String entity, String elementName) {
		logger.debug("Get  org and elementName  wise billing ");
		return queryRepository.getOrgAndElementNameBilling(orgId, entity, elementName);
	}

	public List<CostBillingQueryObj> getOrgAndLandingZoneBilling(Long orgId, String entity, Long landingZone) {
		logger.debug("Get  org and landingZone  wise billing ");
		return queryRepository.getOrgAndLandingZoneBilling(orgId, entity, landingZone);
	}

	public List<CostBillingQueryObj> getOrgAndElementNameAndLandingZoneBilling(Long orgId, String entity,
			Long landingZone, String elementName) {
		logger.debug("Get  org and landingZone  wise billing ");
		return queryRepository.getOrgAndElementNameAndLandingZoneBilling(orgId, entity, landingZone, elementName);
	}

	public List<SlaAnalyticQueryObj> getSlaWiseCostNonAssociate(Long orgId) {
		logger.debug("Get  org   wise sla non associate ");
		return queryRepository.getSlaWiseCostNonAssociate(orgId);
	}

	public List<SlaAnalyticQueryObj> getSlaWiseCostAssociate(Long orgId) {
		logger.debug("Get  org   wise sla  associate ");
		return queryRepository.getSlaWiseCostAssociate(orgId);
	}

	public List<ApplicationTopologyQueryObj> getApplicationTopology(Long orgId, Long landingZoneId) {
		logger.debug("Get application topology data");
		return queryRepository.getApplicationTopology(orgId, landingZoneId);
	}

	public List<ProcessCentralAnalyticQueryObj> getProcessCentralAnalyticData(Long orgId) {
		logger.debug("Get process central data of organization id: {}", orgId);
		return queryRepository.getProcessCentralAnalyticData(orgId);
	}

	public List<BusinessElement> getServiceViewTopology(Long landingZoneId, String productName, String deptName,
			String env, String productType, String serviceNature) {
		logger.debug("Get service view topology: ");
		return businessElementService.getServiceViewTopology(landingZoneId, productName, deptName, env, productType,
				serviceNature);
	}
//	public EnvironmentCountQueryObj getResourceCountsByOrgAndCloudAndLandingZone(Long orgId, String cloud,Long landingZone) {
//		  logger.debug("Getting cloud wise landing zone and their resource counts for an organization and given cloud. Org Id: {}, Cloud: {}", orgId,cloud);
//	        return queryRepository.getResourceCountsByOrgAndCloudAndLandingZone(cloud, orgId,landingZone);
//	}

	public List<CloudWiseLandingzoneCountQueryObj> getCloudWiseLandingzoneCount(Long orgId){
		logger.debug("Get cloud wise landing-zone counts of an organization. organization id: {}", orgId);
		return queryRepository.getCloudWiseLandingzoneCount(orgId);
	}

	public List<String> getBiMappingCloudElements(Long orgId, Long departmentId, Long productId, Long productEnvId){
		logger.debug("Get list of cloud-elements for bi mapping. organization id: {}, department id:{}, product id: {}, product-environment id: {}", orgId, departmentId, productId, productEnvId);
		return queryRepository.getBiMappingCloudElements(orgId, departmentId, productId, productEnvId);
	}


	public List<SpendOverviewReportObj> getSpendOverviewReport(Long orgId, String serviceCategory, String startDate, String endDate, String cloud){
		logger.debug("Get spend-overview report. organization id: {}, start date:{}, end date id: {}, cloud: {}", orgId, startDate, endDate, cloud);
		String sql = "";
		if(!Constants.ALL.equalsIgnoreCase(serviceCategory)){
			sql = ReportingQueryConstants.SPEND_OVERVIEW.replace("#DYNAMIC_CONDITION#"," and upper(ce.service_category) = upper(?) ");
		}else {
			sql = ReportingQueryConstants.SPEND_OVERVIEW.replace("#DYNAMIC_CONDITION#"," ");
		}

		Query query = entityManager.createNativeQuery(sql, SpendOverviewReportObj.class);
		int index = 0;
		query.setParameter(++index, startDate);
		query.setParameter(++index, endDate);
		query.setParameter(++index, cloud);
		query.setParameter(++index, orgId);
		if(!Constants.ALL.equalsIgnoreCase(serviceCategory)){
			query.setParameter(++index, serviceCategory);
		}

		List<SpendOverviewReportObj> list = query.getResultList();
		if(list.size() <= 1){
			return Collections.emptyList();
		}
		return list;
	}

	public List<TopUsedServicesReportObj> getTopUsedServicesReport(Long orgId, String service, String startDate, String endDate, String prevStartDate, String prevEndDate, String cloud, Long noOfRecords, String order){

		logger.debug("Get top-used-services report. organization id: {}, start date:{}, end date id: {}, cloud: {}", orgId, startDate, endDate, cloud);
		String sql = "";
		if(!Constants.ALL.equalsIgnoreCase(service)){
			sql = ReportingQueryConstants.TOP_USED_SERVICES.replaceAll("#DYNAMIC_CONDITION#"," and upper(ce.element_type) = upper(?) ");
		}else {
			sql = ReportingQueryConstants.TOP_USED_SERVICES.replaceAll("#DYNAMIC_CONDITION#"," ");
		}

		if (noOfRecords != null){
			sql = sql.replaceAll("#DYNAMIC_LIMIT#"," limit "+noOfRecords);
		}else {
			sql = sql.replaceAll("#DYNAMIC_LIMIT#"," ");
		}

		if(!StringUtils.isBlank(order) && Constants.ASC.equalsIgnoreCase(order)){
			sql = sql.replaceAll("#DYNAMIC_ORDER#",Constants.ASC);
		}else {
			sql = sql.replaceAll("#DYNAMIC_ORDER#",Constants.DESC);
		}

		Query query = entityManager.createNativeQuery(sql, TopUsedServicesReportObj.class);
		int index = 0;
		query.setParameter(++index, prevStartDate);
		query.setParameter(++index, prevEndDate);
		query.setParameter(++index, orgId);
		query.setParameter(++index, cloud);
		if(!Constants.ALL.equalsIgnoreCase(service)){
			query.setParameter(++index, service);
		}

		query.setParameter(++index, startDate);
		query.setParameter(++index, endDate);
		query.setParameter(++index, orgId);
		query.setParameter(++index, cloud);
		if(!Constants.ALL.equalsIgnoreCase(service)){
			query.setParameter(++index, service);
		}

		query.setParameter(++index, startDate);
		query.setParameter(++index, endDate);
		query.setParameter(++index, orgId);
		query.setParameter(++index, cloud);
		if(!Constants.ALL.equalsIgnoreCase(service)){
			query.setParameter(++index, service);
		}

		List<TopUsedServicesReportObj> list = query.getResultList();
//		if(list.size() <= 1){
//			return Collections.emptyList();
//		}
		return list;
	}

	public List<CostOfTopAccountsReportObj> getCostOfTopAccountsReport(Long orgId, String cloud, String account, String startDate, String endDate, Long noOfRecords, String order){

		logger.debug("Get cost-of-top-accounts report. organization id: {}, start date:{}, end date id: {}, cloud: {}", orgId, startDate, endDate, cloud);
		String sql = "";
		if(!Constants.ALL.equalsIgnoreCase(account)){
			sql = ReportingQueryConstants.COST_OF_TOP_ACCOUNTS.replaceAll("#DYNAMIC_CONDITION#"," and upper(d.\"name\") = upper(?) ");
		}else {
			sql = ReportingQueryConstants.COST_OF_TOP_ACCOUNTS.replaceAll("#DYNAMIC_CONDITION#"," ");
		}

		if (noOfRecords != null){
			sql = sql.replaceAll("#DYNAMIC_LIMIT#"," limit "+noOfRecords);
		}else {
			sql = sql.replaceAll("#DYNAMIC_LIMIT#"," ");
		}

		if(!StringUtils.isBlank(order) && Constants.ASC.equalsIgnoreCase(order)){
			sql = sql.replaceAll("#DYNAMIC_ORDER#",Constants.ASC);
		}else {
			sql = sql.replaceAll("#DYNAMIC_ORDER#",Constants.DESC);
		}

		Query query = entityManager.createNativeQuery(sql, CostOfTopAccountsReportObj.class);
		int index = 0;
		query.setParameter(++index, startDate);
		query.setParameter(++index, endDate);
		query.setParameter(++index, orgId);

		query.setParameter(++index, startDate);
		query.setParameter(++index, endDate);
		query.setParameter(++index, cloud);
		if(!Constants.ALL.equalsIgnoreCase(account)){
			query.setParameter(++index, account);
		}
		query.setParameter(++index, orgId);

		List<CostOfTopAccountsReportObj> list = query.getResultList();
		return list;
	}

	public List<SpendingTrendReportObj> getSpendingTrendReport(Long orgId, String startDate, String endDate, String prevStartDate, String prevEndDate, String futureStartDate, String futureEndDate, String cloud){
		logger.debug("Get spending-trend report. organization id: {}, start date:{}, end date id: {}, previous start date:{}, previous end date id: {}, future start date:{}, future end date id: {}, cloud: {}", orgId, startDate, endDate, prevStartDate,prevEndDate,futureStartDate,futureEndDate, cloud);
		Query query = entityManager.createNativeQuery(ReportingQueryConstants.SPENDING_TREND, SpendingTrendReportObj.class);
		int index = 0;
		query.setParameter(++index, prevStartDate);
		query.setParameter(++index, prevEndDate);
		query.setParameter(++index, orgId);
		query.setParameter(++index, startDate);
		query.setParameter(++index, endDate);
		query.setParameter(++index, orgId);
		query.setParameter(++index, futureStartDate);
		query.setParameter(++index, futureEndDate);
		List<SpendingTrendReportObj> list = query.getResultList();
		return list;
	}

	public List<SpendOverviewDetailReportObj> getSpendOverviewDetailReport(Long orgId, String serviceCategory, String startDate, String endDate, String prevStartDate, String prevEndDate, String prevToPrevStartDate, String prevToPrevEndDate, String cloud){
		logger.debug("Get spend-overview-detail report. organization id: {}, start date:{}, end date id: {}, prev start date:{}, prev end date id: {}, prev to prev start date:{}, prev to prev end date id: {}, cloud: {}", orgId, startDate, endDate, prevStartDate, prevEndDate, prevToPrevStartDate, prevToPrevEndDate, cloud);
		String sql = ReportingQueryConstants.SPEND_OVERVIEW_DETAIL;
		Query query = entityManager.createNativeQuery(sql, SpendOverviewDetailReportObj.class);
		int index = 0;
		query.setParameter(++index, prevToPrevStartDate);
		query.setParameter(++index, prevToPrevEndDate);
		query.setParameter(++index, cloud);
		query.setParameter(++index, serviceCategory);
		query.setParameter(++index, orgId);

		query.setParameter(++index, prevStartDate);
		query.setParameter(++index, prevEndDate);
		query.setParameter(++index, cloud);
		query.setParameter(++index, serviceCategory);
		query.setParameter(++index, orgId);

		query.setParameter(++index, startDate);
		query.setParameter(++index, endDate);
		query.setParameter(++index, cloud);
		query.setParameter(++index, serviceCategory);
		query.setParameter(++index, orgId);

		query.setParameter(++index, endDate);
		query.setParameter(++index, startDate);

		query.setParameter(++index, prevEndDate);
		query.setParameter(++index, prevStartDate);

		List<SpendOverviewDetailReportObj> list = query.getResultList();
		return list;
	}

	public List<TopUsedServicesDetailReportObj> getTopUsedServiceDetailReport(Long orgId, String startDate, String endDate, String prevStartDate, String prevEndDate, String prevToPrevStartDate, String prevToPrevEndDate, String cloud){
		logger.debug("Get top-used-service-detail report. organization id: {}, start date:{}, end date id: {}, prev start date:{}, prev end date id: {}, prev to prev start date:{}, prev to prev end date id: {}, cloud: {}", orgId, startDate, endDate, prevStartDate, prevEndDate, prevToPrevStartDate, prevToPrevEndDate, cloud);
		String sql = ReportingQueryConstants.TOP_USED_SERVICES_DETAIL;
		Query query = entityManager.createNativeQuery(sql, TopUsedServicesDetailReportObj.class);
		int index = 0;
		query.setParameter(++index, prevToPrevStartDate);
		query.setParameter(++index, prevToPrevEndDate);
		query.setParameter(++index, cloud);
		query.setParameter(++index, orgId);

		query.setParameter(++index, prevStartDate);
		query.setParameter(++index, prevEndDate);
		query.setParameter(++index, cloud);
		query.setParameter(++index, orgId);

		query.setParameter(++index, startDate);
		query.setParameter(++index, endDate);
		query.setParameter(++index, cloud);
		query.setParameter(++index, orgId);

		query.setParameter(++index, endDate);
		query.setParameter(++index, startDate);

		query.setParameter(++index, prevEndDate);
		query.setParameter(++index, prevStartDate);

		List<TopUsedServicesDetailReportObj> list = query.getResultList();
		return list;
	}
	public List<CostOfTopAccountsDetailReportObj> getCostTopAccountsDetailReport(Long orgId, String cloud, Long noOfRecords, String order, String startDate, String endDate, String prevStartDate, String prevEndDate, String prevToPrevStartDate, String prevToPrevEndDate){
		logger.debug("Get cost-of-top-accounts-detail report. organization id: {}, start date:{}, end date id: {}, prev start date:{}, prev end date id: {}, prev to prev start date:{}, prev to prev end date id: {}, cloud: {}", orgId, startDate, endDate, prevStartDate, prevEndDate, prevToPrevStartDate, prevToPrevEndDate, cloud);

		String sql = ReportingQueryConstants.COST_OF_TOP_ACCOUNTS_DETAIL;

		if (noOfRecords != null){
			sql = sql.replaceAll("#DYNAMIC_LIMIT#"," limit "+noOfRecords);
		}else {
			sql = sql.replaceAll("#DYNAMIC_LIMIT#"," ");
		}

		if(!StringUtils.isBlank(order) && Constants.ASC.equalsIgnoreCase(order)){
			sql = sql.replaceAll("#DYNAMIC_ORDER#",Constants.ASC);
		}else {
			sql = sql.replaceAll("#DYNAMIC_ORDER#",Constants.DESC);
		}

		Query query = entityManager.createNativeQuery(sql, CostOfTopAccountsDetailReportObj.class);
		int index = 0;
		query.setParameter(++index, startDate);
		query.setParameter(++index, endDate);
		query.setParameter(++index, orgId);

		query.setParameter(++index, prevToPrevStartDate);
		query.setParameter(++index, prevToPrevEndDate);
		query.setParameter(++index, cloud);
		query.setParameter(++index, orgId);

		query.setParameter(++index, startDate);
		query.setParameter(++index, endDate);
		query.setParameter(++index, cloud);
		query.setParameter(++index, orgId);

		query.setParameter(++index, prevStartDate);
		query.setParameter(++index, prevEndDate);
		query.setParameter(++index, cloud);
		query.setParameter(++index, orgId);

		query.setParameter(++index, startDate);
		query.setParameter(++index, endDate);
		query.setParameter(++index, cloud);
		query.setParameter(++index, orgId);

		query.setParameter(++index, endDate);
		query.setParameter(++index, startDate);

		query.setParameter(++index, prevEndDate);
		query.setParameter(++index, prevStartDate);

		List list = query.getResultList();
		return list;

	}
}
