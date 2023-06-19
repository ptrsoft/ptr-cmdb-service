package com.synectiks.asset.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.asset.domain.Organization;
import com.synectiks.asset.domain.query.EnvironmentCountQueryObj;
import com.synectiks.asset.domain.query.EnvironmentSummaryQueryObj;

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
		
	
		
	
		
		
	
		
	
		
		
	
		
	
		
		

}
