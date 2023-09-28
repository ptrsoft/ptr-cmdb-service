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
}
