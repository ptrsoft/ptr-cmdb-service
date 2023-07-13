package com.synectiks.asset.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.synectiks.asset.domain.Organization;
import com.synectiks.asset.domain.query.CloudElementCloudWiseMonthlyQueryObj;
import com.synectiks.asset.domain.query.CloudElementCloudWiseQueryObj;
import com.synectiks.asset.domain.query.CloudElementSpendAnalyticsQueryObj;
import com.synectiks.asset.domain.query.CloudEnvironmentVpcQueryObj;
import com.synectiks.asset.domain.query.EnvironmentCountQueryObj;
import com.synectiks.asset.domain.query.EnvironmentSummaryQueryObj;
import com.synectiks.asset.domain.query.InfraTopologyQueryObj;
import com.synectiks.asset.domain.query.InfraTopologySummaryQueryObj;
import com.synectiks.asset.domain.query.MonthlyStatisticsQueryObj;
import com.synectiks.asset.domain.query.TotalBudgetQueryObj;

/**
 * Spring Data SQL repository for the query database.
 */
@SuppressWarnings("unused")
@Repository
public interface QueryRepository extends JpaRepository<Organization, Long>{

    String ENV_COUNT_QUERY ="select cloud, count(*) as environments, "
            + "sum(cast (summary_json -> 'TotalDiscoveredResources' as integer)) as assets, "
            + "0 as alerts, 0 as totalbilling "
            + "from cloud_environment ce, cloud_element_summary ces, department dep, organization org  "
            + "where ce.id = ces.cloud_environment_id "
            + "and ce.department_id = dep.id and dep.organization_id = org.id "
            + "and org.id = :orgId "
            + "group by ce.cloud, ces.summary_json";
    @Query(value = ENV_COUNT_QUERY, nativeQuery = true)
    List<EnvironmentCountQueryObj> getCount(@Param("orgId") Long orgId);


    String ENV_CLOUD_WISE_COUNT_QUERY ="select cloud, count(*) as environments, "
            + "sum(cast (summary_json -> 'TotalDiscoveredResources' as integer)) as assets, "
            + "0 as alerts, 0 as totalbilling "
            + "from cloud_environment ce, cloud_element_summary ces, department dep, organization org "
            + "where ce.id = ces.cloud_environment_id "
            + "and ce.department_id = dep.id and dep.organization_id = org.id "
            + "and upper(ce.cloud) = upper(:cloud) "
            + "and org.id = :orgId "
            + "group by ce.cloud, ces.summary_json";
    @Query(value = ENV_CLOUD_WISE_COUNT_QUERY, nativeQuery = true)
    EnvironmentCountQueryObj getCount(@Param("cloud") String cloud, @Param("orgId") Long orgId);

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
	
	String ALL_SPEND_TODAY_ANALYTICS ="with yesterday_sum as ( " +
			"select coalesce(SUM(cast(jb.value as INT)), 0) as sum_values " +
			"from cloud_element ce " +
			"cross join lateral jsonb_array_elements(ce.cost_json -> 'elementLists') with ordinality c(obj,pos) " +
			"cross join lateral jsonb_each_text(c.obj -> 'DAILYCOST') as jb(key, value) " +
			"join cloud_environment cnv on cnv.id = ce.cloud_environment_id " +
			"join department dep on dep.id = cnv.department_id " +
			"join organization org on org.id = dep.organization_id " +
			"where date(jb.key) = current_date - interval '1 day' and org.id = :orgId " +
			") " +
			"select " +
			"coalesce(SUM(cast(jb.value as INT)), 0) as sum_current_date, " +
			"yesterday_sum.sum_values as sum_previous_date, " +
			"cast(coalesce(SUM(cast(jb.value as INT)),0) - yesterday_sum.sum_values as float) * 100.0 / yesterday_sum.sum_values as percentage " +
			"from " +
			"cloud_element ce " +
			"cross join lateral jsonb_array_elements(ce.cost_json -> 'elementLists') with ordinality c(obj, pos) " +
			"cross join lateral jsonb_each_text(c.obj -> 'DAILYCOST') as jb(key, value) " +
			"join cloud_environment cnv on cnv.id = ce.cloud_environment_id " +
			"join department dep on dep.id = cnv.department_id " +
			"join organization org on org.id = dep.organization_id " +
			"cross join yesterday_sum " +
			"where " +
			"date(jb.key) = current_date " +
			"and org.id = :orgId " +
			"group by yesterday_sum.sum_values ";
	@Query(value = ALL_SPEND_TODAY_ANALYTICS, nativeQuery = true)
	public CloudElementSpendAnalyticsQueryObj allSpendTodayAnalytics(@Param("orgId") Long orgId);
	
