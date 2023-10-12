package com.synectiks.asset.repository;

import java.util.List;

import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.domain.query.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.synectiks.asset.api.model.SlaAnalyticDTO;
import com.synectiks.asset.domain.Organization;
import com.synectiks.asset.domain.query.CloudElementCloudWiseMonthlyQueryObj;
import com.synectiks.asset.domain.query.CloudElementCloudWiseQueryObj;
import com.synectiks.asset.domain.query.CloudElementSpendAnalyticsQueryObj;
import com.synectiks.asset.domain.query.CloudEnvironmentVpcQueryObj;
import com.synectiks.asset.domain.query.CostAnalyticQueryObj;
import com.synectiks.asset.domain.query.CostBillingQueryObj;
import com.synectiks.asset.domain.query.EnvironmentCountQueryObj;
import com.synectiks.asset.domain.query.EnvironmentSummaryQueryObj;
import com.synectiks.asset.domain.query.InfraTopology3TierQueryObj;
import com.synectiks.asset.domain.query.InfraTopology3TierStatsQueryObj;
import com.synectiks.asset.domain.query.InfraTopologyCategoryWiseViewQueryObj;
import com.synectiks.asset.domain.query.InfraTopologyCloudElementQueryObj;
import com.synectiks.asset.domain.query.InfraTopologySOAQueryObj;
import com.synectiks.asset.domain.query.InfraTopologySOAStatsQueryObj;
import com.synectiks.asset.domain.query.MonthlyStatisticsQueryObj;
import com.synectiks.asset.domain.query.SlaAnalyticQueryObj;
import com.synectiks.asset.domain.query.TotalBudgetQueryObj;

/**
 * Spring Data SQL repository for the query database.
 */
@SuppressWarnings("unused")
@Repository
public interface QueryRepository extends JpaRepository<Organization, Long>{

    String ENV_COUNT_QUERY ="select l.cloud, count(l.id) as environments, \n" +
			" sum(cast (ces.summary_json -> 'TotalDiscoveredResources' as integer)) as assets, \n" +
			" 0 as alerts, 0 as totalbilling \n" +
			" from landingzone l, cloud_element_summary ces, department dep, organization org  \n" +
			" where l.id = ces.landingzone_id  \n" +
			" and l.department_id = dep.id and dep.organization_id = org.id \n" +
			" and org.id = :orgId \n" +
			" group by l.cloud \n";
    @Query(value = ENV_COUNT_QUERY, nativeQuery = true)
    List<EnvironmentCountQueryObj> getEnvStats(@Param("orgId") Long orgId);


    String ENV_CLOUD_WISE_COUNT_QUERY ="select l.cloud, count(l.id) as environments, \n" +
			" sum(cast (ces.summary_json -> 'TotalDiscoveredResources' as integer)) as assets, \n" +
			" 0 as alerts, 0 as totalbilling \n" +
			" from landingzone l, cloud_element_summary ces, department dep, organization org  \n" +
			" where l.id = ces.landingzone_id  \n" +
			" and l.department_id = dep.id and dep.organization_id = org.id \n" +
			" and upper(l.cloud) = upper(:cloud) \n" +
			" and org.id = :orgId \n" +
			" group by l.cloud \n";
    @Query(value = ENV_CLOUD_WISE_COUNT_QUERY, nativeQuery = true)
    EnvironmentCountQueryObj getEnvStats(@Param("cloud") String cloud, @Param("orgId") Long orgId);

	String ENV_LANDINGZONE_CLOUD_WISE_COUNT_QUERY =" select l.cloud, count(l.id) as environments, \n" +
			" sum(cast (ces.summary_json -> 'TotalDiscoveredResources' as integer)) as assets, \n" +
			" 0 as alerts, 0 as totalbilling \n" +
			" from landingzone l, cloud_element_summary ces, department dep, organization org  \n" +
			" where l.id = ces.landingzone_id  \n" +
			" and l.department_id = dep.id and dep.organization_id = org.id \n" +
			" and upper(l.cloud) = upper(:cloud)\n" +
			" and org.id = :orgId \n" +
			" and l.landing_zone = :landingZone \n" +
			" group by l.cloud \n";
	@Query(value = ENV_LANDINGZONE_CLOUD_WISE_COUNT_QUERY, nativeQuery = true)
	EnvironmentCountQueryObj getEnvStats(@Param("landingZone") String landingZone, @Param("cloud") String cloud, @Param("orgId") Long orgId);

	String ORG_WISE_ENV_SUMMARY_QUERY ="select cnv.cloud, replace(cast(ceo.landing_zone as text), '\"', '') as landing_zone, " +
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
            " count(ce.hardware_location -> 'productEnclave') as product_enclave " +
            " from  cloud_element ce group by ce.cloud_environment_id, ce.hardware_location -> 'landingZone' " +
            " ) as ceo, " +
            "cloud_environment cnv, department dep, organization org " +
            "where ceo.cloud_environment_id = cnv.id  " +
            "and cnv.department_id = dep.id " +
            "and dep.organization_id = org.id " +
            "and org.id = :orgId " +
            "group by cnv.cloud, ceo.landing_zone, ceo.product_enclave";
    @Query(value = ORG_WISE_ENV_SUMMARY_QUERY, nativeQuery = true)
    public List<EnvironmentSummaryQueryObj> getEnvironmentSummary(@Param("orgId") Long orgId);

    String ORG_AND_CLOUD_WISE_ENV_SUMMARY_QUERY ="select cnv.cloud, replace(cast(ceo.landing_zone as text), '\"', '') as landing_zone, " +
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
            " count(ce.hardware_location -> 'productEnclave') as product_enclave " +
            " from  cloud_element ce group by ce.cloud_environment_id, ce.hardware_location -> 'landingZone' " +
            " ) as ceo, " +
            "cloud_environment cnv, department dep, organization org " +
            "where ceo.cloud_environment_id = cnv.id  " +
            "and cnv.department_id = dep.id " +
            "and dep.organization_id = org.id " +
            "and org.id = :orgId " +
            "and cnv.cloud = :cloud " +
            "group by cnv.cloud, ceo.landing_zone, ceo.product_enclave";
    @Query(value = ORG_AND_CLOUD_WISE_ENV_SUMMARY_QUERY, nativeQuery = true)
    public List<EnvironmentSummaryQueryObj> getEnvironmentSummary(@Param("orgId") Long orgId, @Param("cloud") String cloud);
	
    
    String PRODUCT_QUERY = "select distinct replace (cast(jsonb_array_elements(ce.hosted_services -> 'HOSTEDSERVICES') -> 'associatedProduct' as text), '\"', '') as products from cloud_element ce, cloud_environment cnv, department dep, organization org where org.id =:orgId and org.id = dep.organization_id and dep.id = cnv.department_id and cnv.id = ce.cloud_environment_id and replace(cast(ce.hardware_location -> 'landingZone' as text),'\"','') = cnv.account_id\r\n";

	@Query(value = PRODUCT_QUERY, nativeQuery = true)
	public List<String> getProduct(@Param("orgId") Long orgId);

	
	String LANDING_ZONE_QUERY = "select cnv .account_id  \r\n"
			+ "from cloud_environment cnv, department dep, organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and dep.id = cnv.department_id\r\n"
			+ "and org.id =:orgId \r\n"
			+ "";

	@Query(value = LANDING_ZONE_QUERY, nativeQuery = true)
	public List<String> getOrgLandingZone(@Param("orgId") Long orgId);

	
	String PRODUCT_ENC_QUERY = "select  replace(cast(ce.hardware_location -> 'productEnlave' as text), '\"', '') as products_enclave\r\n"
			+ "from cloud_element ce,cloud_environment cnv, department dep, organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and dep.id = cnv.department_id\r\n"
			+ "and cnv.id = ce.cloud_environment_id\r\n"
			+ "and org.id =:orgId ";

	@Query(value = PRODUCT_ENC_QUERY, nativeQuery = true)
	public List<String>getOrgWiseProductEnclave(@Param("orgId") Long orgId);


	String ORG_MICRO_SERVICES_QUERY ="select distinct ms.* as service  \r\n"
			+ "from micro_service ms , department dep, organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and ms.department_id = dep.id\r\n"
			+ "and org.id = :orgId\r\n";
		@Query(value = ORG_MICRO_SERVICES_QUERY, nativeQuery = true)
		public List<Object> getOrgWiseServices(@Param("orgId")Long orgId);
		
		String ORG_DEPPRODUCT_QUERY = "select distinct replace (cast(jsonb_array_elements(ce.hosted_services -> 'HOSTEDSERVICES') -> 'associatedProduct' as text), '\"', '') as products from cloud_element ce, cloud_environment cnv, department dep, organization org where org.id =:orgId and dep.id =:depId and org.id = dep.organization_id and dep.id = cnv.department_id and cnv.id = ce.cloud_environment_id and replace(cast(ce.hardware_location -> 'landingZone' as text),'\"','') = cnv.account_id ";
		@Query(value = ORG_DEPPRODUCT_QUERY, nativeQuery = true)
		List<String> getOrgDepProductWiseServices(@Param("orgId")Long orgId, @Param("depId") Long depId);
		
		String DEPARTMENY_LANDING_ZONE_QUERY = "select cnv.account_id  \r\n"
				+ "from cloud_environment cnv, department dep, organization org\r\n"
				+ "where org.id = dep.organization_id\r\n"
				+ "and dep.id = cnv.department_id\r\n"
				+ "and org.id =:orgId  and dep.id =:depId";

		@Query(value = DEPARTMENY_LANDING_ZONE_QUERY, nativeQuery = true)
		public List<String> getDepartmentLandingZones(@Param("orgId") Long orgId,@Param("depId") Long depId);

		String DEPARTMENT_PRODUCT_ENC_QUERY = "select  replace(cast(ce.hardware_location -> 'productEnlave' as text), '\"', '') as products_enclave\r\n"
				+ "from cloud_element ce,cloud_environment cnv, department dep, organization org\r\n"
				+ "where org.id = dep.organization_id\r\n"
				+ "and dep.id = cnv.department_id\r\n"
				+ "and cnv.id = ce.cloud_environment_id\r\n"
				+ "and org.id =:orgId \r\n"
				+ "and dep .id =:depId";

		@Query(value = DEPARTMENT_PRODUCT_ENC_QUERY, nativeQuery = true)
		public List<String> getOrganizationDepartmentsProductEnclave(@Param("orgId") Long orgId,@Param("depId") Long depId);

		String ORG_DEPARTMENT_MICRO_SERVICES_QUERY ="select distinct ms.* as service  \r\n"
				+ "from micro_service ms , department dep, organization org\r\n"
				+ "where org.id = dep.organization_id\r\n"
				+ "and ms.department_id = dep.id\r\n"
				+ "and org.id = :orgId\r\n"
				+ "and dep .id =:depId";
		@Query(value = ORG_DEPARTMENT_MICRO_SERVICES_QUERY, nativeQuery = true)
		public List<Object> getOrganizationDepartmentsMicroServices(@Param("orgId")Long orgId, @Param("depId")Long depId);
	
		String ORG_PRODUCT_MICRO_SERVICES_QUERY ="select distinct ms.product  as service  \r\n"
				+ "from micro_service ms , department dep, organization org\r\n"
				+ "where org.id = dep.organization_id\r\n"
				+ "and ms.department_id = dep.id\r\n"
				+ "and org.id = :orgId\r\n"
				+ "and ms.product =:product ";
		@Query(value = ORG_PRODUCT_MICRO_SERVICES_QUERY, nativeQuery = true)
		public List<String> getOrgProductServices(@Param("orgId")Long orgId, @Param("product")String product);
		
