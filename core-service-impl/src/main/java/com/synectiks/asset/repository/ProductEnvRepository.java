package com.synectiks.asset.repository;

import com.synectiks.asset.domain.ProductEnv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductEnv entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductEnvRepository extends JpaRepository<ProductEnv, Long> {}
