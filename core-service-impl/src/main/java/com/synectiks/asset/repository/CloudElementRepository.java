package com.synectiks.asset.repository;

import com.synectiks.asset.domain.CloudElement;
import com.synectiks.asset.domain.query.CloudElementTagQueryObj;
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

    String CLOUD_ELEMENT_QUERY ="select ce.* from cloud_element ce where ce.landingzone_id in ( " +
            " select l.id from landingzone l\n" +
            " where l.landing_zone = :landingZone and upper(l.cloud) = upper(:cloud)\n" +
            " and l.department_id = (select d.id from department d where upper(d.\"name\") = upper(:department)\n" +
            " and d.organization_id = (select o.id from organization o where upper(o.\"name\") = upper(:organization) )))\n" +
            " and upper(ce.element_type) = upper('lambda')  " +
            " and ce.arn = :arn " +
            " order by ce.id asc ";
    @Query(value = CLOUD_ELEMENT_QUERY, nativeQuery = true)
    List<CloudElement> getCloudElement(@Param("organization") String organization,
                                                     @Param("department") String department,
                                                     @Param("cloud") String cloud,
                                                     @Param("landingZone") String landingZone,
                                                     @Param("arn") String arn);


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
}
