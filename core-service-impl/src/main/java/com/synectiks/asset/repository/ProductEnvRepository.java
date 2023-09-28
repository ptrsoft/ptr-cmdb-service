package com.synectiks.asset.repository;

import com.synectiks.asset.domain.ProductEnv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductEnv entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductEnvRepository extends JpaRepository<ProductEnv, Long> {
    String GET_ENV_QUERY ="select pe.* from product_env pe where upper(pe.name) = upper(:envName) and pe.product_id = :productId ";
    @Query(value = GET_ENV_QUERY, nativeQuery = true)
    ProductEnv getProductEnv(@Param("envName") String envName, @Param("productId") Long productId);
}
