package com.nequi.franchise_api.infrastructure.adapter.out.persistence.adapter;

import com.nequi.franchise_api.domain.model.entity.Product;
import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.ProductId;
import com.nequi.franchise_api.domain.port.out.ProductRepository;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.repository.ProductR2dbcRepository;
import com.nequi.franchise_api.infrastructure.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductR2dbcRepository productR2dbcRepository;
    private final ProductMapper productMapper;

    @Override
    public Mono<Product> save(Product product) {
        log.debug("Saving product: {}", product.getId());
        return Mono.fromCallable(() -> productMapper.toEntity(product))
                .flatMap(productR2dbcRepository::save)
                .map(productMapper::toDomain)
                .doOnSuccess(saved -> log.debug("Product saved: {}", saved.getId()));
    }

    @Override
    public Mono<Product> findById(ProductId productId) {
        log.debug("Finding product by ID: {}", productId);
        return productR2dbcRepository.findById(productId.getValue())
                .map(productMapper::toDomain)
                .doOnNext(found -> log.debug("Product found: {}", found.getId()));
    }

    @Override
    public Flux<Product> findByBranchId(BranchId branchId) {
        log.debug("Finding products by branch ID: {}", branchId);
        return productR2dbcRepository.findByBranchIdOrderByName(branchId.getValue())
                .map(productMapper::toDomain)
                .doOnComplete(() -> log.debug("Products retrieved for branch: {}", branchId));
    }

    @Override
    public Flux<Product> findAll() {
        log.debug("Finding all products");
        return productR2dbcRepository.findAll()
                .map(productMapper::toDomain)
                .doOnComplete(() -> log.debug("All products retrieved"));
    }

    @Override
    public Mono<Boolean> existsById(ProductId productId) {
        log.debug("Checking if product exists: {}", productId);
        return productR2dbcRepository.existsById(productId.getValue());
    }

    @Override
    public Mono<Boolean> existsByBranchIdAndName(BranchId branchId, String name) {
        log.debug("Checking if product exists by branch ID and name: {} - {}", branchId, name);
        return productR2dbcRepository.existsByBranchIdAndNameIgnoreCase(branchId.getValue(), name);
    }

    @Override
    public Mono<Void> deleteById(ProductId productId) {
        log.debug("Deleting product: {}", productId);
        return productR2dbcRepository.deleteById(productId.getValue())
                .doOnSuccess(unused -> log.debug("Product deleted: {}", productId));
    }

    @Override
    public Mono<Void> deleteByBranchId(BranchId branchId) {
        log.debug("Deleting products by branch ID: {}", branchId);
        return productR2dbcRepository.deleteByBranchId(branchId.getValue())
                .doOnSuccess(unused -> log.debug("Products deleted for branch: {}", branchId));
    }
}
