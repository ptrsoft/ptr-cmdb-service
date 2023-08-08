package com.synectiks.asset.repository;

import com.synectiks.asset.domain.BusinessElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BusinessElement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessElementRepository extends JpaRepository<BusinessElement, Long> {}
