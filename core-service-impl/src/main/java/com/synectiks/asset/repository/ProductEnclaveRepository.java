package com.synectiks.asset.repository;

import com.synectiks.asset.domain.Product;
import com.synectiks.asset.domain.ProductEnclave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ProductEnclave entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductEnclaveRepository extends JpaRepository<ProductEnclave, Long> {}
