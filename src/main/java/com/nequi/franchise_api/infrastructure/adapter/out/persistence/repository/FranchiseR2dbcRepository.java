package com.nequi.franchise_api.infrastructure.adapter.out.persistence.repository;

import com.nequi.franchise_api.infrastructure.adapter.out.persistence.entity.FranchiseEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface FranchiseR2dbcRepository extends R2dbcRepository<FranchiseEntity, UUID> {

    @Query("SELECT EXISTS(SELECT 1 FROM franchises WHERE LOWER(name) = LOWER(:name))")
    Mono<Boolean> existsByNameIgnoreCase(String name);

    @Query("SELECT * FROM franchises WHERE LOWER(name) = LOWER(:name)")
    Mono<FranchiseEntity> findByNameIgnoreCase(String name);
}
