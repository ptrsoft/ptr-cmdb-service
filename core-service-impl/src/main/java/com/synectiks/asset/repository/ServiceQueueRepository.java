package com.synectiks.asset.repository;

import com.synectiks.asset.domain.ServiceQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Config entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceQueueRepository extends JpaRepository<ServiceQueue, Long> {
    ServiceQueue findByKey(String key);
    List<ServiceQueue> findByKeyAndStatus(String key, String status);

}
