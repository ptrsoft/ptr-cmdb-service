package com.synectiks.asset.repository;

import com.synectiks.asset.domain.CloudElementSupportedApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the CloudElementSupportedApi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CloudElementSupportedApiRepository extends JpaRepository<CloudElementSupportedApi, Long> {
    List<CloudElementSupportedApi> findByCloud(String cloud);
    List<CloudElementSupportedApi> findByCloudAndElementType(String cloud, String elementType);
    List<CloudElementSupportedApi> findByName(String name);
    List<CloudElementSupportedApi> findByElementTypeAndName(String elementType, String name);
    CloudElementSupportedApi findByCloudAndElementTypeAndName(String cloud, String elementType, String name);
}
