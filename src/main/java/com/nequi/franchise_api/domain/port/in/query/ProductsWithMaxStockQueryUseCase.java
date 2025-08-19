package com.nequi.franchise_api.domain.port.in.query;

import com.nequi.franchise_api.domain.model.aggregate.ProductMaxStock;
import reactor.core.publisher.Flux;

public interface ProductsWithMaxStockQueryUseCase {

    Flux<ProductMaxStock> getProductsWithMaxStock(GetProductsWithMaxStockQuery query);

    record GetProductsWithMaxStockQuery(String franchiseId) {
        public GetProductsWithMaxStockQuery {
            if (franchiseId == null || franchiseId.trim().isEmpty()) {
                throw new IllegalArgumentException("Franchise ID cannot be null or empty");
            }
        }
    }
}
