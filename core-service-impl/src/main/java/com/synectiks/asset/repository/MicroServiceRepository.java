package com.synectiks.asset.repository;

import com.synectiks.asset.domain.MicroService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MicroService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MicroServiceRepository extends JpaRepository<MicroService, Long> {}