		String ORG_SERVICETYPE_MICRO_SERVICES_QUERY ="select distinct ms.product  as service  \r\n"
				+ "from micro_service ms , department dep, organization org\r\n"
				+ "where org.id = dep.organization_id\r\n"
				+ "and ms.department_id = dep.id\r\n"
				+ "and org.id = :orgId\r\n"
				+ "and ms.service_type  =:serviceType";
		@Query(value = ORG_SERVICETYPE_MICRO_SERVICES_QUERY, nativeQuery = true)
		public List<String> getOrganizationServiceTypeMicroServices(@Param("orgId")Long orgId, @Param("serviceType")String serviceType);
		
		
		String ORG_SERVICES_NAME_COST_SERVICES_QUERY ="select distinct ms.cost_json "
				+ "from micro_service ms , department dep, organization org "
				+ "where org.id = dep.organization_id "
				+ "and ms.department_id = dep.id "
				+ "and org.id = :orgId "
				+ "and ms.name = :serviceName";
		@Query(value = ORG_SERVICES_NAME_COST_SERVICES_QUERY, nativeQuery = true)
		public List<Object> getOrganizationServiceNameMicroServices(@Param("orgId")Long orgId,@Param("serviceName") String serviceName);
		
		String ORG_SERVICES_NAME_COST_SERVICES_DAILY_QUERY ="select distinct ms.cost_json->'DAILYCOST' as  daily_cost "
				+ "from micro_service ms , department dep, organization org "
				+ "where org.id = dep.organization_id "
				+ "and ms.department_id = dep.id "
				+ "and org.id = :orgId "
				+ "and ms.name = :serviceName";
		@Query(value = ORG_SERVICES_NAME_COST_SERVICES_DAILY_QUERY, nativeQuery = true)
		public List<Object> getOrgServiceDailyCostServices(@Param("orgId")Long orgId,@Param("serviceName") String serviceName);
		
		String ORG_SERVICES_NAME_COST_SERVICES_WEEKLY_QUERY ="select distinct ms.cost_json->'WEEKLYCOST' as  weekly_cost\r\n"
				+ "from micro_service ms , department dep, organization org\r\n"
				+ "where org.id = dep.organization_id\r\n"
				+ "and ms.department_id = dep.id\r\n"
				+ "and org.id = :orgId\r\n"
				+ "and ms .\"name\" =:serviceName";
		@Query(value = ORG_SERVICES_NAME_COST_SERVICES_WEEKLY_QUERY, nativeQuery = true)
		public List<Object> getOrganizationServiceNameWeeklyMicroServices(@Param("orgId")Long orgId,@Param("serviceName") String serviceName);
		
		String ORG_SERVICES_NAME_COST_SERVICES_MONTHLY_QUERY ="select distinct ms.cost_json->'MONTHLYCOST' as  monthly_cost\r\n"
				+ "from micro_service ms , department dep, organization org\r\n"
				+ "where org.id = dep.organization_id\r\n"
				+ "and ms.department_id = dep.id\r\n"
				+ "and org.id = :orgId\r\n"
				+ "and ms .\"name\" =:serviceName";
		@Query(value = ORG_SERVICES_NAME_COST_SERVICES_MONTHLY_QUERY, nativeQuery = true)
		public List<Object> getOrgServiceMonthlyCostServices(@Param("orgId")Long orgId,@Param("serviceName") String serviceName);
		
		String ORG_LANDINGZONE_SERVICES_NAME_SERVICES_QUERY ="select distinct  ms.name \r\n"
				+ "from  micro_service ms ,cloud_element ce,cloud_environment cnv, department dep, organization org\r\n"
				+ "where org.id = dep.organization_id\r\n"
				+ "and dep.id = cnv.department_id\r\n"
				+ "and cnv.id = ce.cloud_environment_id\r\n"
				+ "and ms.department_id = dep.id\r\n"
				+ "and ms.department_id = cnv.department_id \r\n"
				+ "and org.id = :orgId\r\n"
				+ "and cnv.account_id =:landingZone";
		@Query(value = ORG_LANDINGZONE_SERVICES_NAME_SERVICES_QUERY, nativeQuery = true)
		public List<String> getOrgLandingZoneServices(@Param("orgId")Long orgId, @Param("landingZone")String landingZone);
		
		String ORG_LANDINGZONE_PRODUCT_NAME_SERVICES_QUERY ="select distinct  ms.product \r\n"
				+ "from  micro_service ms ,cloud_element ce,cloud_environment cnv, department dep, organization org\r\n"
				+ "where org.id = dep.organization_id\r\n"
				+ "and dep.id = cnv.department_id\r\n"
				+ "and cnv.id = ce.cloud_environment_id\r\n"
				+ "and ms.department_id = dep.id\r\n"
				+ "and ms.department_id = cnv.department_id \r\n"
				+ "and org.id = :orgId\r\n"
				+ "and cnv.account_id =:landingZone";
		@Query(value = ORG_LANDINGZONE_PRODUCT_NAME_SERVICES_QUERY, nativeQuery = true)
		public List<String> getOrgLandingZoneMicroServices(@Param("orgId")Long orgId, @Param("landingZone")String landingZone);
	
		String ORG_SERVICETYPE_MICRO_SERVICES_SLA_QUERY ="select distinct ms.sla_json  \r\n"
				+ "from micro_service ms , department dep, organization org\r\n"
				+ "where org.id = dep.organization_id\r\n"
				+ "and ms.department_id = dep.id\r\n"
				+ "and org.id = :orgId\r\n"
				+ "and ms .name =:serviceName";
		@Query(value = ORG_SERVICETYPE_MICRO_SERVICES_SLA_QUERY, nativeQuery = true)
		public List<Object> getOrgServiceSlaServices(@Param("orgId")Long orgId, @Param("serviceName")String serviceName);
		
		String ORG_SERVICETYPE_MICRO_SERVICES_CURRENT_SLA_QUERY ="select distinct ms.sla_json ->'CURRENTSLA' as  current_sla\r\n"
				+ "from micro_service ms , department dep, organization org\r\n"
				+ "where org.id = dep.organization_id\r\n"
				+ "and ms.department_id = dep.id\r\n"
				+ "and org.id = :orgId\r\n"
				+ "and ms .name =:serviceName\r\n";
		@Query(value = ORG_SERVICETYPE_MICRO_SERVICES_CURRENT_SLA_QUERY, nativeQuery = true)
		public List<Object> getOrganizationServiceCurrentSlaMicroServices(@Param("orgId")Long orgId, @Param("serviceName")String serviceName);
		
		String ORG_SERVICETYPE_MICRO_SERVICES_WEEKLY_SLA_QUERY ="select distinct ms.sla_json->'WEEKLYSLA' as  weekly_sla\r\n"
				+ "from micro_service ms , department dep, organization org\r\n"
				+ "where org.id = dep.organization_id\r\n"
				+ "and ms.department_id = dep.id\r\n"
				+ "and org.id = :orgId\r\n"
				+ "and ms .\"name\" =:serviceName";
		@Query(value = ORG_SERVICETYPE_MICRO_SERVICES_WEEKLY_SLA_QUERY, nativeQuery = true)
		public List<Object> getOrgServiceWeeklySlaServices(@Param("orgId")Long orgId, @Param("serviceName")String serviceName);
		
		String ORG_SERVICETYPE_MICRO_SERVICES_MONTHLY_SLA_QUERY ="select distinct ms.sla_json->'MONTHLYSLA' as  monthly_sla\r\n"
				+ "from micro_service ms , department dep, organization org\r\n"
				+ "where org.id = dep.organization_id\r\n"
				+ "and ms.department_id = dep.id\r\n"
				+ "and org.id = :orgId\r\n"
				+ "and ms .\"name\" =:serviceName";
		@Query(value = ORG_SERVICETYPE_MICRO_SERVICES_MONTHLY_SLA_QUERY, nativeQuery = true)
		public List<Object> getOrgServiceMonthlySlaServices(@Param("orgId")Long orgId, @Param("serviceName")String serviceName);
		
		String ORG_ENV_MICRO_SERVICES_QUERY ="select distinct ms.environment  as service  \r\n"
				+ "from micro_service ms , department dep, deployment_environment denv , organization org\r\n"
				+ "where org.id = dep.organization_id\r\n"
				+ "and ms.department_id = dep.id\r\n"
				+ "and denv.id = ms.deployment_environment_id \r\n"
				+ "and org.id = :orgId\r\n"
				+ "and  denv.id =:env";
		@Query(value = ORG_ENV_MICRO_SERVICES_QUERY, nativeQuery = true)
		public List<String> getOrganizationEnvMicroServices(@Param("orgId")Long orgId, @Param("env") Long env);
		
		
		String ORG_PRODUCT_ENV_MICRO_SERVICES_QUERY ="select distinct ms.*  as service   \r\n"
				+ "from micro_service ms , department dep,deployment_environment denv, organization org\r\n"
				+ "where org.id = dep.organization_id\r\n"
				+ "and ms.department_id = dep.id\r\n"
				+ "and denv.id = ms.deployment_environment_id \r\n"
				+ "and org.id = :orgId\r\n"
				+ "and ms.product =:product\r\n"
				+ "and  denv.id =:env";
		@Query(value = ORG_PRODUCT_ENV_MICRO_SERVICES_QUERY, nativeQuery = true)
		public List<Object> getOrgProductEnvServices(@Param("orgId")Long orgId, @Param("product")String product,@Param("env") Long env);
		
		
		String ORG_DEPARTMENT_PRODUCT_MICRO_SERVICES_QUERY ="select distinct ms.product  as service  \r\n"
				+ "from micro_service ms , department dep, organization org\r\n"
				+ "where org.id = dep.organization_id\r\n"
				+ "and ms.department_id = dep.id\r\n"
				+ "and org.id = :orgId\r\n"
				+ "and dep .id =:depId\r\n"
				+ "and ms.product =:product";
		@Query(value = ORG_DEPARTMENT_PRODUCT_MICRO_SERVICES_QUERY, nativeQuery = true)
		public List<String> getOrganizationDepartmentsProductMicroServices(@Param("orgId")Long orgId, @Param("depId")Long depId,@Param("product") String product);
		
		String ORG_DEPARTMENT_ENV_MICRO_SERVICES_QUERY ="select distinct ms.environment  as service  \r\n"
			+ "from micro_service ms , department dep, deployment_environment denv , organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and ms.department_id = dep.id\r\n"
			+ "and denv.id = ms.deployment_environment_id \r\n"
			+ "and org.id = :orgId\r\n"
			+ "and dep.id =:depId\r\n"
			+ "and  denv.id =:env";
	@Query(value = ORG_DEPARTMENT_ENV_MICRO_SERVICES_QUERY, nativeQuery = true)
	public List<String> getOrganizationDepartmentsEnvMicroServices(@Param("orgId")Long orgId, @Param("depId")Long depId,@Param("env")Long env);
	
	String ORG_DEPARTMENT_PRODUCT_ENV_MICRO_SERVICES_QUERY ="select distinct ms.*  as service   \r\n"
			+ "from micro_service ms , department dep,deployment_environment denv, organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and ms.department_id = dep.id\r\n"
			+ "and denv.id = ms.deployment_environment_id \r\n"
			+ "and org.id = :orgId\r\n"
			+ "and dep .id =:depId\r\n"
			+ "and ms.product =:product\r\n"
			+ "and  denv.id =:env";
	@Query(value = ORG_DEPARTMENT_PRODUCT_ENV_MICRO_SERVICES_QUERY, nativeQuery = true)
	public List<Object> getOrganizationDepartmentsProductEnvMicroServices(@Param("orgId")Long orgId, @Param("depId")Long depId,@Param("product")String product,@Param("env")Long env);
	
	String ORG_DEPARTMENT_SERVICETYPE_SERVICES_QUERY ="select distinct ms.product  as service  \r\n"
			+ "from micro_service ms , department dep, organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and ms.department_id = dep.id\r\n"
			+ "and org.id = :orgId"
			+ "and dep .id =:depId"
			+ "and ms.service_type  =:serviceType";
	@Query(value = ORG_DEPARTMENT_SERVICETYPE_SERVICES_QUERY, nativeQuery = true)
	public List<String> getOrganizationDepartmentsServiceTypeMicroServices(@Param("orgId")Long orgId, @Param("depId")Long depId,@Param("serviceType") String serviceType);
	
