package com.synectiks.asset.repository;

import com.synectiks.asset.domain.Landingzone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Landingzone entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LandingzoneRepository extends JpaRepository<Landingzone, Long> {}
