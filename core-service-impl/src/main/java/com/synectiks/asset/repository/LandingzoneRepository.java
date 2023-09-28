package com.synectiks.asset.repository;

import com.synectiks.asset.domain.Landingzone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Landingzone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LandingzoneRepository extends JpaRepository<Landingzone, Long> {

    String LANDING_ZONE_QUERY ="select l.* from landingzone l\n" +
            "where l.landing_zone = :landingZone and upper(l.cloud) = upper(:cloud)\n" +
            "and l.department_id = (select d.id from department d where upper(d.\"name\") = upper(:department)\n" +
            " and d.organization_id = (select o.id from organization o where upper(o.\"name\") = upper(:organization) ))\n";
    @Query(value = LANDING_ZONE_QUERY, nativeQuery = true)
    List<Landingzone> getLandingZone(@Param("organization") String organization,
                                                     @Param("department") String department,
                                                     @Param("cloud") String cloud,
                                                     @Param("landingZone") String landingZone);

    String LANDING_ZONE_BY_ORG_ID_QUERY ="select l.* from landingzone l where l.department_id in (select d.id from department d where d.organization_id = :orgId) and upper(l.cloud) = upper(:cloud)";
    @Query(value = LANDING_ZONE_BY_ORG_ID_QUERY, nativeQuery = true)
    List<Landingzone> getLandingZoneByOrgId(@Param("orgId") Long orgId, @Param("cloud") String cloud);

}
