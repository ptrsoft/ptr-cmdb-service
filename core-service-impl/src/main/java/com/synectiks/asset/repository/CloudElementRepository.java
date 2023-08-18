package com.synectiks.asset.repository;

import com.synectiks.asset.domain.CloudElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CloudElement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CloudElementRepository extends JpaRepository<CloudElement, Long> {
    CloudElement findByInstanceId(String instanceId);
}
