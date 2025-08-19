package com.nequi.franchise_api.infrastructure.adapter.out.persistence.repository;

import com.nequi.franchise_api.infrastructure.adapter.out.persistence.entity.BranchEntity;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.projection.FranchiseRawData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface FranchiseQueryR2dbcRepository extends R2dbcRepository<FranchiseRawData, UUID>  {

    @Query("""
    SELECT
        f.id::text as franchise_id,
        f.name as franchise_name,
        b.id::text as branch_id,
        b.name as branch_name,
        p.id::text as product_id,
        p.name as product_name,
        p.stock as product_stock
    FROM franchises f
    LEFT JOIN branches b ON f.id = b.franchise_id
    LEFT JOIN products p ON b.id = p.branch_id
    WHERE f.id = :franchiseId::UUID
    ORDER BY f.id, b.id, p.id
    """)
    Flux<FranchiseRawData> findFranchiseWithBranchesAndProducts(String franchiseId);
}