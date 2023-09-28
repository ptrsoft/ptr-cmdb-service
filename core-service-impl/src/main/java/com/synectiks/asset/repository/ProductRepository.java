package com.synectiks.asset.repository;

import com.synectiks.asset.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    String GET_PRODUCT_QUERY ="select p.* from product p where upper(p.name) = upper(:productName) and p.department_id = :departmentId and p.organization_id = :orgId ";
    @Query(value = GET_PRODUCT_QUERY, nativeQuery = true)
    Product getProduct(@Param("productName") String productName, @Param("departmentId") Long departmentId, @Param("orgId") Long orgId);
}
