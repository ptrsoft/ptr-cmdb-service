package com.synectiks.asset.repository;

import java.util.List;

import com.synectiks.asset.domain.query.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.synectiks.asset.domain.Organization;

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

	String ENV_LANDINGZONE_CLOUD_WISE_COUNT_QUERY ="select cloud, count(*) as environments, "
			+ "sum(cast (summary_json -> 'TotalDiscoveredResources' as integer)) as assets, "
			+ "0 as alerts, 0 as totalbilling "
			+ "from cloud_environment ce, cloud_element_summary ces, department dep, organization org "
			+ "where ce.id = ces.cloud_environment_id "
			+ "and ce.department_id = dep.id and dep.organization_id = org.id "
			+ "and upper(ce.cloud) = upper(:cloud) "
			+ "and org.id = :orgId and ce.account_id = :landingZone "
			+ "group by ce.cloud, ces.summary_json";
	@Query(value = ENV_LANDINGZONE_CLOUD_WISE_COUNT_QUERY, nativeQuery = true)
	EnvironmentCountQueryObj getCount(@Param("landingZone") String landingZone, @Param("cloud") String cloud, @Param("orgId") Long orgId);

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

	String PRODUCT_WISE_COST_QUERY = "WITH product_sum AS ( " +
			"  select " +
			"    c.obj->>'associatedProduct' as name, " +
			"    SUM(cast(value as INT)) as total " +
			"  from " +
			"    cloud_element ce, " +
			"    organization org, " +
			"    JSONB_ARRAY_ELEMENTS(ce.cost_json -> 'elementLists') with ordinality b(obj, pos), " +
			"    JSONB_EACH_TEXT(b.obj -> 'MONTHLYCOST') as jb(key, value), " +
			"    JSONB_ARRAY_ELEMENTS(ce.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj) " +
			"  where " +
			"    org.id = :orgId " +
			"    and extract('year' from TO_DATE(jb.key, 'YYYY-MM')) = extract('year' from CURRENT_DATE) " +
			"  group by c.obj->>'associatedProduct' " +
			"  ) " +
			"  select name, total, (total * 100.0) / (select SUM(total) from product_sum) as percentage " +
			"  from " +
			"  product_sum " +
			"UNION ALL " +
			"  select 'Cumulative Total', SUM(total) as total_sum, null as percentage " +
			"  from " +
			"  product_sum ";
	@Query(value = PRODUCT_WISE_COST_QUERY, nativeQuery = true)
	List<CostAnalyticQueryObj> getProductWiseCost(@Param("orgId") Long orgId);

	String PRODUCTION_VS_OTHERS_QUERY = "with product_sum as ( " +
			"  select " +
			"   c.obj->>'associatedEnv' as name, " +
			"   SUM(cast(value as INT)) as total " +
			"  from " +
			"   cloud_element ce, " +
			"   organization org, " +
			"   JSONB_ARRAY_ELEMENTS(ce.cost_json -> 'elementLists') with ordinality b(obj, pos), " +
			"   JSONB_EACH_TEXT(b.obj -> 'MONTHLYCOST') as jb(key, value), " +
			"   JSONB_ARRAY_ELEMENTS(ce.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj) " +
			"  where " +
			"   org.id = :orgId " +
			"   and extract('year' from TO_DATE(jb.key, 'YYYY-MM')) = extract('year' from CURRENT_DATE) and c.obj->>'associatedEnv' = 'PROD' " +
			"  group by c.obj->>'associatedEnv' " +
			" ), " +
			" " +
			"other_sum as ( " +
			"  select " +
			"   c.obj->>'associatedEnv' as name, " +
			"   SUM(cast(value as INT)) as total " +
			"  from " +
			"   cloud_element ce, " +
			"   organization org, " +
			"   JSONB_ARRAY_ELEMENTS(ce.cost_json -> 'elementLists') with ordinality b(obj, pos), " +
			"   JSONB_EACH_TEXT(b.obj -> 'MONTHLYCOST') as jb(key, value), " +
			"   JSONB_ARRAY_ELEMENTS(ce.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj) " +
			"  where " +
			"   org.id = :orgId " +
			"   and extract('year' from TO_DATE(jb.key, 'YYYY-MM')) = extract('year' from CURRENT_DATE) and c.obj->>'associatedEnv' != 'PROD' " +
			"  group by c.obj->>'associatedEnv' " +
			" ), " +
			"total_sum as ( " +
			" select SUM(total) as total_sum from  " +
			"  (  select SUM(total) as total from product_sum " +
			"    union all " +
			"   select SUM(total) as total from other_sum " +
			"  ) as totals " +
			" ) " +
			"select name, total, ROUND((total / ( select total_sum from total_sum)) * 100, 7) as percentage " +
			"from " +
			" ( " +
			"  select 'PROD' as name, SUM(product_sum.total) as total " +
			"  from product_sum " +
			"   union all " +
			"  select 'others' as name, SUM(other_sum.total) as total " +
			"  from other_sum " +
			" ) as subquery " +
			"union all " +
			"  " +
			"select 'Cumulative Total' as name, " +
			" ( select SUM(total) from product_sum )  " +
			"  +  " +
			" ( select SUM(total) from other_sum ) as total_sum, " +
			" null ";
	@Query(value = PRODUCTION_VS_OTHERS_QUERY, nativeQuery = true)
	List<CostAnalyticQueryObj> getProductionVsOthersCost(@Param("orgId") Long orgId);

	String SERVICE_TYPE_WISE_COST_QUERY = "WITH labels AS ( " +
			"  select " +
			"   ce.cloud_identity ->> 'category' as name, " +
			"   SUM(cast(value as INT)) as total " +
			"  from " +
			"   cloud_element ce, " +
			"   organization org, " +
			"   JSONB_ARRAY_ELEMENTS(ce.cost_json -> 'elementLists') with ordinality b(obj, pos), " +
			"   JSONB_EACH_TEXT(b.obj -> 'MONTHLYCOST') as jb(key, value) " +
			"  where " +
			"   org.id = :orgId " +
			"   and extract('year' from TO_DATE(jb.key, 'YYYY-MM')) = extract('year' from CURRENT_DATE) " +
			"  group by ce.cloud_identity ->> 'category' " +
			" ) " +
			" select " +
			"  name, " +
			"  total, " +
			"  (total * 100.0) / ( select SUM(total) from labels) as percentage " +
			" from " +
			"  labels " +
			"UNION ALL " +
			"select " +
			" 'Cumulative Total', " +
			" SUM(total) as total_sum, " +
			" null as percentage " +
			"from labels ";
	@Query(value = SERVICE_TYPE_WISE_COST_QUERY, nativeQuery = true)
	List<CostAnalyticQueryObj> getServiceTypeWiseCost(@Param("orgId") Long orgId);
	
	

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

}
