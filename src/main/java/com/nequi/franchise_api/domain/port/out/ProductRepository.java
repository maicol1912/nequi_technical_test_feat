package com.nequi.franchise_api.domain.port.out;

import com.nequi.franchise_api.domain.model.entity.Product;
import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.ProductId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {

    Mono<Product> save(Product product);

    Mono<Product> findById(ProductId productId);

    Flux<Product> findByBranchId(BranchId branchId);

    Flux<Product> findAll();

    Mono<Boolean> existsById(ProductId productId);

    Mono<Boolean> existsByBranchIdAndName(BranchId branchId, String name);

    Mono<Void> deleteById(ProductId productId);

    Mono<Void> deleteByBranchId(BranchId branchId);
}
