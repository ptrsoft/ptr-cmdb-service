package com.synectiks.asset.repository;

import com.synectiks.asset.domain.DbCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DeploymentEnvironment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DbCategoryRepository extends JpaRepository<DbCategory, Long> {
    DbCategory findByName(String name);
}