	String ORG_DEP_SERVICES_NAME_COST_SERVICES_QUERY ="select distinct ms.cost_json  \r\n"
			+ "from micro_service ms , department dep, organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and ms.department_id = dep.id\r\n"
			+ "and org.id = :orgId\r\n"
			+ "and dep .id =:depId\r\n"
			+ "and ms .\"name\" =:serviceName";
	@Query(value = ORG_DEP_SERVICES_NAME_COST_SERVICES_QUERY, nativeQuery = true)
	public List<Object> getOrganizationDepartmentsServiceNameMicroServices(@Param("orgId")Long orgId, @Param("depId")Long  depId,
			@Param("serviceName") String serviceName);
	
	String ORG_DEP_SERVICES_NAME_COST_SERVICES_DAILY_QUERY ="select distinct ms.cost_json->'DAILYCOST' as  daily_cost\r\n"
			+ "from micro_service ms , department dep, organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and ms.department_id = dep.id\r\n"
			+ "and org.id = :orgId\r\n"
			+ "and dep.id = :depId\r\n"
			+ "and ms .\"name\" =:serviceName";
	@Query(value = ORG_DEP_SERVICES_NAME_COST_SERVICES_DAILY_QUERY, nativeQuery = true)
	public List<Object> getOrgDepServicesDailyCost(@Param("orgId")Long orgId, @Param("depId")Long  depId,
			@Param("serviceName") String serviceName);

	String ORG_DEP_SERVICES_NAME_COST_SERVICES_WEEKLY_QUERY ="select distinct ms.cost_json->'WEEKLYCOST' as  weekly_cost\r\n"
			+ "from micro_service ms , department dep, organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and ms.department_id = dep.id\r\n"
			+ "and org.id = :orgId\r\n"
			+ "and dep.id = :depId\r\n"
			+ "and ms .\"name\" =:serviceName";
	@Query(value = ORG_DEP_SERVICES_NAME_COST_SERVICES_WEEKLY_QUERY, nativeQuery = true)
	public List<Object> getOrgDepServicesWeeklyCost(@Param("orgId")Long orgId, @Param("depId")Long  depId,
			@Param("serviceName") String serviceName);
		
	String ORG_DEP_SERVICES_NAME_COST_SERVICES_MONTHLY_QUERY ="select distinct ms.cost_json->'MONTHLYCOST' as  monthly_costt\r\n"
			+ "from micro_service ms , department dep, organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and ms.department_id = dep.id\r\n"
			+ "and org.id = :orgId\r\n"
			+ "and dep.id = :depId\r\n"
			+ "and ms .\"name\" =:serviceName";
	@Query(value = ORG_DEP_SERVICES_NAME_COST_SERVICES_MONTHLY_QUERY, nativeQuery = true)
	public List<Object> getOrgDepServicesMonthlyCost(@Param("orgId")Long orgId, @Param("depId")Long  depId,
			@Param("serviceName") String serviceName);
	
	String ORG_DEP_LANDINGZONE_SERVICES_NAME_SERVICES_QUERY ="select distinct  ms.\"name\"  \r\n"
			+ "from  micro_service ms ,cloud_element ce,cloud_environment cnv, department dep, organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and dep.id = cnv.department_id\r\n"
			+ "and cnv.id = ce.cloud_environment_id\r\n"
			+ "and ms.department_id = dep.id\r\n"
			+ "and ms.department_id = cnv.department_id \r\n"
			+ "and org.id = :orgId\r\n"
			+ "and dep.id =:depId\r\n"
			+ "and cnv.account_id =:landingZone";
	@Query(value = ORG_DEP_LANDINGZONE_SERVICES_NAME_SERVICES_QUERY, nativeQuery = true)
	public List<String> getOrgDepLandingZoneService(@Param("orgId")Long orgId,@Param("depId")Long depId ,@Param("landingZone")String landingZone);
	
	String ORG_DEP_LANDINGZONE_PRODUCT_NAME_SERVICES_QUERY ="select distinct  ms.product \r\n"
			+ "from  micro_service ms ,cloud_element ce,cloud_environment cnv, department dep, organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and dep.id = cnv.department_id\r\n"
			+ "and cnv.id = ce.cloud_environment_id\r\n"
			+ "and ms.department_id = dep.id\r\n"
			+ "and ms.department_id = cnv.department_id \r\n"
			+ "and org.id = :orgId\r\n"
			+ "and dep.id =:depId\r\n"
			+ "and cnv.account_id =:landingZone";
	@Query(value = ORG_DEP_LANDINGZONE_PRODUCT_NAME_SERVICES_QUERY, nativeQuery = true)
	public List<String> getOrgDepProductsService(@Param("orgId")Long orgId,@Param("depId")Long depId ,@Param("landingZone")String landingZone);
	
	String ORG_DEP_SERVICETYPE_MICRO_SERVICES_SLA_QUERY ="select distinct ms.sla_json  \r\n"
			+ "from micro_service ms , department dep, organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and ms.department_id = dep.id\r\n"
			+ "and org.id = :orgId\r\n"
			+ "and dep.id = :depId\r\n"
			+ "and ms .name =:serviceName";
	@Query(value = ORG_DEP_SERVICETYPE_MICRO_SERVICES_SLA_QUERY, nativeQuery = true)
	public List<Object> getOrgDepServiceSla(@Param("orgId")Long orgId,@Param("depId")Long depId, @Param("serviceName")String serviceName);

	String ORG_DEP_SERVICETYPE_MICRO_SERVICES_CURRENT_SLA_QUERY ="select distinct ms.sla_json ->'CURRENTSLA' as  current_sla\r\n"
			+ "from micro_service ms , department dep, organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and ms.department_id = dep.id\r\n"
			+ "and org.id = :orgId\r\n"
			+ "and dep.id = :depId\r\n"
			+ "and ms .name =:serviceName\r\n";
	@Query(value = ORG_DEP_SERVICETYPE_MICRO_SERVICES_CURRENT_SLA_QUERY, nativeQuery = true)
	public List<Object> getOrgDepServiceCureentSla(@Param("orgId")Long orgId,@Param("depId")Long depId, @Param("serviceName")String serviceName);
		
	
	String ORG_DEP_SERVICETYPE_MICRO_SERVICES_WEEKLY_SLA_QUERY ="select distinct ms.sla_json->'WEEKLYSLA' as  weekly_sla\r\n"
			+ "from micro_service ms , department dep, organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and ms.department_id = dep.id\r\n"
			+ "and org.id = :orgId\r\n"
			+ "and dep.id = :depId\r\n"
			+ "and ms .\"name\" =:serviceName";
	@Query(value = ORG_DEP_SERVICETYPE_MICRO_SERVICES_WEEKLY_SLA_QUERY, nativeQuery = true)
	public List<Object> getOrgDepServiceWeeklySla(@Param("orgId")Long orgId,@Param("depId")Long depId, @Param("serviceName")String serviceName);

	
	String ORG_DEP_SERVICETYPE_MICRO_SERVICES_MONTHLY_SLA_QUERY ="select distinct ms.sla_json->'MONTHLYSLA' as  monthly_sla\r\n"
			+ "from micro_service ms , department dep, organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and ms.department_id = dep.id\r\n"
			+ "and org.id = :orgId\r\n"
			+ "and dep.id = :depId\r\n"
			+ "and ms .\"name\" =:serviceName";
	@Query(value = ORG_DEP_SERVICETYPE_MICRO_SERVICES_MONTHLY_SLA_QUERY, nativeQuery = true)
	public List<Object> getOrgDepServiceMonthlySla(@Param("orgId")Long orgId,@Param("depId")Long depId, @Param("serviceName")String serviceName);
	String DEPARTMENT_LANDINGZONENAME_PRODUCT_ENC_QUERY = "select  replace(cast(ce.hardware_location -> 'productEnlave' as text), '\"', '') as products_enclave\r\n"
			+ "from cloud_element ce,cloud_environment cnv, department dep, organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and dep.id = cnv.department_id\r\n"
			+ "and cnv.id = ce.cloud_environment_id\r\n"
			+ "and org.id =:orgId \r\n"
			+ "and dep .id =:depId\r\n"
			+ "and replace(cast(ce.hardware_location -> 'landingZone' as text), '\"', '') =:landingZone";
	@Query(value = DEPARTMENT_LANDINGZONENAME_PRODUCT_ENC_QUERY, nativeQuery = true)
	public List<String> getOrganizationDepartmentLandingzoneProductEnclave(@Param("orgId") Long orgId,@Param("depId") Long depId,@Param("landingZone") String landingZone);
	String QRY = "select replace(cast(ce.hardware_location -> 'productEnlave' as text), '\\\"', '') as products_enclave\r\n"
			+ "from cloud_element ce,cloud_environment cnv, department dep, organization org\r\n"
			+ "where org.id = dep.organization_id\r\n"
			+ "and dep.id = cnv.department_id\r\n"
			+ "and cnv.id = ce.cloud_environment_id\r\n"
			+ "and org.id =:orgId\r\n"
			+ "and replace(cast(ce.hardware_location -> 'landingZone' as text), '\"', '') =:landingZone";
	
	@Query(value = QRY, nativeQuery = true)
	public List<String> orgLandingZoneProductEnclave(@Param("orgId") Long orgId, @Param("landingZone") String landingZone);
	
	String DISCOVER_ASSTES_LANDING_ZONE_PRODUCT = "select ceo.product_enclave, " +
            "(select count(c.obj -> 'associatedProduct') " +
            "from cloud_element ce2, jsonb_array_elements(ce2.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj, pos) " +
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
            " ce.hardware_location -> 'productEnclave' as product_enclave " +
            " from cloud_element ce" +
            " ) as ceo, " +
            "cloud_environment cnv, department dep, organization org " +
            "where ceo.cloud_environment_id = cnv.id  " +
            "and cnv.department_id = dep.id " +
            "and dep.organization_id = org.id " +
            "and org.id = :orgId " +
            "and replace(cast(ceo.landing_zone as text), '\"', '') = :landingZone " +
            "and replace(cast(ceo.product_enclave as text), '\"', '') =  :productEnclave " +
            "group by  ceo.landing_zone, ceo.product_enclave ";
	@Query(value = DISCOVER_ASSTES_LANDING_ZONE_PRODUCT, nativeQuery = true)
	List<CloudEnvironmentVpcQueryObj> orgVpcSummary(@Param("orgId") Long orgId, @Param("landingZone") String landingZone,@Param("productEnclave") String productEnclave);
	
	String SPEND_TODAY_ANALYTICS_QUERY ="with yesterday_sum as (  " +
			"   select coalesce(SUM(cast(jb.value as INT)), 0) as sum_values  " +
			"   from cloud_element ce  " +
			"   cross join jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value)  " +
			"   join landingzone l  on l.id = ce.landingzone_id   " +
			"   join department dep on dep.id = l.department_id  " +
			"   join organization org on org.id = dep.organization_id  " +
			"   where date(jb.key) = current_date - interval '1 day' and org.id =:orgId  " +
			"   )  " +
			"   select  " +
			"   coalesce(SUM(cast(jb.value as INT)), 0) as sum_current_date,  " +
			"   yesterday_sum.sum_values as sum_previous_date,  " +
			"   cast(coalesce(SUM(cast(jb.value as INT)),0) - yesterday_sum.sum_values as float) * 100.0 / yesterday_sum.sum_values as percentage  " +
			"   from  " +
			"   cloud_element ce  " +
			"   cross join jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value)  " +
			"   join landingzone l2 on l2.id = ce.landingzone_id   " +
			"   join department dep on dep.id = l2.department_id  " +
			"   join organization org on org.id = dep.organization_id  " +
			"   cross join yesterday_sum  " +
			"   where  " +
			"   date(jb.key) = current_date  " +
			"   and org.id =:orgId  " +
			"   group by yesterday_sum.sum_values    ";
	@Query(value = SPEND_TODAY_ANALYTICS_QUERY, nativeQuery = true)
	public CloudElementSpendAnalyticsQueryObj spendTodayAnalytics(@Param("orgId") Long orgId);
	