	String ALL_SPEND_YESTERDAY_ANALYTICS ="with yesterday_sum as ( " +
			"select coalesce(SUM(cast(jb.value as INT)), 0) as sum_values " +
			"from cloud_element ce " +
			"cross join lateral jsonb_array_elements(ce.cost_json -> 'elementLists') with ordinality c(obj,pos) " +
			"cross join lateral jsonb_each_text(c.obj -> 'DAILYCOST') as jb(key, value) " +
			"join cloud_environment cnv on cnv.id = ce.cloud_environment_id " +
			"join department dep on dep.id = cnv.department_id " +
			"join organization org on org.id = dep.organization_id " +
			"where date(jb.key) = current_date - interval '2 day' and org.id = :orgId " +
			") " +
			"select " +
			"coalesce(SUM(cast(jb.value as INT)), 0) as sum_current_date, " +
			"yesterday_sum.sum_values as sum_previous_date, " +
			"cast(coalesce(SUM(cast(jb.value as INT)),0) - yesterday_sum.sum_values as float) * 100.0 / yesterday_sum.sum_values as percentage " +
			"from " +
			"cloud_element ce " +
			"cross join lateral jsonb_array_elements(ce.cost_json -> 'elementLists') with ordinality c(obj, pos) " +
			"cross join lateral jsonb_each_text(c.obj -> 'DAILYCOST') as jb(key, value) " +
			"join cloud_environment cnv on cnv.id = ce.cloud_environment_id " +
			"join department dep on dep.id = cnv.department_id " +
			"join organization org on org.id = dep.organization_id " +
			"cross join yesterday_sum " +
			"where " +
			"date(jb.key) = current_date - interval '1 day' " +
			"and org.id = :orgId " +
			"group by yesterday_sum.sum_values ";
	@Query(value = ALL_SPEND_YESTERDAY_ANALYTICS, nativeQuery = true)
	public CloudElementSpendAnalyticsQueryObj allSpendYesterdayAnalytics(@Param("orgId") Long orgId);
	
//	String ALL_CURRENT_HOUR_ANALYTICS ="WITH current_hour_sum AS (\r\n"
//			+ "  SELECT SUM(CAST(value AS INT)) AS sum_current_hour\r\n"
//			+ "  FROM cloud_element ce\r\n"
//			+ "  JOIN cloud_environment cnv ON cnv.id = ce.cloud_environment_id\r\n"
//			+ "  JOIN department dep ON dep.id = cnv.department_id\r\n"
//			+ "  JOIN organization org ON org.id = dep.organization_id\r\n"
//			+ "  JOIN jsonb_each_text(ce.cost_json->'HOURLYCOST') AS obj(key, value) ON CAST(key AS TEXT) = TO_CHAR(current_timestamp, 'YYYY-MM-DD HH24:59:59')\r\n"
//			+ "  WHERE org.id = :orgId \r\n"
//			+ "    AND jsonb_exists(ce.cost_json->'HOURLYCOST', TO_CHAR(current_timestamp, 'YYYY-MM-DD HH24:59:59'))\r\n"
//			+ "),\r\n"
//			+ "previous_hour_sum AS (\r\n"
//			+ "  SELECT SUM(CAST(value AS INT)) AS sum_previous_hour\r\n"
//			+ "  FROM cloud_element ce\r\n"
//			+ "  JOIN cloud_environment cnv ON cnv.id = ce.cloud_environment_id\r\n"
//			+ "  JOIN department dep ON dep.id = cnv.department_id\r\n"
//			+ "  JOIN organization org ON org.id = dep.organization_id\r\n"
//			+ "  JOIN jsonb_each_text(ce.cost_json->'HOURLYCOST') AS obj(key, value) ON CAST(key AS TEXT) = TO_CHAR(current_timestamp - INTERVAL '1 hour', 'YYYY-MM-DD HH24:59:59')\r\n"
//			+ "  WHERE org.id = :orgId \r\n"
//			+ "    AND jsonb_exists(ce.cost_json->'HOURLYCOST', TO_CHAR(current_timestamp - INTERVAL '1 hour', 'YYYY-MM-DD HH24:59:59'))\r\n"
//			+ ")\r\n"
//			+ "SELECT\r\n"
//			+ "  sum_current_hour,\r\n"
//			+ "  sum_previous_hour,\r\n"
//			+ "  (sum_current_hour - sum_previous_hour) / sum_current_hour * 100 AS percentage,\r\n"
//			+ "  sum_current_hour - sum_previous_hour AS sum_difference\r\n"
//			+ "FROM current_hour_sum, previous_hour_sum ";
//	@Query(value = ALL_CURRENT_HOUR_ANALYTICS, nativeQuery = true)
//	List<CloudElementCurrentQueryObj> spendCurrentRateHour(@Param("orgId") Long orgId);
//
	String ALL_CURRENT_DAY_ANALYTICS ="select coalesce(sum(cast (jb.value as int)),0) AS sum_values " +
			"from cloud_element ce, " +
			"jsonb_array_elements(ce.cost_json  -> 'elementLists') with ordinality c(obj, pos), " +
			"jsonb_each_text(c.obj -> 'DAILYCOST') AS jb(key, value), " +
			"cloud_environment cnv, department dep, organization org " +
			"where date(jb.key) = current_date and " +
			"org.id = dep.organization_id " +
			"and dep.id = cnv.department_id " +
			"and cnv.id = ce.cloud_environment_id " +
			"and org.id = :orgId  ";
	@Query(value = ALL_CURRENT_DAY_ANALYTICS, nativeQuery = true)
	Long currentSpendRatePerDay(@Param("orgId") Long orgId);
	
