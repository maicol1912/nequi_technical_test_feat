package com.nequi.franchise_api.infrastructure.adapter.out.persistence.repository;

import com.nequi.franchise_api.infrastructure.dto.response.ProductMaxStockProjection;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductQueryRepository extends ReactiveCrudRepository<ProductMaxStockProjection, String> {

    @Query("""
        SELECT p.id, 
               p.branch_id, 
               p.name, 
               p.stock, 
               p.created_at, 
               p.updated_at, 
               b.name as branch_name 
        FROM products p 
        JOIN branches b ON p.branch_id = b.id 
        WHERE b.franchise_id = CAST(:franchiseId AS UUID) 
        AND p.stock = (
            SELECT MAX(p2.stock) 
            FROM products p2 
            JOIN branches b2 ON p2.branch_id = b2.id 
            WHERE b2.franchise_id = CAST(:franchiseId AS UUID)
        )
    """)
    Flux<ProductMaxStockProjection> findProductsWithMaxStockByFranchise(String franchiseId);
}