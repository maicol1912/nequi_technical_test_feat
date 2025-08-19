package com.nequi.franchise_api.infrastructure.adapter.out.persistence.repository;

import com.nequi.franchise_api.infrastructure.adapter.out.persistence.entity.BranchEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface BranchR2dbcRepository extends R2dbcRepository<BranchEntity, UUID> {

    @Query("SELECT * FROM branches WHERE franchise_id = :franchiseId ORDER BY name")
    Flux<BranchEntity> findByFranchiseIdOrderByName(UUID franchiseId);

    @Query("SELECT EXISTS(SELECT 1 FROM branches WHERE franchise_id = :franchiseId AND LOWER(name) = LOWER(:name))")
    Mono<Boolean> existsByFranchiseIdAndNameIgnoreCase(UUID franchiseId, String name);

    @Query("DELETE FROM branches WHERE franchise_id = :franchiseId")
    Mono<Void> deleteByFranchiseId(UUID franchiseId);

    @Query("SELECT COUNT(*) FROM branches WHERE franchise_id = :franchiseId")
    Mono<Long> countByFranchiseId(UUID franchiseId);
}
