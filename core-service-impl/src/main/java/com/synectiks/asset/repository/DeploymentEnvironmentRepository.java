package com.synectiks.asset.repository;

import com.synectiks.asset.business.domain.DeploymentEnvironment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DeploymentEnvironment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeploymentEnvironmentRepository extends JpaRepository<DeploymentEnvironment, Long> {}