	String ALL_TOTAL_SPEND_ANALYTICS ="SELECT SUM(cast(value as INT)) AS sum_values \n" +
			" FROM cloud_element ce, \n" +
			" cloud_environment cnv, \n" +
			" department dep, \n" +
			" organization org, \n" +
			" jsonb_array_elements(ce.cost_json -> 'elementLists') with ordinality c(obj, pos), \n" +
			" jsonb_each_text(c.obj -> 'YEARLYCOST') AS jb(key, value) \n" +
			" where org.id = dep.organization_id \n" +
			" and dep.id = cnv.department_id \n" +
			" and cnv.id = ce.cloud_environment_id \n" +
			" and cast(jb.key as int) = extract ('year' from current_date) \n" +
			" and org.id = :orgId ";
	@Query(value = ALL_TOTAL_SPEND_ANALYTICS, nativeQuery = true)
	List<String> totalSpendAnalytics(@Param("orgId") Long orgId);
	
	String ALL_CLOUD_WISE_ANALYTICS ="select distinct cnv.cloud,\r\n"
			+ "SUM(cast(value as INT)) AS sum_values,\r\n"
			+ "(SUM(cast(value as INT)) * 100) / (\r\n"
			+ "SELECT SUM(cast(value as INT))\r\n"
			+ "FROM cloud_element ce,\r\n"
			+ "jsonb_array_elements(ce.cost_json -> 'elementLists') with ordinality c(obj, pos),\r\n"
			+ "jsonb_each_text(c.obj -> 'DAILYCOST') AS jb(key, value)\r\n"
			+ ") AS percentage\r\n"
			+ "FROM cloud_element ce,\r\n"
			+ "cloud_environment cnv,\r\n"
			+ "department dep,\r\n"
			+ "organization org,\r\n"
			+ "jsonb_array_elements(ce.cost_json -> 'elementLists') with ordinality c(obj, pos),\r\n"
			+ "jsonb_each_text(c.obj -> 'DAILYCOST') AS jb(key, value)\r\n"
			+ "WHERE org.id = dep.organization_id\r\n"
			+ "AND dep.id = cnv.department_id\r\n"
			+ "AND cnv.id = ce.cloud_environment_id\r\n"
			+ "AND org.id = :orgId\r\n"
			+ "AND cnv.cloud IN (SELECT DISTINCT ce.cloud\r\n"
			+ "FROM cloud_environment ce, department d, organization o\r\n"
			+ "WHERE o.id = d.organization_id AND d.id = ce.department_id AND o.id = :orgId)\r\n"
			+ "GROUP BY cnv.cloud ORDER BY cnv.cloud ASC ";
	@Query(value = ALL_CLOUD_WISE_ANALYTICS, nativeQuery = true)
	List<CloudElementCloudWiseQueryObj> cloudWiseTotalSpendAnalytics(@Param("orgId")Long orgId);
	
	
	String ALL_MONTHLY_WISE_ANALYTICS="select cnv.cloud, TO_CHAR(TO_DATE(jb.key, 'YYYY-MM'), 'Month') AS month, SUM(CAST(jb.value AS INT)) AS sum_values " +
			"from cloud_element ce, " +
			"jsonb_array_elements(ce.cost_json  -> 'elementLists') with ordinality c(obj, pos), " +
			"jsonb_each_text(c.obj -> 'MONTHLYCOST') AS jb(key, value), " +
			"cloud_environment cnv, department dep, organization org " +
			"where org.id = dep.organization_id " +
			"and dep.id = cnv.department_id " +
			"and cnv.id = ce.cloud_environment_id " +
			"and org.id = :orgId " +
			"and cnv.cloud IN (SELECT DISTINCT ce.cloud  " +
			"  FROM cloud_environment ce, department d, organization o " +
			"  WHERE o.id = d.organization_id AND d.id = ce.department_id AND o.id = :orgId) " +
			"group by cnv.cloud, TO_CHAR(TO_DATE(jb.key, 'YYYY-MM'), 'Month'), jb.key " +
			"ORDER BY TO_DATE(jb.key, 'YYYY-MM') ASC, cnv.cloud ASC";
	@Query(value = ALL_MONTHLY_WISE_ANALYTICS, nativeQuery = true)
	List<CloudElementCloudWiseMonthlyQueryObj> eachMonthTotal(@Param("orgId")Long orgId);
	

