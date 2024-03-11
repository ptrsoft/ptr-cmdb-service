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

    String GET_SOA_SERVICE ="select be.* from business_element be where upper(be.service_name) = upper(:serviceName) \n " +
            " and upper(be.service_nature) = upper(:serviceNature) and upper(be.service_type) = upper(:serviceType) and be.product_id = :productId and be.product_env_id = :productEnvId and be.module_id = :moduleId and be.cloud_element_id = :cloudElementId ";
    @Query(value = GET_SOA_SERVICE, nativeQuery = true)
    BusinessElement getSoaService(@Param("serviceName") String serviceName, @Param("serviceNature") String serviceNature, @Param("serviceType") String serviceType,  @Param("productId") Long productId, @Param("productEnvId") Long productEnvId, @Param("moduleId") Long moduleId, @Param("cloudElementId") Long cloudElementId);

    String GET_THREE_TIER_SERVICE ="select be.* from business_element be where upper(be.service_name) = upper(:serviceName) \n " +
            " and upper(be.service_type) = upper(:serviceType) and be.product_id = :productId and be.product_env_id = :productEnvId and be.cloud_element_id = :cloudElementId  ";
    @Query(value = GET_THREE_TIER_SERVICE, nativeQuery = true)
    BusinessElement getThreeTierService(@Param("serviceName") String serviceName, @Param("serviceType") String serviceType,  @Param("productId") Long productId, @Param("productEnvId") Long productEnvId, @Param("cloudElementId") Long cloudElementId);

    String SERVICE_VIEW_TOPOLOGY_QUERY="select be.* from business_element be \n" +
            "where be.product_id in (select p.id \n" +
            " \t\t\t\t\t\t\tfrom product p \n" +
            " \t\t\t\t\t\t\twhere p.department_id in (select d.id from department d \n" +
            " \t\t\t\t\t\t\t\t\t\t\t\t\t\twhere d.id in (select l.department_id  \n" +
            " \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tfrom landingzone l \n" +
            " \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\twhere l.id = :landingZoneId )\n" +
            " \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tand upper(d.name) = upper(:deptName))\n" +
            "\t\t\t\t\t\t\tand upper(p.name) = upper(:productName)\n" +
            "\t\t\t\t\t\t\tand upper(p.type) = upper(:productType))\n" +
            "and be.product_env_id in (select pe.id from product_env pe where pe.product_id in (select p.id \n" +
            "\t\t\t\t\t\t\tfrom product p \n" +
            "\t\t\t\t\t\t\twhere p.department_id in (select d.id from department d \n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\twhere d.id in (select l.department_id  \n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tfrom landingzone l \n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\twhere l.id = :landingZoneId )\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tand upper(d.name) = upper(:deptName))\n" +
            "\t\t\t\t\t\t\tand upper(p.name) = upper(:productName)\n" +
            "\t\t\t\t\t\t\tand upper(p.type) = upper(:productType)\n" +
            "\t\t\t\t\t\t\t)\n" +
            "\t\t\t\t\t\t\tand upper(pe.name) = upper(:env))\t\t\n" +
            "and upper(be.service_nature) = upper(:serviceNature)";
    @Query(value = SERVICE_VIEW_TOPOLOGY_QUERY, nativeQuery = true)
    List<BusinessElement> getServiceViewTopology(@Param("landingZoneId") Long landingZoneId,
                                                 @Param("productName") String productName, @Param("deptName") String deptName,
                                                 @Param("env") String env, @Param("productType") String productType,
                                                 @Param("serviceNature") String serviceNature);

}
