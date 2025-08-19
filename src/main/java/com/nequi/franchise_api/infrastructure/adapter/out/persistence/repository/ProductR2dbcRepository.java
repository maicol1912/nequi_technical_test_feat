package com.nequi.franchise_api.infrastructure.adapter.out.persistence.repository;

import com.nequi.franchise_api.infrastructure.adapter.out.persistence.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ProductR2dbcRepository extends R2dbcRepository<ProductEntity, UUID> {

    @Query("SELECT * FROM products WHERE branch_id = :branchId ORDER BY name")
    Flux<ProductEntity> findByBranchIdOrderByName(UUID branchId);

    @Query("SELECT EXISTS(SELECT 1 FROM products WHERE branch_id = :branchId AND LOWER(name) = LOWER(:name))")
    Mono<Boolean> existsByBranchIdAndNameIgnoreCase(UUID branchId, String name);

    @Query("DELETE FROM products WHERE branch_id = :branchId")
    Mono<Void> deleteByBranchId(UUID branchId);

    @Query("SELECT COUNT(*) FROM products WHERE branch_id = :branchId")
    Mono<Long> countByBranchId(UUID branchId);
}
