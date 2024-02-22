package com.synectiks.asset.repository;

import com.synectiks.asset.domain.BiService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the BiService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BiServiceRepository extends JpaRepository<BiService, Long> {
    List<BiService> findByProductCategory(String productCategory);
    List<BiService> findByServiceCategory(String serviceCategory);

    List<BiService> findByName(String name);
    List<BiService> findByStatus(String status);

    /**
     *
     * @param productCategory 3 tier/soa
     * @param serviceCategory 3 tier (web/app/data/aux), soa (app/data/other)
     * @return List<BiService>
     */
    List<BiService> findByProductCategoryAndServiceCategory(String productCategory, String serviceCategory);



}
