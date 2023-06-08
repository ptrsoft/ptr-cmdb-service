package com.synectiks.asset.repository;

import com.synectiks.asset.business.domain.CloudElementSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CloudElementSummary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CloudElementSummaryRepository extends JpaRepository<CloudElementSummary, Long> {}
