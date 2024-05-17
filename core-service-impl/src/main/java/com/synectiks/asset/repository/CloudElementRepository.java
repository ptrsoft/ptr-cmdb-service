package com.synectiks.asset.repository;

import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.domain.query.BiMappingBusinessCloudElementQueryObj;
import com.synectiks.asset.domain.query.CloudElementTagQueryObj;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the CloudElement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CloudElementRepository extends JpaRepository<CloudElement, Long> {
    CloudElement findByInstanceId(String instanceId);
    List<CloudElement> findByCloud(String cloud);

    String CLOUD_ELEMENT_BY_LANDINGZONE_ID_AND_INSTANCE_ID_QUERY ="select ce.* from cloud_element ce where ce.instance_id = :instanceId and ce.landingzone_id = :landingZoneId ";
    @Query(value = CLOUD_ELEMENT_BY_LANDINGZONE_ID_AND_INSTANCE_ID_QUERY, nativeQuery = true)
    CloudElement getCloudElementByLandingZoneAndInstanceId(@Param("landingZoneId") Long landingZoneId, @Param("instanceId") String instanceId);


    String CLOUD_ELEMENT_QUERY ="select ce.* from cloud_element ce where ce.landingzone_id = :landingZoneId and upper(ce.element_type) = upper(:elementType) and ce.arn = :arn  ";
    @Query(value = CLOUD_ELEMENT_QUERY, nativeQuery = true)
    CloudElement getCloudElementByArn(@Param("landingZoneId") Long landingZoneId,
                                      @Param("arn") String arn,
                                      @Param("elementType") String elementType);

    String CLOUD_ELEMENT_BY_INSTANCE_ID_QUERY ="select ce.* from cloud_element ce where ce.landingzone_id = :landingZoneId and upper(ce.element_type) = upper(:elementType) and ce.instance_id = :instanceId  ";
    @Query(value = CLOUD_ELEMENT_BY_INSTANCE_ID_QUERY, nativeQuery = true)
    CloudElement getCloudElementByInstanceId(@Param("landingZoneId") Long landingZoneId,
                                 @Param("instanceId") String instanceId,
                                 @Param("elementType") String elementType);


    String CLOUD_ELEMENT_TAG_QUERY ="select ce.id, ce.landingzone_id,  ce.instance_id, c.obj -> 'tag' as tag  \n" +
            " from cloud_element ce, jsonb_array_elements(ce.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj, pos)\n" +
            " where ce.landingzone_id = :landingZoneId and ce.instance_id = :instanceId and c.obj -> 'tag' is not null order by c.obj -> 'tag' asc";
    @Query(value = CLOUD_ELEMENT_TAG_QUERY, nativeQuery = true)
    List<CloudElementTagQueryObj> getCloudElementTag(@Param("landingZoneId") Long landingZoneId, @Param("instanceId") String instanceId);


    String SEARCH_CLOUD_ELEMENT_FOR_TAG_QUERY ="select ce.*   \n" +
            " from cloud_element ce, jsonb_array_elements(ce.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj, pos)\n" +
            " where ce.landingzone_id = :landingZoneId and ce.instance_id = :instanceId and cast (c.obj ->> 'serviceId' as int) = :serviceId " ;
    @Query(value = SEARCH_CLOUD_ELEMENT_FOR_TAG_QUERY, nativeQuery = true)
    CloudElement getCloudElementForTag(@Param("landingZoneId") Long landingZoneId,
                                       @Param("serviceId") Long serviceId,
                                       @Param("instanceId") String instanceId);

    String CLOUD_ELEMENT_BY_LANDING_ZONE_ID_QUERY =" select ce.* from cloud_element ce where ce.landingzone_id in :landingZoneIdList ";
    @Query(value = CLOUD_ELEMENT_BY_LANDING_ZONE_ID_QUERY, nativeQuery = true)
    List<CloudElement> getCloudElementsByLandingZoneIds(@Param("landingZoneIdList") List<Long> landingZoneIdList);


    String BI_MAPPING_CLOUD_ELEMENT_INSTANCES="select distinct ce.id, be.id as  business_element_id, be.service_name, be.service_nature, be.service_type,  \n" +
            "ce.element_type, ce.arn, ce.instance_id, ce.instance_name, ce.category,ce.landingzone_id, ce.db_category_id, ce.product_enclave_id, pe.instance_id as product_enclave_instance_id, \n" +
            "ce.status, ce.created_by, ce.created_on, ce.updated_by, ce.updated_on, \n" +
            "ce.log_location, ce.trace_location, ce.metric_location, l.landing_zone, l.cloud, dc.name as db_category_name, \n" +
            "null as view_json, null as config_json, null as hosted_services,\n" +
            "ce.service_category,ce.region, ce.log_group \n" +
            " from cloud_element ce left join jsonb_array_elements(ce.hosted_services -> 'HOSTEDSERVICES') with ordinality c(obj) on 1 = 1\n" +
            "left join business_element be on cast(c.obj -> 'serviceId' as int) = be.id \n" +
            "left join product_enclave pe on ce.product_enclave_id = pe.id\n" +
            "left join landingzone l on ce.landingzone_id = l.id\n" +
            "left join db_category dc on ce.db_category_id = dc.id\n" +
            "where \n " +
            " cast(c.obj -> 'tag' -> 'org' ->> 'id' as int) = :orgId \n" +
            "and cast(c.obj -> 'tag' -> 'org' -> 'dep' ->> 'id' as int) = :departmentId \n" +
            "and cast(c.obj -> 'tag' -> 'org' -> 'dep' -> 'product' ->> 'id' as int) = :productId \n" +
            "and cast(c.obj -> 'tag' -> 'org' -> 'dep' -> 'product' -> 'productEnv' ->> 'id' as int) = :productEnvId \n" +
            "and upper(ce.element_type) = upper(:elementType) " +
            "order by ce.element_type asc";
    @Query(value = BI_MAPPING_CLOUD_ELEMENT_INSTANCES,nativeQuery = true)
    List<BiMappingBusinessCloudElementQueryObj> getBiMappingCloudElementInstances(@Param("orgId") Long orgId, @Param("departmentId") Long departmentId, @Param("productId") Long productId, @Param("productEnvId") Long productEnvId, @Param("elementType") String elementType);

    String GET_ALL_ELEMENTS_OF_ORG="select ce.* " +
            "FROM \n" +
            "\tcloud_element ce, \n" +
            "\tlandingzone l, \n" +
            "\tdepartment d, \n" +
            "\torganization o\n" +
            "WHERE \n" +
            "\tl.department_id = d.id \n" +
            "\tAND d.organization_id = o.id \n" +
            "\tAND ce.landingzone_id = l.id \n" +
            "\tAND o.id = :orgId order by ce.element_type asc";
    @Query(value = GET_ALL_ELEMENTS_OF_ORG, nativeQuery = true)
    Page<CloudElement> getAllCloudElementsOfOrganization(@Param("orgId") Long orgId, Pageable pageable);
}