	String INFRA_TOPOLOGY_QUERY = "select ce.hardware_location ->> 'landingZone' as landing_zone, " +
			"ce.hardware_location ->> 'productEnclave' as product_enclave, " +
			"ce.cloud_identity ->> 'hostingType' as hosting_type, " +
			"ce.cloud_identity ->> 'category' as category, " +
			"ce.element_type, " +
			"ce.cloud_identity ->> 'elementLists' as element_list " +
			"from cloud_element ce, cloud_environment cnv, department dep, organization org " +
			"where ce.cloud_environment_id = cnv.id and cnv.department_id = dep.id and dep.organization_id = org.id " +
			"and org.id = :orgId and ce.hardware_location ->> 'landingZone' = :landingZone ";
	@Query(value = INFRA_TOPOLOGY_QUERY, nativeQuery = true)
	List<InfraTopologyQueryObj> getInfraTopology(@Param("orgId") Long orgId, @Param("landingZone") String landingZone);
	
	String CLOUD_TYPE_TOPOLOGY_QUERY = " SELECT DISTINCT\r\n"
			+ "  ce.cloud_identity ->> 'hostingType' AS hosting_type,\r\n"
			+ "  ce.element_type,\r\n"
			+ "  ce.cloud_identity ->> 'category' AS category,\r\n"
			+ "  ce.cloud_identity ->> 'dbCategory' AS db_category,\r\n"
			+ "  coalesce(jsonb_array_length(ce.cloud_identity -> 'elementLists'), 0) AS total_elements\r\n"
			+ "FROM\r\n"
			+ "  cloud_element ce,\r\n"
			+ "  cloud_environment cnv,\r\n"
			+ "  department dep,\r\n"
			+ "  organization org\r\n"
			+ "WHERE\r\n"
			+ "  ce.cloud_environment_id = cnv.id\r\n"
			+ "  AND cnv.department_id = dep.id\r\n"
			+ "  AND dep.organization_id = org.id\r\n"
			+ "  AND org.id = :orgId \r\n"
			+ "  AND ce.hardware_location ->> 'landingZone' = :landingZone \r\n"
			+ "  AND ce.hardware_location ->> 'productEnclave' = :productEnclave \r\n"
			+ "ORDER BY\r\n"
			+ "  ce.cloud_identity ->> 'hostingType' ASC ";
	@Query(value = CLOUD_TYPE_TOPOLOGY_QUERY, nativeQuery = true)
	List<InfraTopologySummaryQueryObj> getInfraTopologySummary(@Param("orgId") Long orgId, @Param("landingZone") String landingZone,@Param("productEnclave") String productEnclave);
	
