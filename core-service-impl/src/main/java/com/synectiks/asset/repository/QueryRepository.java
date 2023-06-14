package com.synectiks.asset.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.synectiks.asset.api.model.MicroServiceDTO;
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
				+ "and org.id = :orgId";
		@Query(value = ORG_MICRO_SERVICES_QUERY, nativeQuery = true)
		public List<MicroServiceDTO> getOrgWiseServices(@Param("orgId")Long orgId);


}
