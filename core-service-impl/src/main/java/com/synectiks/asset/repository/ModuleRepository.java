package com.synectiks.asset.repository;

import com.synectiks.asset.domain.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Module entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    String SEARCH_BY_FILTER ="select distinct m.*\n " +
            "from department d, product p, product_env pe, module m, business_element be  \n" +
            "where d.id  = p.department_id \n " +
            "and p.id = pe.product_id\n " +
            "and pe.id = m.product_env_id and p.id = m.product_id \n " +
            "and be.module_id = m.id and be.product_env_id = pe.id and be.product_id = p.id\n " +
            "and d.id = :departmentId and p.id = :productId and pe.id = :productEnvId\n " +
            "and upper(be.service_nature) = upper(:serviceNature) ";
    @Query(value = SEARCH_BY_FILTER, nativeQuery = true)
    List<Module> searchByFilters(@Param("departmentId") Long departmentId, @Param("productId") Long productId, @Param("productEnvId") Long productEnvId, @Param("serviceNature") String serviceNature);

    String GET_MODULE_QUERY ="select m.* from module m where upper(m.name) = upper(:moduleName) and m.product_id = :productId and m.product_env_id = :productEnvId ";
    @Query(value = GET_MODULE_QUERY, nativeQuery = true)
    Module getModule(@Param("moduleName") String moduleName, @Param("productId") Long productId, @Param("productEnvId") Long productEnvId);

}