	String SPEND_YESTERDAY_ANALYTICS_QUERY ="with yesterday_sum as ( " +
			"   select coalesce(SUM(cast(jb.value as INT)), 0) as sum_values " +
			"   from cloud_element ce " +
			"   cross join jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value) " +
			"   join landingzone l on l.id = ce.landingzone_id " +
			"   join department dep on dep.id = l.department_id " +
			"   join organization org on org.id = dep.organization_id " +
			"   where date(jb.key) = current_date - interval '2 day' and org.id = :orgId " +
			"   ) " +
			"   select " +
			"   coalesce(SUM(cast(jb.value as INT)), 0) as sum_current_date, " +
			"   yesterday_sum.sum_values as sum_previous_date, " +
			"   cast(coalesce(SUM(cast(jb.value as INT)),0) - yesterday_sum.sum_values as float) * 100.0 / yesterday_sum.sum_values as percentage " +
			"   from " +
			"   cloud_element ce " +
			"   cross join jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value) " +
			"   join landingzone l on l.id = ce.landingzone_id " +
			"   join department dep on dep.id = l.department_id " +
			"   join organization org on org.id = dep.organization_id " +
			"   cross join yesterday_sum " +
			"   where " +
			"   date(jb.key) = current_date - interval '1 day' " +
			"   and org.id = :orgId " +
			"   group by yesterday_sum.sum_values ";
	@Query(value = SPEND_YESTERDAY_ANALYTICS_QUERY, nativeQuery = true)
	public CloudElementSpendAnalyticsQueryObj spendYesterdayAnalytics(@Param("orgId") Long orgId);

	String CURRENT_SPEND_RATE_PER_DAY_QUERY ="select coalesce(sum(cast (jb.value as int)),0) AS sum_values " +
			"from cloud_element ce, " +
			"landingzone l, department dep, organization org,jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value) " +
			"where date(jb.key) = current_date and " +
			"org.id = dep.organization_id " +
			"and dep.id = l.department_id " +
			"and l.id = ce.landingzone_id  " +
			"and org.id = :orgId  ";
	@Query(value = CURRENT_SPEND_RATE_PER_DAY_QUERY, nativeQuery = true)
	Long currentSpendRatePerDay(@Param("orgId") Long orgId);
	
	String TOTAL_SPEND_ANALYTICS_QUERY ="SELECT SUM(cast(value as INT)) AS sum_values " +
			" FROM cloud_element ce, " +
			" landingzone l , " +
			" department dep, " +
			" organization org, " +
			" jsonb_each_text(ce.cost_json -> 'cost' -> 'YEARLYCOST') AS jb(key, value)    " +
			" where org.id = dep.organization_id " +
			" and dep.id = l.department_id " +
			" and l.id = ce.landingzone_id  " +
			" and cast(jb.key as int) = extract ('year' from current_date) " +
			" and org.id = :orgId ";
	@Query(value = TOTAL_SPEND_ANALYTICS_QUERY, nativeQuery = true)
	Long totalSpendAnalytics(@Param("orgId") Long orgId);

	String CLOUD_WISE_TOTAL_SPEND_QUERY ="select distinct l.cloud,  " +
			"    SUM(cast(value as INT)) AS sum_values,  " +
			" (SUM(cast(value as INT)) * 100) / (  " +
			"    SELECT SUM(cast(jb2.value as INT))  " +
			"    FROM cloud_element ce2,  " +
			"     landingzone l2,  " +
			"     department dep2,  " +
			"     organization org2, " +
			"    jsonb_each_text(ce2.cost_json -> 'cost' -> 'YEARLYCOST') AS jb2(key, value) " +
			"    where cast(jb2.key as int) = extract ('year' from current_date)  " +
			"    AND   org2.id = dep2.organization_id  " +
			"    AND dep2.id = l2.department_id  " +
			"    AND l2.id = ce2.landingzone_id  " +
			"    AND org2.id = :orgId   " +
			"    and l2.cloud = l.cloud   " +
			" ) AS percentage  " +
			"    FROM cloud_element ce,  " +
			"    landingzone l,  " +
			"    department dep,  " +
			"    organization org,  " +
			"    jsonb_each_text(ce.cost_json -> 'cost' -> 'YEARLYCOST') AS jb(key, value) " +
			"    WHERE org.id = dep.organization_id  " +
			"    AND dep.id = l.department_id  " +
			"    AND l.id = ce.landingzone_id  " +
			"    AND org.id = :orgId  " +
			"    AND l.cloud IN (SELECT DISTINCT ce.cloud  " +
			"      FROM landingzone ce, department d, organization o  " +
			"      WHERE o.id = d.organization_id AND d.id = ce.department_id AND o.id = :orgId )  " +
			"    and cast(jb.key as int) = extract ('year' from current_date)     " +
			"    GROUP BY l.cloud ORDER BY l.cloud ASC " +
			"  ";
	@Query(value = CLOUD_WISE_TOTAL_SPEND_QUERY, nativeQuery = true)
	List<CloudElementCloudWiseQueryObj> cloudWiseTotalSpend(@Param("orgId")Long orgId);
	
	
	String CLOUD_WISE_MONTHLY_SPEND_QUERY ="select l.cloud,   " +
			"  TO_CHAR(TO_DATE(jb.key, 'YYYY-MM'), 'Month') AS month,   " +
			"  SUM(CAST(jb.value AS INT)) AS sum_values  " +
			"from  cloud_element ce,  " +
			"  jsonb_each_text(ce.cost_json -> 'cost' -> 'MONTHLYCOST') AS jb(key, value), " +
			"   landingzone l, department dep, organization org  " +
			"where org.id = dep.organization_id  " +
			" and dep.id = l.department_id  " +
			" and l.id = ce.landingzone_id  " +
			" and org.id = :orgId  " +
			" and l.cloud IN (SELECT DISTINCT ce.cloud  " +
			"      FROM landingzone ce, department d, organization o  " +
			"      WHERE o.id = d.organization_id AND d.id = ce.department_id AND o.id = :orgId )  " +
			" and extract ('year' from TO_DATE(jb.key, 'YYYY-MM')) = extract ('year' from current_date)       " +
			"group by l.cloud, TO_CHAR(TO_DATE(jb.key, 'YYYY-MM'), 'Month'), jb.key  " +
			"ORDER BY TO_DATE(jb.key, 'YYYY-MM') ASC, l.cloud asc ";
	@Query(value = CLOUD_WISE_MONTHLY_SPEND_QUERY, nativeQuery = true)
	List<CloudElementCloudWiseMonthlyQueryObj> cloudWiseMonthlySpend(@Param("orgId")Long orgId);
	
	String INFRA_TOPOLOGY_BY_LANDINGZONE_ID_QUERY ="with vpc as( \n" +
			"    select  l.cloud,pe.id,  \n" +
			"            pe.instance_id ,  \n" +
			"            pe.instance_name  \n" +
			"    from product_enclave pe, landingzone l, department d, organization o  \n" +
			"    where pe.landingzone_id = l.id \n" +
			"        and l.department_id = d.id  \n" +
			"        and d.organization_id = o.id and o.id = :orgId and l.id =:landingZoneId), \n" +
			"threeTierObj as ( \n" +
			"    select  p. type , pe.id, \n" +
			"        pe.instance_id ,  \n" +
			"        pe.instance_name , \n" +
			"        count(distinct be.product_id) as product_count, \n" +
			"        SUM(CASE WHEN upper(be.service_type) = upper('Web') THEN 1 ELSE 0 END) AS web_count,  \n" +
			"        SUM(CASE WHEN upper(be.service_type) = upper('App') THEN 1 ELSE 0 END) AS app_count,  \n" +
			"        SUM(CASE WHEN upper(be.service_type) = upper('Data') THEN 1 ELSE 0 END) AS data_count,  \n" +
			"        SUM(CASE WHEN upper(be.service_type) = upper('Auxiliary') THEN 1 ELSE 0 END) as auxiliary_count \n" +
			"    from cloud_element ce, business_element be, product p, product_enclave pe, landingzone l   \n" +
			"    where ce.id = be.cloud_element_id  \n" +
			"        and p.id = be.product_id and pe.id = ce.product_enclave_id  \n" +
			"        and ce.landingzone_id = l.id and l.id = :landingZoneId \n" +
			"        and upper(p.type) = upper('3 Tier')    \n" +
			"    group by p.type ,pe.instance_id, pe.instance_name, pe.id \n" +
			"    ), \n" +
			"soaObj as ( \n" +
			"    select  p.type , pe.id, \n" +
			"        pe.instance_id ,  \n" +
			"        pe.instance_name , \n" +
			"        count(distinct be.product_id) as product_count, \n" +
			"        SUM(CASE WHEN upper(be.service_type) = upper('App') THEN 1 ELSE 0 END) AS app_count,  \n" +
			"        SUM(CASE WHEN upper(be.service_type) = upper('Data') THEN 1 ELSE 0 END) AS data_count,  \n" +
			"        SUM(CASE WHEN upper(be.service_type) NOT IN (upper('App'), upper('Data')) THEN 1 ELSE 0 END) AS other_count \n" +
			"    from cloud_element ce, business_element be, product p, product_enclave pe, landingzone l   \n" +
			"    where ce.id = be.cloud_element_id   \n" +
			"        and p.id = be.product_id and pe.id = ce.product_enclave_id \n" +
			"        and ce.landingzone_id = l.id and l.id = :landingZoneId \n" +
			"        and upper(p.type) = upper('soa')    \n" +
			"    group by p.type,pe.instance_id, pe.instance_name, pe.id \n" +
			"    ) \n" +
			"    select  vpc.id,  \n" +
			"        vpc.instance_id ,  \n" +
			"        vpc.instance_name ,    \n" +
			"        jsonb_build_object( \n" +
			"            'productCount', coalesce(threeTierObj.product_count,0), \n" +
			"            'webCount', coalesce(threeTierObj.web_count,0), \n" +
			"            'appCount', coalesce(threeTierObj.app_count,0), \n" +
			"            'dataCount', coalesce(threeTierObj.data_count,0), \n" +
			"            'auxiliaryCount', coalesce(threeTierObj.auxiliary_count,0) \n" +
			"        ) as three_tier, \n" +
			"        jsonb_build_object(  \n" +
			"            'productCount', coalesce(soaObj.product_count,0),  \n" +
			"            'appCount', coalesce(soaObj.app_count,0),  \n" +
			"            'dataCount', coalesce(soaObj.data_count,0),  \n" +
			"            'otherCount', coalesce(soaObj.other_count,0)) as soa    \n" +
			"    from vpc \n" +
			"        left join threeTierObj on vpc.id = threeTierObj.id   \n" +
			"            and vpc.instance_id = threeTierObj.instance_id  \n" +
			"            and vpc.instance_name = threeTierObj.instance_name \n" +
			"        left join soaObj on vpc.id = soaObj.id   \n" +
			"            and vpc.instance_id = soaObj.instance_id  \n" +
			"            and vpc.instance_name = soaObj.instance_name";
	@Query(value = INFRA_TOPOLOGY_BY_LANDINGZONE_ID_QUERY, nativeQuery = true)
	List<InfraTopologyQueryObj> getInfraTopologyByLandingZoneId(@Param("orgId") Long orgId, @Param("landingZoneId") Long landingZoneId);

