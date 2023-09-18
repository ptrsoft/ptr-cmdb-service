package com.synectiks.asset.repository;

import com.synectiks.asset.domain.ProductEnclave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductEnclave entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductEnclaveRepository extends JpaRepository<ProductEnclave, Long> {
    String PRODUCT_ENCLAVE_QUERY ="select pe.* from product_enclave pe where pe.instance_id = :instanceId and pe.department_id = :departmentId and pe.landingzone_id = :landingZoneId ";
    @Query(value = PRODUCT_ENCLAVE_QUERY, nativeQuery = true)
    ProductEnclave findProductEnclave(@Param("instanceId") String instanceId,
                                       @Param("departmentId") Long departmentId,
                                       @Param("landingZoneId") Long landingZoneId);
}
