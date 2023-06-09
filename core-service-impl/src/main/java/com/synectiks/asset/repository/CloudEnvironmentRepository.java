package com.synectiks.asset.repository;

import com.synectiks.asset.domain.CloudEnvironment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CloudEnvironment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CloudEnvironmentRepository extends JpaRepository<CloudEnvironment, Long> {

//	String ENV_COUNT_QUERY ="select cloud, count(*) as environments, "
//			+ "sum(cast (summary_json -> 'TotalDiscoveredResources' as integer)) as assets, "
//			+ "0 as alerts, 0 as totalbilling "
//			+ "from cloud_environment ce, cloud_element_summary ces, department dep, organization org  "
//			+ "where ce.id = ces.cloud_environment_id "
//			+ "and ce.department_id = dep.id and dep.organization_id = org.id "
//			+ "and org.id = :orgId "
//			+ "group by ce.cloud, ces.summary_json";
//
//	@Query(value = ENV_COUNT_QUERY, nativeQuery = true)
//	public List<EnvironmentCountsDto> getCount(@Param("orgId") Long orgId);
//
//	String ENV_CLOUD_WISE_COUNT_QUERY ="select cloud, count(*) as environments, "
//			+ "sum(cast (summary_json -> 'TotalDiscoveredResources' as integer)) as assets, "
//			+ "0 as alerts, 0 as totalbilling "
//			+ "from cloud_environment ce, cloud_element_summary ces, department dep, organization org "
//			+ "where ce.id = ces.cloud_environment_id "
//			+ "and ce.department_id = dep.id and dep.organization_id = org.id "
//			+ "and upper(ce.cloud) = upper(:cloud) "
//			+ "and org.id = :orgId "
//			+ "group by ce.cloud, ces.summary_json";
//
//	@Query(value = ENV_CLOUD_WISE_COUNT_QUERY, nativeQuery = true)
//	public EnvironmentCountsDto getCount(@Param("cloud") String cloud, @Param("orgId") Long orgId);
//
//
//    String ORG_WISE_ENV_SUMMARY_QUERY ="select cnv.cloud, replace(cast(ceo.landing_zone as text), '\"', '') as landing_zone, " +
//        "count(ceo.product_enclave) as product_enclave, " +
//        "(select count(c.obj -> 'associatedProduct')  " +
//        " from cloud_element ce2, jsonb_array_elements(ce2.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj, pos) " +
//        " where ce2.hardware_location -> 'landingZone' = ceo.landing_zone " +
//        " ) as product, " +
//        "(select count(c.obj -> 'associatedService')  " +
//        " from cloud_element ce2, jsonb_array_elements(ce2.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj, pos) " +
//        " where ce2.hardware_location -> 'landingZone' = ceo.landing_zone and upper(cast(c.obj -> 'serviceType' as text)) = '\"APP\"' " +
//        " ) as app_service, " +
//        "(select count(c.obj -> 'associatedService')  " +
//        " from cloud_element ce2, jsonb_array_elements(ce2.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj, pos) " +
//        " where ce2.hardware_location -> 'landingZone' = ceo.landing_zone and upper(cast(c.obj -> 'serviceType' as text)) = '\"DATA\"' " +
//        " ) as data_service  " +
//        "from (select ce.cloud_environment_id, ce.hardware_location -> 'landingZone' as landing_zone, " +
//        " count(ce.hardware_location -> 'productEnclave') as product_enclave " +
//        " from  cloud_element ce group by ce.cloud_environment_id, ce.hardware_location -> 'landingZone' " +
//        " ) as ceo, " +
//        "cloud_environment cnv, department dep, organization org " +
//        "where ceo.cloud_environment_id = cnv.id  " +
//        "and cnv.department_id = dep.id " +
//        "and dep.organization_id = org.id " +
//        "and org.id = :orgId " +
//        "group by cnv.cloud, ceo.landing_zone, ceo.product_enclave";
//    @Query(value = ORG_WISE_ENV_SUMMARY_QUERY, nativeQuery = true)
//    public List<EnvironmentSummaryDto> getEnvironmentSummary(@Param("orgId") Long orgId);
//
//    String ORG_AND_CLOUD_WISE_ENV_SUMMARY_QUERY ="select cnv.cloud, replace(cast(ceo.landing_zone as text), '\"', '') as landing_zone, " +
//        "count(ceo.product_enclave) as product_enclave, " +
//        "(select count(c.obj -> 'associatedProduct')  " +
//        " from cloud_element ce2, jsonb_array_elements(ce2.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj, pos) " +
//        " where ce2.hardware_location -> 'landingZone' = ceo.landing_zone " +
//        " ) as product, " +
//        "(select count(c.obj -> 'associatedService')  " +
//        " from cloud_element ce2, jsonb_array_elements(ce2.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj, pos) " +
//        " where ce2.hardware_location -> 'landingZone' = ceo.landing_zone and upper(cast(c.obj -> 'serviceType' as text)) = '\"APP\"' " +
//        " ) as app_service, " +
//        "(select count(c.obj -> 'associatedService')  " +
//        " from cloud_element ce2, jsonb_array_elements(ce2.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj, pos) " +
//        " where ce2.hardware_location -> 'landingZone' = ceo.landing_zone and upper(cast(c.obj -> 'serviceType' as text)) = '\"DATA\"' " +
//        " ) as data_service  " +
//        "from (select ce.cloud_environment_id, ce.hardware_location -> 'landingZone' as landing_zone, " +
//        " count(ce.hardware_location -> 'productEnclave') as product_enclave " +
//        " from  cloud_element ce group by ce.cloud_environment_id, ce.hardware_location -> 'landingZone' " +
//        " ) as ceo, " +
//        "cloud_environment cnv, department dep, organization org " +
//        "where ceo.cloud_environment_id = cnv.id  " +
//        "and cnv.department_id = dep.id " +
//        "and dep.organization_id = org.id " +
//        "and org.id = :orgId " +
//        "and cnv.cloud = :cloud " +
//        "group by cnv.cloud, ceo.landing_zone, ceo.product_enclave";
//    @Query(value = ORG_AND_CLOUD_WISE_ENV_SUMMARY_QUERY, nativeQuery = true)
//    public List<EnvironmentSummaryDto> getEnvironmentSummary(@Param("cloud") String cloud, @Param("orgId") Long orgId);

}