	String INFRA_TOPOLOGY_GLOBAL_BY_LANDINGZONE_ID_QUERY ="with vpc as( \n" +
			"    select l.cloud, l.id \n" +
			"\tfrom landingzone l, department d, organization o  \n" +
			"\twhere l.department_id = d.id \n" +
			"            and d.organization_id = o.id \n" +
			"            and o.id = :orgId and l.id =:landingZoneId),\n" +
			"threeTierObj as ( \n" +
			"    select  l.id, p.type, \n" +
			"        count(distinct be.product_id) as product_count, \n" +
			"        SUM(CASE WHEN upper(be.service_type) = upper('Web') THEN 1 ELSE 0 END) AS web_count,  \n" +
			"        SUM(CASE WHEN upper(be.service_type) = upper('App') THEN 1 ELSE 0 END) AS app_count,  \n" +
			"        SUM(CASE WHEN upper(be.service_type) = upper('Data') THEN 1 ELSE 0 END) AS data_count,  \n" +
			"        SUM(CASE WHEN upper(be.service_type) = upper('Auxiliary') THEN 1 ELSE 0 END) as auxiliary_count \n" +
			"    from cloud_element ce, business_element be, product p, landingzone l   \n" +
			"    where  ce.product_enclave_id is null \n" +
			"   \t\t\tand ce.id = be.cloud_element_id\n" +
			"   \t\t\tand ce.landingzone_id = l.id \n" +
			"   \t\t\tand p.id = be.product_id\n" +
			"   \t\t\tand upper(p.type) = upper('3 Tier')\n" +
			"   \t\t\tand l.id = :landingZoneId\n" +
			"    group by l.id, p.type \n" +
			"    ), \n" +
			"soaObj as ( \n" +
			"    select  l.id, p.type, \n" +
			"        count(distinct be.product_id) as product_count, \n" +
			"        SUM(CASE WHEN upper(be.service_type) = upper('App') THEN 1 ELSE 0 END) AS app_count,  \n" +
			"        SUM(CASE WHEN upper(be.service_type) = upper('Data') THEN 1 ELSE 0 END) AS data_count,  \n" +
			"        SUM(CASE WHEN upper(be.service_type) NOT IN (upper('App'), upper('Data')) THEN 1 ELSE 0 END) AS other_count \n" +
			"    from cloud_element ce, business_element be, product p, landingzone l   \n" +
			"    where ce.product_enclave_id is null \n" +
			"        and ce.id = be.cloud_element_id\n" +
			"        and ce.landingzone_id = l.id \n" +
			"        and p.id = be.product_id\n" +
			"        and upper(p.type) = upper('soa')\n" +
			"        and l.id = :landingZoneId\n" +
			"    group by l.id, p.type \n" +
			"    ) \n" +
			"    select  null as id,  \n" +
			"         null as instance_id ,  \n" +
			"        null as instance_name ,    \n" +
			"        jsonb_build_object( \n" +
			"            'productCount', coalesce(threeTierObj.product_count,0), \n" +
			"            'webCount', coalesce(threeTierObj.web_count,0), \n" +
			"            'appCount', coalesce(threeTierObj.app_count,0), \n" +
			"            'dataCount', coalesce(threeTierObj.data_count,0), \n" +
			"            'auxiliaryCount', coalesce(threeTierObj.auxiliary_count,0) \n" +
			"        ) as three_tier, \n" +
			"        jsonb_build_object(  \n" +
			"            'productCount', coalesce(soaObj.product_count,0),  \n" +
			"            'appCount', coalesce(soaObj.app_count,0),  \n" +
			"            'dataCount', coalesce(soaObj.data_count,0),  \n" +
			"            'otherCount', coalesce(soaObj.other_count,0)) as soa    \n" +
			"    from vpc \n" +
			"        left join threeTierObj on vpc.id = threeTierObj.id   \n" +
			"        left join soaObj on vpc.id = soaObj.id";
	@Query(value = INFRA_TOPOLOGY_GLOBAL_BY_LANDINGZONE_ID_QUERY, nativeQuery = true)
	List<InfraTopologyQueryObj> getInfraTopologyGlobalByLandingZoneId(@Param("orgId") Long orgId, @Param("landingZoneId") Long landingZoneId);

	String INFRA_TOPOLOGY_CATEGORY_WISE_VIEW_QUERY ="select ce.element_type, jsonb_build_object() as metadata, ce.category, ce.db_category_id , count(ce.element_type) as total_record  \n" +
			"from cloud_element ce  \n" +
			"inner join product_enclave pe on ce.product_enclave_id = pe.id \n" +
			"inner join landingzone l on ce.landingzone_id = l.id  \n" +
			"INNER JOIN department d ON l.department_id = d.id\n" +
			"INNER JOIN organization o ON d.organization_id = o.id\n" +
			"where ce.product_enclave_id = pe.id and ce.landingzone_id = l.id   \n" +
			"and pe.instance_id = :productEnclaveInstanceId \n" +
			"and l.landing_zone = :landingZone \n" +
			"and o.id = :orgId \n" +
			"group by ce.product_enclave_id, ce.category, ce.element_type, ce.db_category_id  \n" +
			"order by ce.element_type asc";
	@Query(value = INFRA_TOPOLOGY_CATEGORY_WISE_VIEW_QUERY, nativeQuery = true)
	List<InfraTopologyCategoryWiseViewQueryObj> getInfraTopologyCategoryWiseView(@Param("orgId") Long orgId, @Param("landingZone") String landingZone, @Param("productEnclaveInstanceId") String productEnclaveInstanceId);

	String INFRA_TOPOLOGY_GLOBAL_SERVICE_CATEGORY_WISE_VIEW_QUERY =" select ce.element_type, jsonb_build_object() as metadata, count(ce.element_type) as total_record   \n" +
			"    from cloud_element ce   \n" +
			"    inner join landingzone l on ce.landingzone_id = l.id   \n" +
			"    INNER JOIN department d ON l.department_id = d.id \n" +
			"    INNER JOIN organization o ON d.organization_id = o.id \n" +
			" where ce.product_enclave_id is null \n" +
			"    and ce.landingzone_id = l.id    \n" +
			"    and l.id = :landingZoneId  \n" +
			"    and o.id = :orgId  \n" +
			" group by ce.element_type\n" +
			" order by ce.element_type asc";
	@Query(value = INFRA_TOPOLOGY_GLOBAL_SERVICE_CATEGORY_WISE_VIEW_QUERY, nativeQuery = true)
	List<InfraTopologyGlobalServiceCategoryWiseViewQueryObj> getInfraTopologyGlobalServiceCategoryWiseView(@Param("orgId") Long orgId, @Param("landingZoneId") Long landingZoneId);

	String INFRA_TOPOLOGY_CLOUD_ELEMENT_QUERY ="select ce.id, ce.element_type, ce.instance_id, ce.instance_name, ce.category  \n" +
			"from cloud_element ce  \n" +
			"inner join product_enclave pe on ce.product_enclave_id = pe.id \n" +
			"inner join landingzone l on ce.landingzone_id = l.id  \n" +
			"INNER JOIN department d ON l.department_id = d.id \n" +
			"INNER JOIN organization o ON d.organization_id = o.id \n" +
			"where ce.product_enclave_id = pe.id and ce.landingzone_id = l.id \n" +
			"and pe.instance_id = :productEnclaveInstanceId \n" +
			"and l.landing_zone = :landingZone \n" +
			"and o.id = :orgId \n" +
			"order by ce.element_type asc ";
	@Query(value = INFRA_TOPOLOGY_CLOUD_ELEMENT_QUERY, nativeQuery = true)
	List<InfraTopologyCloudElementQueryObj> getInfraTopologyCloudElementList(@Param("orgId") Long orgId, @Param("landingZone") String landingZone, @Param("productEnclaveInstanceId") String productEnclaveInstanceId);

	String INFRA_TOPOLOGY_3_TIER_STATS_QUERY ="SELECT \n" +
			"    coalesce (SUM(CASE WHEN upper(be.service_type) = upper('Web') THEN 1 ELSE 0 END),0) AS web_count,\n" +
			"    coalesce (SUM(CASE WHEN upper(be.service_type) = upper('App') THEN 1 ELSE 0 END),0) AS app_count,\n" +
			"    coalesce (SUM(CASE WHEN upper(be.service_type) = upper('Data') THEN 1 ELSE 0 END),0) AS data_count,\n" +
			"    coalesce (SUM(CASE WHEN upper(be.service_type) = upper('Auxiliary') THEN 1 ELSE 0 END),0) as auxiliary_count \n" +
			"FROM landingzone l\n" +
			"INNER JOIN department d ON l.department_id = d.id\n" +
			"INNER JOIN organization o ON d.organization_id = o.id\n" +
			"LEFT JOIN cloud_element ce ON ce.landingzone_id = l.id\n" +
			"LEFT JOIN product_enclave pe ON pe.landingzone_id  = l.id AND pe.department_id = d.id\n" +
			"LEFT JOIN business_element be ON be.cloud_element_id = ce.id\n" +
			"LEFT JOIN product p ON be.product_id = p.id \n" +
			"where\n" +
			"\to.id = d.organization_id\n" +
			"    AND l.department_id = d.id\n" +
			"    AND upper(p.\"type\") = upper('3 Tier')\n" +
			"    and o.id = :orgId \n" +
			"    AND l.landing_zone = :landingZone \n" +
			"    and pe.instance_id = :productEnclaveInstanceId \n" +
			"    and ce.instance_id = :cloudElementInstanceId ";
	@Query(value = INFRA_TOPOLOGY_3_TIER_STATS_QUERY, nativeQuery = true)
	InfraTopology3TierStatsQueryObj getInfraTopology3TierStats(@Param("orgId") Long orgId, @Param("landingZone") String landingZone, @Param("productEnclaveInstanceId") String productEnclaveInstanceId, @Param("cloudElementInstanceId") String cloudElementInstanceId);

	String INFRA_TOPOLOGY_SOA_STATS_QUERY ="SELECT\n" +
			"    coalesce (SUM(CASE WHEN upper(be.service_type) = upper('App') THEN 1 ELSE 0 END),0) AS app_count,\n" +
			"    coalesce (SUM(CASE WHEN upper(be.service_type) = upper('Data') THEN 1 ELSE 0 END),0) AS data_count,\n" +
			"    coalesce (SUM(CASE WHEN upper(be.service_type) NOT IN (upper('App'), upper('Data')) THEN 1 ELSE 0 END),0) AS other_count\n" +
			"FROM landingzone l\n" +
			"INNER JOIN department d ON l.department_id = d.id\n" +
			"INNER JOIN organization o ON d.organization_id = o.id\n" +
			"LEFT JOIN cloud_element ce ON ce.landingzone_id = l.id\n" +
			"LEFT JOIN product_enclave pe ON pe.landingzone_id  = l.id AND pe.department_id = l.department_id \n" +
			"LEFT JOIN business_element be ON be.cloud_element_id = ce.id\n" +
			"LEFT JOIN product p ON be.product_id = p.id \n" +
			"where\n" +
			"\to.id = d.organization_id\n" +
			"    AND l.department_id = d.id\n" +
			"    AND upper(p.\"type\") = upper('SOA')\n" +
			"    and o.id = :orgId \n" +
			"    AND l.landing_zone = :landingZone \n" +
			"    and pe.instance_id = :productEnclaveInstanceId \n" +
			"    and ce.instance_id = :cloudElementInstanceId ";
	@Query(value = INFRA_TOPOLOGY_SOA_STATS_QUERY, nativeQuery = true)
	InfraTopologySOAStatsQueryObj getInfraTopologySOAStats(@Param("orgId") Long orgId, @Param("landingZone") String landingZone, @Param("productEnclaveInstanceId") String productEnclaveInstanceId, @Param("cloudElementInstanceId") String cloudElementInstanceId);


