package com.nequi.franchise_api.domain.port.in.query;

import com.nequi.franchise_api.domain.model.entity.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductQueryUseCase {

    Flux<Product> getAllProducts();

    Mono<Product> getProduct(GetProductQuery query);

    Flux<Product> getProductsByBranch(GetProductsByBranchQuery query);

    record GetProductQuery(String productId) {
        public GetProductQuery {
            if (productId == null || productId.trim().isEmpty()) {
                throw new IllegalArgumentException("Product ID cannot be null or empty");
            }
        }
    }

    record GetProductsByBranchQuery(String branchId) {
        public GetProductsByBranchQuery {
            if (branchId == null || branchId.trim().isEmpty()) {
                throw new IllegalArgumentException("Branch ID cannot be null or empty");
            }
        }
    }
}