	String MOTHLY_STATISTICS_QUERY = "WITH current_year_data AS (\r\n"
			+ "  SELECT\r\n"
			+ "    TO_CHAR(TO_DATE(obj.key, 'YYYY-MM'), 'Month') AS month,\r\n"
			+ "    SUM(CAST(obj.value AS INT)) AS sum_values\r\n"
			+ "  FROM\r\n"
			+ "    cloud_element ce\r\n"
			+ "  JOIN\r\n"
			+ "    cloud_environment cnv ON cnv.id = ce.cloud_environment_id\r\n"
			+ "  JOIN\r\n"
			+ "    department dep ON dep.id = cnv.department_id\r\n"
			+ "  JOIN\r\n"
			+ "    organization org ON org.id = dep.organization_id\r\n"
			+ "  CROSS JOIN\r\n"
			+ "    JSONB_EACH_TEXT(ce.cost_json->'MONTHLYCOST') AS obj(key, value)\r\n"
			+ "  WHERE\r\n"
			+ "    org.id = :orgId\r\n"
			+ "    AND EXTRACT(YEAR FROM TO_DATE(obj.key, 'YYYY-MM')) = EXTRACT(YEAR FROM CURRENT_DATE) -- Filter for current year\r\n"
			+ "  GROUP BY\r\n"
			+ "    TO_CHAR(TO_DATE(obj.key, 'YYYY-MM'), 'Month'), EXTRACT(MONTH FROM TO_DATE(obj.key, 'YYYY-MM')) -- Group by month and month number\r\n"
			+ "),\r\n"
			+ "previous_year_data AS (\r\n"
			+ "  SELECT\r\n"
			+ "    TO_CHAR(TO_DATE(obj.key, 'YYYY-MM'), 'Month') AS month,\r\n"
			+ "    SUM(CAST(obj.value AS INT)) AS sum_values\r\n"
			+ "  FROM\r\n"
			+ "    cloud_element ce\r\n"
			+ "  JOIN\r\n"
			+ "    cloud_environment cnv ON cnv.id = ce.cloud_environment_id\r\n"
			+ "  JOIN\r\n"
			+ "    department dep ON dep.id = cnv.department_id\r\n"
			+ "  JOIN\r\n"
			+ "    organization org ON org.id = dep.organization_id\r\n"
			+ "  CROSS JOIN\r\n"
			+ "    JSONB_EACH_TEXT(ce.cost_json->'MONTHLYCOST') AS obj(key, value)\r\n"
			+ "  WHERE\r\n"
			+ "    org.id = :orgId\r\n"
			+ "    AND EXTRACT(YEAR FROM TO_DATE(obj.key, 'YYYY-MM')) = EXTRACT(YEAR FROM CURRENT_DATE) - 1 -- Filter for the previous year\r\n"
			+ "  GROUP BY\r\n"
			+ "    TO_CHAR(TO_DATE(obj.key, 'YYYY-MM'), 'Month'), EXTRACT(MONTH FROM TO_DATE(obj.key, 'YYYY-MM')) -- Group by month and month number\r\n"
			+ "),\r\n"
			+ "current_year_sum AS (\r\n"
			+ "  SELECT SUM(CAST(obj.value AS INT)) AS total_sum\r\n"
			+ "  FROM cloud_element ce\r\n"
			+ "  JOIN cloud_environment cnv ON cnv.id = ce.cloud_environment_id\r\n"
			+ "  JOIN department dep ON dep.id = cnv.department_id\r\n"
			+ "  JOIN organization org ON org.id = dep.organization_id\r\n"
			+ "  CROSS JOIN JSONB_EACH_TEXT(ce.cost_json->'MONTHLYCOST') AS obj(key, value)\r\n"
			+ "  WHERE org.id = :orgId\r\n"
			+ "    AND EXTRACT(YEAR FROM TO_DATE(obj.key, 'YYYY-MM')) = EXTRACT(YEAR FROM CURRENT_DATE)\r\n"
			+ ")\r\n"
			+ "SELECT \r\n"
			+ "  ROW_NUMBER() OVER() AS id,\r\n"
			+ "  current_year_data.month,\r\n"
			+ "  current_year_data.sum_values AS current_year_sum,\r\n"
			+ "  previous_year_data.sum_values AS previous_year_sum,\r\n"
			+ "  current_year_data.sum_values - previous_year_data.sum_values AS difference,\r\n"
			+ "  ROUND(((current_year_data.sum_values - previous_year_data.sum_values)::float / current_year_data.sum_values) * 100::float) AS difference_percentage,\r\n"
			+ "  current_year_sum.total_sum AS current_year_total_sum\r\n"
			+ "FROM\r\n"
			+ "  current_year_data\r\n"
			+ "JOIN\r\n"
			+ "  previous_year_data ON current_year_data.month = previous_year_data.month\r\n"
			+ "CROSS JOIN\r\n"
			+ "  current_year_sum\r\n"
			+ "ORDER BY \r\n"
			+ "  EXTRACT(MONTH FROM TO_DATE(current_year_data.month, 'Month')) ASC ";
	@Query(value = MOTHLY_STATISTICS_QUERY, nativeQuery = true)
	List<MonthlyStatisticsQueryObj> monthlyStatisticsQueryObj(@Param("orgId")Long orgId);
	
	
	
