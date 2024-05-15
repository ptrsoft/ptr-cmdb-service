package com.synectiks.asset.repository;

import com.synectiks.asset.domain.CloudElementCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CloudElementCost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CloudElementCostRepository extends JpaRepository<CloudElementCost, Long> {
    CloudElementCost findByCloudElementId(Long cloudElementId);

}
