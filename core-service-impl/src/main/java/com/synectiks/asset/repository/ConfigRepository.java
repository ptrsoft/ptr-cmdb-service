package com.synectiks.asset.repository;

import com.synectiks.asset.domain.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Config entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {
    Config findByKey(String key);
}