	String TOTAL_BUdGET_QUERY = "WITH monthly_costs AS (\r\n"
			+ "    SELECT SUM(CAST(value AS INT)) AS sum_values\r\n"
			+ "    FROM cloud_element ce,\r\n"
			+ "    JSONB_EACH_TEXT(ce.cost_json->'MONTHLYCOST') AS obj(key, value)\r\n"
			+ "), budget_sum AS (\r\n"
			+ "    SELECT b.allocated_budget AS total_sum\r\n"
			+ "    FROM budget b\r\n"
			+ "    JOIN organization org ON b.organization_id = org.id\r\n"
			+ "    WHERE org.id = :orgId\r\n"
			+ ")\r\n"
			+ "SELECT\r\n"
			+ "    monthly_costs.sum_values AS monthly_cost_sum,\r\n"
			+ "    budget_sum.total_sum AS budget_total_sum,\r\n"
			+ "    monthly_costs.sum_values - budget_sum.total_sum AS difference,\r\n"
			+ "    (monthly_costs.sum_values - budget_sum.total_sum) / budget_sum.total_sum * 100 AS percentage\r\n"
			+ "FROM monthly_costs, budget_sum ";
	@Query(value = TOTAL_BUdGET_QUERY, nativeQuery = true)
	List<TotalBudgetQueryObj> totalBudget(@Param("orgId") Long orgId);
}
