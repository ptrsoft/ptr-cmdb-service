package com.synectiks.asset.repository;

import com.synectiks.asset.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    String GET_DEPARTMENT_QUERY ="select d.* from department d where upper(d.name) = upper(:departmentName) and d.organization_id = :orgId ";
    @Query(value = GET_DEPARTMENT_QUERY, nativeQuery = true)
    Department getDepartment(@Param("departmentName") String departmentName, @Param("orgId") Long orgId);
}