	String MONTHLY_STATISTICS_QUERY = " SELECT * FROM (  " +
			" WITH monthly_costs AS (  " +
			"  SELECT  " +
			" TO_CHAR(TO_DATE(jb.key, 'YYYY-MM'), 'Month') AS month,  " +
			" SUM(CAST(jb.value AS INT)) AS sum_values  " +
			"  FROM  " +
			" cloud_element ce,  " +
			" jsonb_each_text(ce.cost_json -> 'cost' -> 'MONTHLYCOST') AS jb(key, value),  " +
			" landingzone l, department dep, organization org  " +
			"  WHERE  " +
			" org.id = dep.organization_id  " +
			" AND dep.id = l.department_id  " +
			" AND l.id = ce.landingzone_id  " +
			" AND org.id  = :orgId " +
			" AND EXTRACT('year' FROM TO_DATE(jb.key, 'YYYY-MM')) = EXTRACT('year' FROM CURRENT_DATE)  " +
			"  GROUP BY TO_CHAR(TO_DATE(jb.key, 'YYYY-MM'), 'Month'), jb.key  " +
			"  ORDER BY TO_DATE(jb.key, 'YYYY-MM') DESC  " +
			"  LIMIT 3  " +
			" )  " +
			" SELECT  " +
			"  'Total Sum of Value' AS month,  " +
			"  SUM(sum_values) AS sum_all_values  " +
			" FROM  " +
			"  monthly_costs  " +
			"    " +
			" UNION ALL  " +
			"    " +
			" SELECT  " +
			"  month,  " +
			"  sum_values  " +
			" FROM (  " +
			"  SELECT  " +
			" TO_CHAR(TO_DATE(jb.key, 'YYYY-MM'), 'Month') AS month,  " +
			" SUM(CAST(jb.value AS INT)) AS sum_values  " +
			"  FROM  " +
			" cloud_element ce,  " +
			" jsonb_each_text(ce.cost_json -> 'cost' -> 'MONTHLYCOST') AS jb(key, value),  " +
			" landingzone l, department dep, organization org  " +
			"  WHERE  " +
			" org.id = dep.organization_id  " +
			" AND dep.id = l.department_id  " +
			" AND l.id = ce.landingzone_id  " +
			" AND org.id  = :orgId " +
			" AND EXTRACT('year' FROM TO_DATE(jb.key, 'YYYY-MM')) = EXTRACT('year' FROM CURRENT_DATE)  " +
			"  GROUP BY TO_CHAR(TO_DATE(jb.key, 'YYYY-MM'), 'Month'), jb.key  " +
			"  ORDER BY TO_DATE(jb.key, 'YYYY-MM') DESC  " +
			"  LIMIT 3  " +
			" ) subquery  " +
			" ) final_query ";
	@Query(value = MONTHLY_STATISTICS_QUERY, nativeQuery = true)
	List<MonthlyStatisticsQueryObj> monthlyStatisticsQueryObj(@Param("orgId")Long orgId);

	String TOTAL_BUdGET_QUERY = "WITH monthly_costs AS ( \n" +
			"  SELECT SUM(cast(value as INT)) AS sum_values   \n" +
			"     FROM cloud_element ce,   \n" +
			"     landingzone l, \n" +
			"     department dep,   \n" +
			"     organization org,   \n" +
			"     jsonb_each_text(ce.cost_json -> 'cost' -> 'YEARLYCOST') AS jb(key, value)   \n" +
			"     where org.id = dep.organization_id   \n" +
			"\t and dep.id = l.department_id   \n" +
			"\t and l.id = ce.landingzone_id \n" +
			"     and cast(jb.key as int) = extract ('year' from current_date)   \n" +
			"     and org.id = :orgId   \n" +
			" ),  \n" +
			" budget_sum AS ( \n" +
			"\t SELECT b.allocated_budget AS total_sum \n" +
			"\t FROM budget b \n" +
			"\t JOIN organization org ON b.organization_id = org.id \n" +
			"\t WHERE org.id = :orgId  \n" +
			" ) \n" +
			" SELECT \n" +
			"\t budget_sum.total_sum AS total_budget, \n" +
			"\t monthly_costs.sum_values AS budget_used, \n" +
			"\t budget_sum.total_sum  - monthly_costs.sum_values AS remaining_budget, \n" +
			"\t cast((budget_sum.total_sum  - monthly_costs.sum_values ) as float) / budget_sum.total_sum * 100 AS remaining_budget_percentage \n" +
			"\t FROM monthly_costs, budget_sum ";
	@Query(value = TOTAL_BUdGET_QUERY, nativeQuery = true)
	TotalBudgetQueryObj getTotalBudget(@Param("orgId") Long orgId);

	String PRODUCT_WISE_COST_NON_ASSOCIATE_QUERY = "WITH product_sum AS (\r\n"
			+ "			 select distinct p.\"name\" as name,\r\n"
			+ "			  SUM(cast(value as INT)) as total\r\n"
			+ "			 from cloud_element ce,\r\n"
			+ "			business_element be,\r\n"
			+ "			product p ,\r\n"
			+ "			department d, organization o ,\r\n"
			+ "			jsonb_array_elements(ce.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj),\r\n"
			+ "			 jsonb_each_text(ce.cost_json -> 'cost' -> 'DAILYCOST') AS jb(key, value)\r\n"
			+ "			where   cast (c.obj ->> 'serviceId' as int) = be.id\r\n"
			+ "			and p.id = be.product_id\r\n"
			+ "			and p.department_id = d.id\r\n"
			+ "			and d.organization_id = o.id\r\n"
			+ "			and  be.cloud_element_id is null\r\n"
			+ "			and o.id = :orgId\r\n"
			+ "			GROUP by p.name\r\n"
			+ "			  )\r\n"
			+ "			  select name, total, (total * 100.0) / (select SUM(total) from product_sum) as percentage\r\n"
			+ "			  from\r\n"
			+ "			  product_sum\r\n"
			+ "			UNION ALL\r\n"
			+ "			  select 'Cumulative Total', SUM(total) as total_sum, null as percentage\r\n"
			+ "			  from\r\n"
			+ "			  product_sum ";
	@Query(value = PRODUCT_WISE_COST_NON_ASSOCIATE_QUERY, nativeQuery = true)
	List<CostAnalyticQueryObj> getProductWiseCostNonAssociate(@Param("orgId") Long orgId);
	
	String PRODUCT_WISE_COST_ASSOCIATE_QUERY = "with product_sum as (\n" +
			"\t\tselect 'Cumulative Total' as name, SUM(cast(value as INT)) as total, null as percentage\n" +
			"\t\tfrom cloud_element ce, business_element be, product p, department d, organization o,  \n" +
			"\t\t\t jsonb_array_elements(ce.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj),\n" +
			"\t\t\t jsonb_each_text(ce.cost_json -> 'cost' -> 'MONTHLYCOST') as jb(key, value)\n" +
			"\t\twhere  cast (c.obj ->> 'serviceId' as int) = be.id\n" +
			"\t\t\tand p.id = be.product_id\n" +
			"\t\t\tand p.department_id = d.id\n" +
			"\t\t\tand d.organization_id = o.id\n" +
			"\t\t\tand cast(substring(jb.key, 6) as int) = extract ('month' from current_date) \n" +
			"\t\t\tand o.id = :orgId\n" +
			"\t),\n" +
			"prev_sum as (\n" +
			"\tselect p.\"name\" as name, sum(cast (jb.value as int)) as total\n" +
			"\tfrom\n" +
			"\t\tcloud_element ce,\n" +
			"\t\tbusiness_element be,\n" +
			"\t\tproduct p ,\n" +
			"\t\tdepartment d,\n" +
			"\t\torganization o ,\n" +
			"\t\tjsonb_each_text(ce.cost_json -> 'cost' -> 'MONTHLYCOST') as jb(key, value),\n" +
			"\t\tjsonb_array_elements(ce.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj, pos)\n" +
			"\twhere\n" +
			"\t\tce.hosted_services is not null and ce.hosted_services != 'null'\n" +
			"\t\tand be.id = cast(c.obj ->> 'serviceId' as int)\n" +
			"\t\tand be.product_id = p.id\n" +
			"\t\tand p.department_id = d.id\n" +
			"\t\tand d.organization_id = o.id\n" +
			"\t\tand o.id = :orgId\n" +
			"\t\tand cast(substring(jb.key, 6) as int) = extract ('month' from current_date - interval '1 month')\n" +
			"\t\tgroup by p.name\n" +
			"\t)\t\n" +
			"select prvs.name, prvs.total, round((prvs.total/(select sum(prvs2.total) from prev_sum prvs2)) * 100, 2) as percentage\n" +
			"from prev_sum prvs group by prvs.name,prvs.total\t\n" +
//			"union all\n" +
//			"select 'Previous Sum' as name, (select sum(prvs2.total) from prev_sum prvs2) as total, null as percentage\n" +
			"union all\n" +
			"select ps.name, ps.total, round(((ps.total - sum(prev_sum.total))/ps.total) * 100,2) as percentage from product_sum ps, prev_sum\n" +
			"group by ps.name, ps.total\n";
	@Query(value = PRODUCT_WISE_COST_ASSOCIATE_QUERY, nativeQuery = true)
	List<CostAnalyticQueryObj> getProductWiseCostAssociate(@Param("orgId") Long orgId);


