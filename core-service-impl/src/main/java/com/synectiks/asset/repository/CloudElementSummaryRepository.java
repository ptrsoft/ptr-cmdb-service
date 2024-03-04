package com.synectiks.asset.repository;

import com.synectiks.asset.domain.CloudElementSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the CloudElementSummary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CloudElementSummaryRepository extends JpaRepository<CloudElementSummary, Long> {

    String CLOUD_ELEMENT_SUMMARY_QUERY ="select * from cloud_element_summary ces where ces.landingzone_id = :landingZoneId";
    @Query(value = CLOUD_ELEMENT_SUMMARY_QUERY, nativeQuery = true)
    List<CloudElementSummary> getCloudElementSummary(@Param("landingZoneId") Long landingZoneId);

    CloudElementSummary findByLandingzoneId(Long landingzoneId);
}
