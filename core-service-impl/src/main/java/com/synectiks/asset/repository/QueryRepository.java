package com.synectiks.asset.repository;

import java.util.List;

import com.synectiks.asset.domain.CloudElement;
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
	
	String INFRA_TOPOLOGY_3TIER_QUERY ="SELECT l.cloud, \n" +
			"\t    p.type, \n" +
			"\t    pe.id as product_enclave_id,\n" +
			"\t    pe.instance_id AS instance_id, \n" +
			"\t   pe.instance_name as product_enclave_name, \n" +
			"\t    COALESCE(COUNT(DISTINCT be.product_id), 0) AS product_count, \n" +
			"\t    SUM(CASE WHEN upper(be.service_type) = upper('Web') THEN 1 ELSE 0 END) AS Web_Count, \n" +
			"\t    SUM(CASE WHEN upper(be.service_type) = upper('App') THEN 1 ELSE 0 END) AS App_Count, \n" +
			"\t    SUM(CASE WHEN upper(be.service_type) = upper('Data') THEN 1 ELSE 0 END) AS Data_Count, \n" +
			"\t    SUM(CASE WHEN upper(be.service_type) = upper('Auxiliary') THEN 1 ELSE 0 END) as Auxiliary_Count  \n" +
			"\tFROM landingzone l \n" +
			"\tINNER JOIN department d ON l.department_id = d.id \n" +
			"\tINNER JOIN organization o ON d.organization_id = o.id \n" +
			"\tLEFT JOIN cloud_element ce ON ce.landingzone_id = l.id \n" +
			"\tLEFT JOIN product_enclave pe ON pe.landingzone_id = l.id AND pe.department_id = d.id \n" +
			"\tLEFT JOIN business_element be ON be.cloud_element_id = ce.id \n" +
			"\tLEFT JOIN product p ON be.product_id = p.id  \n" +
			"\twhere \n" +
			"\to.id = d.organization_id \n" +
			"\t    AND l.department_id = d.id \n" +
			"\t    AND upper(p.type) = upper('3 Tier') \n" +
			"\t    and o.id = :orgId\n" +
			"\t    AND l.landing_zone = :landingZone \n" +
			"\tGROUP BY l.cloud, p.type, pe.id, pe.instance_id, pe.instance_name ";
	@Query(value = INFRA_TOPOLOGY_3TIER_QUERY, nativeQuery = true)
	List<InfraTopology3TierQueryObj> getInfraTopology3TierView(@Param("orgId") Long orgId, @Param("landingZone") String landingZone);

	String INFRA_TOPOLOGY_3TIER_BY_LANDINGZONE_ID_QUERY ="with vpc as(\n" +
			"    select  l.cloud,pe.id, \n" +
			"            pe.instance_id , \n" +
			"            pe.instance_name \n" +
			"    from product_enclave pe, landingzone l, department d, organization o  \n" +
			"    where pe.landingzone_id = l.id and l.department_id = d.id " +
			"	 and d.organization_id = o.id and o.id = :orgId " +
			"	 and l.id = :landingZoneId), \n" +
			" vpc_data as (\n" +
			"     select  p.type, pe.id,\n" +
			"     pe.instance_id , \n" +
			"     pe.instance_name ,\n" +
			"     count(distinct be.product_id) as product_count,\n" +
			"     SUM(CASE WHEN upper(be.service_type) = upper('Web') THEN 1 ELSE 0 END) AS web_count, \n" +
			"        SUM(CASE WHEN upper(be.service_type) = upper('App') THEN 1 ELSE 0 END) AS app_count, \n" +
			"        SUM(CASE WHEN upper(be.service_type) = upper('Data') THEN 1 ELSE 0 END) AS data_count, \n" +
			"        SUM(CASE WHEN upper(be.service_type) = upper('Auxiliary') THEN 1 ELSE 0 END) as auxiliary_count\n" +
			"     from cloud_element ce, business_element be, product p, product_enclave pe, landingzone l  \n" +
			"     where ce.id = be.cloud_element_id \n" +
			"     and p.id = be.product_id and pe.id = ce.product_enclave_id \n" +
			"     and ce.landingzone_id = l.id and l.id = :landingZoneId \n" +
			"     and upper(p.type) = upper('3 Tier')   \n" +
			"     group by p.type,pe.instance_id, pe.instance_name, pe.id\n" +
			"    )\n" +
			"    select  vpc.cloud, '3 Tier' as type, vpc.id as product_enclave_id, vpc.instance_id as instance_id, vpc.instance_name as product_enclave_name,\n" +
			"            coalesce(vpc_data.product_count,0) as product_count, coalesce(vpc_data.web_count,0) as web_count, \n" +
			"            coalesce(vpc_data.app_count,0) as app_count, coalesce(vpc_data.data_count,0) as data_count, \n" +
			"            coalesce(vpc_data.auxiliary_count,0) as auxiliary_count \n" +
			"    from vpc\n" +
			"    left join vpc_data on vpc.id = vpc_data.id  \n" +
			"                and vpc.instance_id = vpc_data.instance_id \n" +
			"                and vpc.instance_name = vpc_data.instance_name";
	@Query(value = INFRA_TOPOLOGY_3TIER_BY_LANDINGZONE_ID_QUERY, nativeQuery = true)
	List<InfraTopology3TierQueryObj> getInfraTopology3TierViewByLandingZoneId(@Param("orgId") Long orgId, @Param("landingZoneId") Long landingZoneId);


	String INFRA_TOPOLOGY_GLOBAL_3TIER_BY_LANDINGZONE_ID_QUERY =" with vpc as( \n" +
			"    select l.cloud, l.id  \n" +
			"    from landingzone l, department d, organization o   \n" +
			"    where  l.department_id = d.id  \n" +
			"            and d.organization_id = o.id  \n" +
			"            and o.id = :orgId and l.id = :landingZoneId), \n" +
			" vpc_data as ( \n" +
			"   select l.id, '3 Tier' as type, count(distinct be.product_id) as product_count,  \n" +
			"     SUM(CASE WHEN upper(be.service_type) = upper('Web') THEN 1 ELSE 0 END) AS web_count,  \n" +
			"     SUM(CASE WHEN upper(be.service_type) = upper('App') THEN 1 ELSE 0 END) AS app_count,  \n" +
			"     SUM(CASE WHEN upper(be.service_type) = upper('Data') THEN 1 ELSE 0 END) AS data_count,  \n" +
			"     SUM(CASE WHEN upper(be.service_type) = upper('Auxiliary') THEN 1 ELSE 0 END) as auxiliary_count \n" +
			"   from cloud_element ce, business_element be, landingzone l, product p   \n" +
			"   where ce.product_enclave_id is null  \n" +
			"    and ce.id = be.cloud_element_id \n" +
			"    and ce.landingzone_id = l.id  \n" +
			"    and p.id = be.product_id \n" +
			"    and upper(p.type) = upper('3 Tier') \n" +
			"    and l.id = :landingZoneId \n" +
			"    group by l.id, p.type) \n" +
			" select vpc.cloud, '3 Tier' as type, null product_enclave_id, null as instance_id, null as product_enclave_name, \n" +
			"        coalesce(vpc_data.product_count,0) as product_count, \n" +
			"        coalesce(vpc_data.web_count,0) as web_count, \n" +
			"        coalesce(vpc_data.app_count,0) as app_count, \n" +
			"        coalesce(vpc_data.data_count,0) as data_count, \n" +
			"        coalesce(vpc_data.auxiliary_count,0) as auxiliary_count \n" +
			" from vpc \n" +
			" left join vpc_data on vpc.id = vpc_data.id ";
	@Query(value = INFRA_TOPOLOGY_GLOBAL_3TIER_BY_LANDINGZONE_ID_QUERY, nativeQuery = true)
	List<InfraTopology3TierQueryObj> getInfraTopologyGlobal3TierViewByLandingZoneId(@Param("orgId") Long orgId, @Param("landingZoneId") Long landingZoneId);

	String INFRA_TOPOLOGY_SOA_QUERY ="\tSELECT \n" +
			"\t    l.cloud, \n" +
			"\t    p.type,\n" +
			"\t    pe.id as product_enclave_id,\n" +
			"\t    pe.instance_id AS instance_id, \n" +
			"\t    pe.instance_name as product_enclave_name, \n" +
			"\t    COALESCE(COUNT(DISTINCT be.product_id), 0) AS product_count, \n" +
			"\t    SUM(CASE WHEN upper(be.service_type) = upper('App') THEN 1 ELSE 0 END) AS app_count, \n" +
			"\t    SUM(CASE WHEN upper(be.service_type) = upper('Data') THEN 1 ELSE 0 END) AS data_count, \n" +
			"\t    SUM(CASE WHEN upper(be.service_type) NOT IN (upper('App'), upper('Data')) THEN 1 ELSE 0 END) AS other_count \n" +
			"\tFROM landingzone l \n" +
			"\tINNER JOIN department d ON l.department_id = d.id \n" +
			"\tINNER JOIN organization o ON d.organization_id = o.id \n" +
			"\tLEFT JOIN cloud_element ce ON ce.landingzone_id = l.id \n" +
			"\tLEFT JOIN product_enclave pe ON pe.landingzone_id  = l.id AND pe.department_id = l.department_id  \n" +
			"\tLEFT JOIN business_element be ON be.cloud_element_id = ce.id \n" +
			"\tLEFT JOIN product p ON be.product_id = p.id  \n" +
			"\twhere \n" +
			"\to.id = d.organization_id \n" +
			"\t    AND l.department_id = d.id \n" +
			"\t    AND upper(p.type) = upper('SOA') \n" +
			"\t    and o.id = :orgId\n" +
			"\t    AND l.landing_zone = :landingZone \n" +
			"\tGROUP BY l.cloud,p.type, pe.id, pe.instance_id, pe.instance_name";
	@Query(value = INFRA_TOPOLOGY_SOA_QUERY, nativeQuery = true)
	List<InfraTopologySOAQueryObj> getInfraTopologySOAView(@Param("orgId") Long orgId, @Param("landingZone") String landingZone);

	String INFRA_TOPOLOGY_SOA_BY_LANDINGZONE_ID_QUERY ="with vpc as( \n" +
			"    select  l.cloud,pe.id,  \n" +
			"    		 pe.instance_id ,  \n" +
			"        	 pe.instance_name  \n" +
			"    from product_enclave pe, landingzone l, department d, organization o  \n" +
			"    where pe.landingzone_id = l.id and l.department_id = d.id " +
			"	 and d.organization_id = o.id and o.id = :orgId " +
			"	 and l.id = :landingZoneId), \n" +
			" vpc_data as ( \n" +
			"    select  p.type, pe.id, \n" +
			"    pe.instance_id ,  \n" +
			"    pe.instance_name , \n" +
			"    count(distinct be.product_id) as product_count, \n" +
			"    SUM(CASE WHEN upper(be.service_type) = upper('App') THEN 1 ELSE 0 END) AS app_count,  \n" +
			"        SUM(CASE WHEN upper(be.service_type) = upper('Data') THEN 1 ELSE 0 END) AS data_count,  \n" +
			"        SUM(CASE WHEN upper(be.service_type) NOT IN (upper('App'), upper('Data')) THEN 1 ELSE 0 END) AS other_count \n" +
			"    from cloud_element ce, business_element be, product p, product_enclave pe, landingzone l   \n" +
			"    where ce.id = be.cloud_element_id   \n" +
			"    and p.id = be.product_id and pe.id = ce.product_enclave_id \n" +
			"    and ce.landingzone_id = l.id and l.id = 1 \n" +
			"    and upper(p.type) = upper('soa')    \n" +
			"    group by p.type,pe.instance_id, pe.instance_name, pe.id \n" +
			" ) \n" +
			" select  vpc.cloud, upper('soa') as type, vpc.id as product_enclave_id, vpc.instance_id as instance_id, vpc.instance_name as product_enclave_name, \n" +
			"    coalesce(vpc_data.product_count,0) as product_count, coalesce(vpc_data.app_count,0) as app_count, \n" +
			"    coalesce(vpc_data.data_count,0) as data_count, \n" +
			"    coalesce(vpc_data.other_count,0) as other_count    \n" +
			" from vpc \n" +
			" left join vpc_data on vpc.id = vpc_data.id  \n" +
			"                    and vpc.instance_id = vpc_data.instance_id  \n" +
			"                    and vpc.instance_name = vpc_data.instance_name ";
	@Query(value = INFRA_TOPOLOGY_SOA_BY_LANDINGZONE_ID_QUERY, nativeQuery = true)
	List<InfraTopologySOAQueryObj> getInfraTopologySOAViewByLandingZoneId(@Param("orgId") Long orgId, @Param("landingZoneId") Long landingZoneId);

	String INFRA_TOPOLOGY_GLOBAL_SOA_BY_LANDINGZONE_ID_QUERY ="with vpc as( \n" +
			"    select l.cloud, l.id  \n" +
			"    from landingzone l, department d, organization o   \n" +
			"    where l.department_id = d.id  \n" +
			"       and d.organization_id = o.id  \n" +
			"       and o.id = :orgId and l.id = :landingZoneId), \n" +
			" vpc_data as ( \n" +
			"    select l.id, p.type, count(distinct be.product_id) as product_count,  \n" +
			"     SUM(CASE WHEN upper(be.service_type) = upper('App') THEN 1 ELSE 0 END) AS app_count,  \n" +
			"        SUM(CASE WHEN upper(be.service_type) = upper('Data') THEN 1 ELSE 0 END) AS data_count,  \n" +
			"        SUM(CASE WHEN upper(be.service_type) NOT IN (upper('App'), upper('Data')) THEN 1 ELSE 0 END) AS other_count \n" +
			"    from  \n" +
			"    cloud_element ce, business_element be, landingzone l, product p   \n" +
			"    where  ce.product_enclave_id is null  \n" +
			"       and ce.id = be.cloud_element_id \n" +
			"       and ce.landingzone_id = l.id  \n" +
			"       and p.id = be.product_id \n" +
			"       and upper(p.type) = upper('soa') \n" +
			"       and l.id = :landingZoneId \n" +
			"  group by l.id, p.type) \n" +
			" select  vpc.cloud, upper('soa') as type, null product_enclave_id, null as instance_id, null as product_enclave_name,  \n" +
			"         coalesce(vpc_data.product_count,0) as product_count, \n" +
			"         coalesce(vpc_data.app_count,0) as app_count, \n" +
			"         coalesce(vpc_data.data_count,0) as data_count, \n" +
			"         coalesce(vpc_data.other_count,0) as other_count    \n" +
			" from vpc \n" +
			" left join vpc_data on vpc.id = vpc_data.id  ";
	@Query(value = INFRA_TOPOLOGY_GLOBAL_SOA_BY_LANDINGZONE_ID_QUERY, nativeQuery = true)
	List<InfraTopologySOAQueryObj> getInfraTopologyGlobalSOAViewByLandingZoneId(@Param("orgId") Long orgId, @Param("landingZoneId") Long landingZoneId);

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

	String SERVICE_TYPE_WISE_COST_QUERY = " WITH labels AS (   " +
			"   select   " +
			"    ce.category as name,   " +
			"    SUM(cast(value as INT)) as total   " +
			"   from   " +
			"    cloud_element ce, department d, landingzone l,  " +
			"    organization org,   " +
			"    JSONB_EACH_TEXT(ce.cost_json  -> 'cost' -> 'MONTHLYCOST') as jb(key, value)   " +
			"   where   " +
			"    d.organization_id = org.id  " +
			"    and l.department_id = d.id  " +
			"    and org.id = :orgId   " +
			"    and extract('year' from TO_DATE(jb.key, 'YYYY-MM')) = extract('year' from CURRENT_DATE)   " +
			"   group by ce.category   " +
			"  )   " +
			"  select   " +
			"   name,   " +
			"   total,   " +
			"   (total * 100.0) / ( select SUM(total) from labels) as percentage   " +
			"  from   " +
			"   labels   " +
			" UNION ALL   " +
			" select   " +
			"  'Cumulative Total',   " +
			"  SUM(total) as total_sum,   " +
			"  null as percentage   " +
			" from labels   ";
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