	String PRODUCTION_VS_OTHERS_NON_ASSOCIATE_QUERY = "with product_sum as ( \r\n"
			+ "			 select distinct pe.\"name\" as name, \r\n"
			+ "			  SUM(cast(value as INT)) as total \r\n"
			+ "			 from \r\n"
			+ "			  cloud_element ce,\r\n"
			+ "			  business_element be,\r\n"
			+ "			  product p ,\r\n"
			+ "			  product_env pe ,\r\n"
			+ "			  department d, organization o ,\r\n"
			+ "			  jsonb_array_elements(ce.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj),\r\n"
			+ "			  jsonb_each_text(ce.cost_json -> 'cost' -> 'MONTHLYCOST') AS jb(key, value)\r\n"
			+ "			  where \r\n"
			+ "			cast (c.obj ->> 'serviceId' as int) = be.id\r\n"
			+ "			and p.id = be.product_id\r\n"
			+ "			and p.department_id = d.id\r\n"
			+ "			and d.organization_id = o.id\r\n"
			+ "			and p.id = pe.product_id \r\n"
			+ "		    and  be.cloud_element_id is null\r\n"
			+ "			and o.id = :orgId\r\n"
			+ "			and extract('year' from TO_DATE(jb.key, 'YYYY-MM')) = extract('year' from CURRENT_DATE) and pe.name = 'PROD'\r\n"
			+ "			 GROUP by pe.\"name\" \r\n"
			+ "			), \r\n"
			+ "			\r\n"
			+ "			other_sum as ( \r\n"
			+ "			 select distinct pe.\"name\" as name, \r\n"
			+ "			  SUM(cast(value as INT)) as total \r\n"
			+ "			 from \r\n"
			+ "			  cloud_element ce,\r\n"
			+ "			  business_element be,\r\n"
			+ "			  product p ,\r\n"
			+ "			  product_env pe ,\r\n"
			+ "			  department d, organization o ,\r\n"
			+ "			  jsonb_array_elements(ce.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj),\r\n"
			+ "			  jsonb_each_text(ce.cost_json -> 'cost' -> 'MONTHLYCOST') AS jb(key, value)\r\n"
			+ "			  where \r\n"
			+ "			cast (c.obj ->> 'serviceId' as int) = be.id\r\n"
			+ "			and p.id = be.product_id\r\n"
			+ "			and p.department_id = d.id\r\n"
			+ "			and d.organization_id = o.id\r\n"
			+ "			and p.id = pe.product_id \r\n"
			+ "			and  be.cloud_element_id is null\r\n"
			+ "			and o.id = :orgId\r\n"
			+ "			and extract('year' from TO_DATE(jb.key, 'YYYY-MM')) = extract('year' from CURRENT_DATE) and pe.name != 'PROD'\r\n"
			+ "			 GROUP by pe.\"name\" \r\n"
			+ "			), \r\n"
			+ "			total_sum as ( \r\n"
			+ "			select SUM(total) as total_sum from  \r\n"
			+ "			 (  select SUM(total) as total from product_sum \r\n"
			+ "			   union all \r\n"
			+ "			  select SUM(total) as total from other_sum \r\n"
			+ "			 ) as totals \r\n"
			+ "			) \r\n"
			+ "			select name, total, ROUND((total / ( select total_sum from total_sum)) * 100, 7) as percentage \r\n"
			+ "			from \r\n"
			+ "			( \r\n"
			+ "			 select 'PROD' as name, SUM(product_sum.total) as total \r\n"
			+ "			 from product_sum \r\n"
			+ "			  union all \r\n"
			+ "			 select 'others' as name, SUM(other_sum.total) as total \r\n"
			+ "			 from other_sum \r\n"
			+ "			) as subquery \r\n"
			+ "			union all \r\n"
			+ "			 \r\n"
			+ "			select 'Cumulative Total' as name, \r\n"
			+ "			( select SUM(total) from product_sum )  \r\n"
			+ "			 +  \r\n"
			+ "			( select SUM(total) from other_sum ) as total_sum, \r\n"
			+ "			null";
	@Query(value = PRODUCTION_VS_OTHERS_NON_ASSOCIATE_QUERY, nativeQuery = true)
	List<CostAnalyticQueryObj> getProductionVsOthersCostNonAssociate(@Param("orgId") Long orgId);

	
	String PRODUCTION_VS_OTHERS_ASSOCIATE_QUERY = "	\r\n"
			+ "with product_sum as ( \r\n"
			+ "			 select distinct pe.\"name\" as name, \r\n"
			+ "			  SUM(cast(value as INT)) as total \r\n"
			+ "			 from \r\n"
			+ "			  cloud_element ce,\r\n"
			+ "			  business_element be,\r\n"
			+ "			  product p ,\r\n"
			+ "			  product_env pe ,\r\n"
			+ "			  department d, organization o ,\r\n"
			+ "			  jsonb_array_elements(ce.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj),\r\n"
			+ "			  jsonb_each_text(ce.cost_json -> 'cost' -> 'MONTHLYCOST') AS jb(key, value)\r\n"
			+ "			  where \r\n"
			+ "			cast (c.obj ->> 'serviceId' as int) = be.id\r\n"
			+ "			and p.id = be.product_id\r\n"
			+ "			and p.department_id = d.id\r\n"
			+ "			and d.organization_id = o.id\r\n"
			+ "			and p.id = pe.product_id \r\n"
			+ "			and ce.id =  be.cloud_element_id \r\n"
			+ "			and o.id = :orgId\r\n"
			+ "			and extract('year' from TO_DATE(jb.key, 'YYYY-MM')) = extract('year' from CURRENT_DATE) and pe.name = 'PROD'\r\n"
			+ "			 GROUP by pe.\"name\" \r\n"
			+ "			), \r\n"
			+ "			\r\n"
			+ "			other_sum as ( \r\n"
			+ "			 select distinct pe.\"name\" as name, \r\n"
			+ "			  SUM(cast(value as INT)) as total \r\n"
			+ "			 from \r\n"
			+ "			  cloud_element ce,\r\n"
			+ "			  business_element be,\r\n"
			+ "			  product p ,\r\n"
			+ "			  product_env pe ,\r\n"
			+ "			  department d, organization o ,\r\n"
			+ "			  jsonb_array_elements(ce.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj),\r\n"
			+ "			  jsonb_each_text(ce.cost_json -> 'cost' -> 'MONTHLYCOST') AS jb(key, value)\r\n"
			+ "			  where \r\n"
			+ "			cast (c.obj ->> 'serviceId' as int) = be.id\r\n"
			+ "			and p.id = be.product_id\r\n"
			+ "			and p.department_id = d.id\r\n"
			+ "			and d.organization_id = o.id\r\n"
			+ "			and p.id = pe.product_id \r\n"
			+ "			and ce.id =  be.cloud_element_id \r\n"
			+ "			and o.id = :orgId\r\n"
			+ "			and extract('year' from TO_DATE(jb.key, 'YYYY-MM')) = extract('year' from CURRENT_DATE) and pe.name != 'PROD'\r\n"
			+ "			 GROUP by pe.\"name\" \r\n"
			+ "			), \r\n"
			+ "			total_sum as ( \r\n"
			+ "			select SUM(total) as total_sum from  \r\n"
			+ "			 (  select SUM(total) as total from product_sum \r\n"
			+ "			   union all \r\n"
			+ "			  select SUM(total) as total from other_sum \r\n"
			+ "			 ) as totals \r\n"
			+ "			) \r\n"
			+ "			select name, total, ROUND((total / ( select total_sum from total_sum)) * 100, 7) as percentage \r\n"
			+ "			from \r\n"
			+ "			( \r\n"
			+ "			 select 'PROD' as name, SUM(product_sum.total) as total \r\n"
			+ "			 from product_sum \r\n"
			+ "			  union all \r\n"
			+ "			 select 'others' as name, SUM(other_sum.total) as total \r\n"
			+ "			 from other_sum \r\n"
			+ "			) as subquery \r\n"
			+ "			union all \r\n"
			+ "			 \r\n"
			+ "			select 'Cumulative Total' as name, \r\n"
			+ "			( select SUM(total) from product_sum )  \r\n"
			+ "			 +  \r\n"
			+ "			( select SUM(total) from other_sum ) as total_sum, \r\n"
			+ "			null";
	@Query(value = PRODUCTION_VS_OTHERS_NON_ASSOCIATE_QUERY, nativeQuery = true)
	List<CostAnalyticQueryObj> getProductionVsOthersCostAssociate(@Param("orgId") Long orgId);

	
	String SERVICE_TYPE_WISE_COST_NON_ASSOCIATE_QUERY = " WITH labels AS (   \r\n"
			+ "			  select   \r\n"
			+ "			   ce.category as name,   \r\n"
			+ "			   SUM(cast(value as INT)) as total   \r\n"
			+ "			  from   \r\n"
			+ "			   cloud_element ce, department d, landingzone l,business_element be, \r\n"
			+ "			   organization org,   \r\n"
			+ "			   JSONB_EACH_TEXT(ce.cost_json  -> 'cost' -> 'MONTHLYCOST') as jb(key, value)   \r\n"
			+ "			  where   \r\n"
			+ "			   d.organization_id = org.id  \r\n"
			+ "			   and l.department_id = d.id  \r\n"
			+ "			   and  be.cloud_element_id is null\r\n"
			+ "			   and org.id = :orgId   \r\n"
			+ "			   and extract('year' from TO_DATE(jb.key, 'YYYY-MM')) = extract('year' from CURRENT_DATE)   \r\n"
			+ "			  group by ce.category   \r\n"
			+ "			 )   \r\n"
			+ "			 select   \r\n"
			+ "			  name,   \r\n"
			+ "			  total,   \r\n"
			+ "			  (total * 100.0) / ( select SUM(total) from labels) as percentage   \r\n"
			+ "			 from   \r\n"
			+ "			  labels   \r\n"
			+ "			UNION ALL   \r\n"
			+ "			select   \r\n"
			+ "			 'Cumulative Total',   \r\n"
			+ "			 SUM(total) as total_sum,   \r\n"
			+ "			 null as percentage   \r\n"
			+ "			from labels   ";
	@Query(value = SERVICE_TYPE_WISE_COST_NON_ASSOCIATE_QUERY, nativeQuery = true)
	List<CostAnalyticQueryObj> getServiceTypeWiseCostNonAssociate(@Param("orgId") Long orgId);
	
	String SERVICE_TYPE_WISE_COST_ASSOCIATE_QUERY = " WITH labels AS (   \r\n"
			+ "			  select   \r\n"
			+ "			   ce.category as name,   \r\n"
			+ "			   SUM(cast(value as INT)) as total   \r\n"
			+ "			  from   \r\n"
			+ "			   cloud_element ce, department d, landingzone l,business_element be, \r\n"
			+ "			   organization org,   \r\n"
			+ "			   JSONB_EACH_TEXT(ce.cost_json  -> 'cost' -> 'MONTHLYCOST') as jb(key, value)   \r\n"
			+ "			  where   \r\n"
			+ "			   d.organization_id = org.id  \r\n"
			+ "			   and l.department_id = d.id  \r\n"
			+ "			   and  ce.id=be.cloud_element_id \r\n"
			+ "			   and org.id = :orgId   \r\n"
			+ "			   and extract('year' from TO_DATE(jb.key, 'YYYY-MM')) = extract('year' from CURRENT_DATE)   \r\n"
			+ "			  group by ce.category   \r\n"
			+ "			 )   \r\n"
			+ "			 select   \r\n"
			+ "			  name,   \r\n"
			+ "			  total,   \r\n"
			+ "			  (total * 100.0) / ( select SUM(total) from labels) as percentage   \r\n"
			+ "			 from   \r\n"
			+ "			  labels   \r\n"
			+ "			UNION ALL   \r\n"
			+ "			select   \r\n"
			+ "			 'Cumulative Total',   \r\n"
			+ "			 SUM(total) as total_sum,   \r\n"
			+ "			 null as percentage   \r\n"
			+ "			from labels   ";
	@Query(value = SERVICE_TYPE_WISE_COST_ASSOCIATE_QUERY, nativeQuery = true)
	List<CostAnalyticQueryObj> getServiceTypeWiseCostAssociate(@Param("orgId") Long orgId);
	
	

	String DATA_GENERATOR = "SELECT b.obj ->> 'name' as entity_type, b.obj ->> :entity AS cost_json \r\n"
			+ "FROM cloud_element ce\r\n" + "JOIN organization org ON org.id = :orgId \r\n"
			+ "CROSS JOIN JSONB_ARRAY_ELEMENTS(ce.cost_json -> 'elementLists') WITH ORDINALITY b(obj, pos)\r\n"
			+ "WHERE b.obj ->> 'name' = :elementName ";

	@Query(value = DATA_GENERATOR, nativeQuery = true)
	List<CostBillingQueryObj> getDataGenerator(@Param("orgId") Long orgId,
			@Param(value = "elementName") String elementName, @Param("entity") String entity);

	String DATA_GENERATOR_ORG_BILLING = "SELECT \r\n"
			+ "		 CAST(ce.hardware_location ->> 'landingZone' AS BIGINT) as landing_zone,\r\n"
			+ "		   b.obj ->> 'name'  as element_name,\r\n" + "		    b.obj ->> UPPER(:entity) AS  cost_json \r\n"
			+ "		FROM cloud_element ce\r\n" + "		JOIN organization org ON org.id =:orgId \r\n"
			+ "		CROSS JOIN JSONB_ARRAY_ELEMENTS(ce.cost_json -> 'elementLists') WITH ORDINALITY b(obj, pos) ";

	@Query(value = DATA_GENERATOR_ORG_BILLING, nativeQuery = true)
	List<CostBillingQueryObj> getDataGeneratorOrgBilling(@Param("orgId") Long orgId, @Param("entity") String entity);

	String DATA_GENERATOR_ORG_ELMENT_NAME_BILLING = "SELECT \r\n"
			+ "CAST(ce.hardware_location ->> 'landingZone' AS BIGINT) as landing_zone,\r\n"
			+ "    b.obj ->> 'name' as element_name,\r\n"
			+ "    b.obj ->>  UPPER(:entity) AS cost_json\r\n"
			+ "FROM cloud_element ce\r\n"
			+ "JOIN organization org ON org.id = :orgId \r\n"
			+ "CROSS JOIN JSONB_ARRAY_ELEMENTS(ce.cost_json -> 'elementLists') WITH ORDINALITY b(obj, pos)\r\n"
			+ "WHERE b.obj ->> 'name' = :elementName ";

	@Query(value = DATA_GENERATOR_ORG_ELMENT_NAME_BILLING, nativeQuery = true)
	List<CostBillingQueryObj> getOrgAndElementNameBilling(@Param("orgId") Long orgId, @Param("entity") String entity,
			@Param("elementName") String elementName);

