package com.synectiks.asset.repository;

import com.synectiks.asset.domain.BusinessElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the BusinessElement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessElementRepository extends JpaRepository<BusinessElement, Long> {

    String SEARCH_BY_FILTER_SOA_ASSOCIATION ="select distinct be.*\n " +
            "from department d, product p, product_env pe, module m, business_element be  \n" +
            "where d.id  = p.department_id \n " +
            "and p.id = pe.product_id\n " +
            "and pe.id = m.product_env_id and p.id = m.product_id \n " +
            "and be.module_id = m.id and be.product_env_id = pe.id and be.product_id = p.id\n " +
            "and d.id = :departmentId and p.id = :productId and pe.id = :productEnvId\n " +
            "and be.module_id = :moduleId \n" +
            "and upper(be.service_nature) = upper(:serviceNature) ";
    @Query(value = SEARCH_BY_FILTER_SOA_ASSOCIATION, nativeQuery = true)
    List<BusinessElement> searchByFiltersForSoaAssociation(@Param("departmentId") Long departmentId, @Param("productId") Long productId, @Param("productEnvId") Long productEnvId, @Param("moduleId") Long moduleId, @Param("serviceNature") String serviceNature);

    String SEARCH_BY_FILTER_3TIER_ASSOCIATION ="select distinct be.* from business_element be, product p, department d, product_env pe  \n" +
            "where be.product_id = p.id \n" +
            "and p.department_id = d.id \n" +
            "and be.product_env_id = pe.id and pe.product_id = p.id \n" +
            "and d.id = :departmentId and p.id = :productId and pe.id = :productEnvId and upper(be.service_type) = upper(:serviceType)";
    @Query(value = SEARCH_BY_FILTER_3TIER_ASSOCIATION, nativeQuery = true)
    List<BusinessElement> searchByFiltersFor3TierAssociation(@Param("departmentId") Long departmentId, @Param("productId") Long productId, @Param("productEnvId") Long productEnvId, @Param("serviceType") String serviceType);

}