	String DATA_GENERATOR_ORG_LANDING_ZONE_BILLING = "SELECT \r\n"
			+ " CAST(ce.hardware_location ->> 'landingZone' AS BIGINT) as landing_zone,\r\n"
			+ "    b.obj ->> 'name' as element_name,\r\n" + "   b.obj ->>  UPPER(:entity) AS cost_json\r\n"
			+ "FROM cloud_element ce\r\n" + "JOIN organization org ON org.id = :orgId \r\n"
			+ "CROSS JOIN JSONB_ARRAY_ELEMENTS(ce.cost_json -> 'elementLists') WITH ORDINALITY b(obj, pos)\r\n"
			+ "WHERE CAST(ce.hardware_location ->> 'landingZone' AS BIGINT) = :landingZone ";

	@Query(value = DATA_GENERATOR_ORG_LANDING_ZONE_BILLING, nativeQuery = true)
	List<CostBillingQueryObj> getOrgAndLandingZoneBilling(@Param("orgId") Long orgId, @Param("entity") String entity,
			@Param("landingZone") Long landingZone);

	String DATA_GENERATOR_ORG_LANDING_ZONE_ELEMENT_BILLING = "SELECT \r\n"
			+ " CAST(ce.hardware_location ->> 'landingZone' AS BIGINT) as landing_zone,\r\n"
			+ "    b.obj ->> 'name' as element_name,\r\n"
			+ "    b.obj ->>  UPPER(:entity) AS cost_json \r\n"
			+ "FROM cloud_element ce\r\n"
			+ "JOIN organization org ON org.id = :orgId \r\n"
			+ "CROSS JOIN JSONB_ARRAY_ELEMENTS(ce.cost_json -> 'elementLists') WITH ORDINALITY b(obj, pos)\r\n"
			+ "WHERE b.obj ->> 'name' = :elementName \r\n"
			+ "and CAST(ce.hardware_location ->> 'landingZone' AS BIGINT) = :landingZone ";

	@Query(value = DATA_GENERATOR_ORG_LANDING_ZONE_ELEMENT_BILLING, nativeQuery = true)
	List<CostBillingQueryObj> getOrgAndElementNameAndLandingZoneBilling(@Param("orgId") Long orgId,
			@Param("entity") String entity, @Param("landingZone") Long landingZone,
			@Param("elementName") String elementName);

	String SLA_COST_NON_ASSOCIATE_QUERY="select p.\"name\", cast (floor(random() * 100 + 1) as int) as performance,\r\n"
			+ "cast (floor(random() * 100 + 1) as int) as availability,\r\n"
			+ "cast (floor(random() * 100 + 1) as int) as reliability,\r\n"
			+ "cast (floor(random() * 100 + 1) as int) as security,\r\n"
			+ "cast (floor(random() * 100 + 1) as int) as end_usage\r\n"
			+ "from product p where p.organization_id = :orgId ";
	@Query(value = SLA_COST_NON_ASSOCIATE_QUERY, nativeQuery = true)
	List<SlaAnalyticQueryObj> getSlaWiseCostNonAssociate(@Param("orgId") Long orgId);
	
	String SLA_COST_ASSOCIATE_QUERY="select p.\"name\", cast (floor(random() * 100 + 1) as int) as performance,\r\n"
			+ "cast (floor(random() * 100 + 1) as int) as availability,\r\n"
			+ "cast (floor(random() * 100 + 1) as int) as reliability,\r\n"
			+ "cast (floor(random() * 100 + 1) as int) as security,\r\n"
			+ "cast (floor(random() * 100 + 1) as int) as end_usage\r\n"
			+ "from product p where p.organization_id = :orgId ";
	@Query(value = SLA_COST_ASSOCIATE_QUERY, nativeQuery = true)
	List<SlaAnalyticQueryObj> getSlaWiseCostAssociate(@Param("orgId") Long orgId);

	String APPLICATION_TOPOLOGY_QUERY=" select l.id as landing_zone_id, l.landing_zone, d.id as department_id, p.name as application, d.name as lob, pe.name as environment, p.type as app_type, \n" +
			"  jsonb_build_object( \n" +
			"    'location', 'US-East EC2-6523', " +
			"    'performance', cast (floor(random() * 100 + 1) as int),  \n" +
			"    'availability', cast (floor(random() * 100 + 1) as int), \n" +
			"    'security', cast (floor(random() * 100 + 1) as int), \n" +
			"    'dataProtection', cast (floor(random() * 100 + 1) as int), \n" +
			"    'userExp', cast (floor(random() * 100 + 1) as int) \n" +
			"    ) as sle, \n" +
			"  jsonb_build_object( \n" +
			"    'location', 'US-East EC2-6523', " +
			"    'performance', cast (floor(random() * 100 + 1) as int),  \n" +
			"    'availability', cast (floor(random() * 100 + 1) as int), \n" +
			"    'security', cast (floor(random() * 100 + 1) as int), \n" +
			"    'dataProtection', cast (floor(random() * 100 + 1) as int), \n" +
			"    'userExp', cast (floor(random() * 100 + 1) as int) \n" +
			"    ) as end_usage, " +
			"  jsonb_build_object( \n" +
			"    'landingZone', l.landing_zone, " +
			"    'countryCode', 'US',  \n" +
			"    'currencyCode', 'USD', \n" +
			"    'currencySymbol', '$', \n" +
			"    'total', '0' \n" +
			"    ) as cost " +
			"  from product p, department d, product_env pe, landingzone l   \n" +
			"  where p.department_id = d.id and pe.product_id = p.id and l.department_id = d.id  \n" +
			"  and p.organization_id = :orgId and l.id = :landingZoneId order by p.name asc ";
	@Query(value = APPLICATION_TOPOLOGY_QUERY, nativeQuery = true)
	List<ApplicationTopologyQueryObj> getApplicationTopology(@Param("orgId") Long orgId, @Param("landingZoneId") Long landingZoneId);

	String PROCESS_CENTRAL_ANALYTIC_QUERY="select \n" +
			" jsonb_build_object( \n" +
			"    'volume',jsonb_build_object(\n" +
			"    \t'product',jsonb_build_object('current','56%', 'previous','62%', 'diff','4%'),\n" +
			"    \t'services',jsonb_build_object('current','22%', 'previous','24%', 'diff','-2%'),\n" +
			"    \t'release',jsonb_build_object('current','36%', 'previous','40%', 'diff','-3%'),\n" +
			"    \t'useCase',jsonb_build_object('current','41%', 'previous','39%', 'diff','2%'),\n" +
			"    \t'bugs',jsonb_build_object('current','46%', 'previous','40%', 'diff','6%'),\n" +
			"    \t'workflow',jsonb_build_object('current','33%', 'previous','36%', 'diff','-3%'),\n" +
			"    \t'documentation',jsonb_build_object('current','11%', 'previous','14%', 'diff','-3%'),\n" +
			"    \t'automationTest',jsonb_build_object('current','46%', 'previous','50%', 'diff','-4%')\n" +
			"    ),\n" +
			"    'velocity',jsonb_build_object(\n" +
			"    \t'scheduleDeviation',jsonb_build_object('current','55%', 'previous','50%', 'diff','5%'),\n" +
			"    \t'releaseTime',jsonb_build_object('current','20%', 'previous','23%', 'diff','-3%'),\n" +
			"    \t'bugFixing',jsonb_build_object('current','46%', 'previous','50%', 'diff','-4%'),\n" +
			"    \t'useCaseDelivery',jsonb_build_object('current','40%', 'previous','35%', 'diff','5%'),\n" +
			"    \t'bugs',jsonb_build_object('current','44%', 'previous','40%', 'diff','4%'),\n" +
			"    \t'workflowGeneration',jsonb_build_object('current','31%', 'previous','33%', 'diff','-2%'),\n" +
			"    \t'documentation',jsonb_build_object('current','9%', 'previous','14%', 'diff','-5%'),\n" +
			"    \t'automationTest',jsonb_build_object('current','11%', 'previous','17%', 'diff','-6%')\n" +
			"    ),\n" +
			"    'reliability',jsonb_build_object(\n" +
			"    \t'postReleaseDefects',jsonb_build_object('current','57%', 'previous','50%', 'diff','7%'),\n" +
			"    \t'usageStats',jsonb_build_object('current','22%', 'previous','28%', 'diff','-6%'))\n" +
			"  ) as dev_central,\n" +
			"  jsonb_build_object( \n" +
			"    'infra',jsonb_build_object(\n" +
			"    \t'account',jsonb_build_object('current','55%', 'previous','50%', 'diff','5%'),\n" +
			"    \t'vpc',jsonb_build_object('current','20%', 'previous','22%', 'diff','-2%'),\n" +
			"    \t'cluster',jsonb_build_object('current','34%', 'previous','38%', 'diff','-4%'),\n" +
			"    \t'managedServices',jsonb_build_object('current','39%', 'previous','35%', 'diff','4%')\n" +
			"    ),\n" +
			"    'app',jsonb_build_object(\n" +
			"    \t'container',jsonb_build_object('current','58%', 'previous','53%', 'diff','5%'),\n" +
			"    \t'code',jsonb_build_object('current','22%', 'previous','26%', 'diff','-4%')\n" +
			"    ),\n" +
			"    'data',jsonb_build_object(\n" +
			"    \t'accessCentral',jsonb_build_object('current','59%', 'previous','51%', 'diff','8%'),\n" +
			"    \t'governance',jsonb_build_object('current','23%', 'previous','25%', 'diff','-2%'),\n" +
			"    \t'transitAndStore',jsonb_build_object('current','35%', 'previous','38%', 'diff','-3%'))\n" +
			"   ) as sec_central,\n" +
			"  jsonb_build_object( \n" +
			"    'volume',jsonb_build_object(\n" +
			"    \t'newCloudProvisioning',jsonb_build_object('current','59%', 'previous','50%', 'diff','9%'),\n" +
			"    \t'newProduct',jsonb_build_object('current','25%', 'previous','28%', 'diff','-3%'),\n" +
			"    \t'serviceOnboarding',jsonb_build_object('current','40%', 'previous','50%', 'diff','-10%'),\n" +
			"    \t'newAutomation',jsonb_build_object('current','45%', 'previous','42%', 'diff','3%'),\n" +
			"    \t'alertsResolved',jsonb_build_object('current','50%', 'previous','48%', 'diff','2%')\n" +
			"    ),\n" +
			"    'velocity',jsonb_build_object(\n" +
			"    \t'scheduleDeviation',jsonb_build_object('current','58%', 'previous','50%', 'diff','8%'),\n" +
			"    \t'releaseTime',jsonb_build_object('current','23%', 'previous','25%', 'diff','-2%'),\n" +
			"    \t'bugFixing',jsonb_build_object('current','59%', 'previous','53%', 'diff','6%'),\n" +
			"    \t'useCaseDelivery',jsonb_build_object('current','24%', 'previous','28%', 'diff','-4%'),\n" +
			"    \t'bugs',jsonb_build_object('current','59%', 'previous','55%', 'diff','4%'),\n" +
			"    \t'workflowGeneration',jsonb_build_object('current','34%', 'previous','35%', 'diff','-1%'),\n" +
			"    \t'documentation',jsonb_build_object('current','59%', 'previous','50%', 'diff','9%'),\n" +
			"    \t'automationTest',jsonb_build_object('current','24%', 'previous','28%', 'diff','-4%')\n" +
			"    ),\n" +
			"    'reliability',jsonb_build_object(\n" +
			"    \t'rateOfReopenTickets',jsonb_build_object('current','51%', 'previous','50%', 'diff','1%'))\n" +
			"  ) as ops_central\n" +
			" from organization o where o.id = :orgId";
	@Query(value = PROCESS_CENTRAL_ANALYTIC_QUERY, nativeQuery = true)
	List<ProcessCentralAnalyticQueryObj> getProcessCentralAnalyticData(@Param("orgId") Long orgId);

}
