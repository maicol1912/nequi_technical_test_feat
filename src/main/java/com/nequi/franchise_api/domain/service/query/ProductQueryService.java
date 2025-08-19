package com.nequi.franchise_api.domain.service.query;

import com.nequi.franchise_api.domain.exception.ProductNotFoundException;
import com.nequi.franchise_api.domain.model.entity.Product;
import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.ProductId;
import com.nequi.franchise_api.domain.port.out.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductQueryService {

    private final ProductRepository productRepository;

    public Mono<Product> getProduct(String productId) {
        log.debug("Getting product: {}", productId);

        ProductId id = ProductId.of(productId);

        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .doOnSuccess(product -> log.debug("Product found: {}", product.getId()));
    }

    public Flux<Product> getAllProducts() {
        log.debug("Getting all products");

        return productRepository.findAll()
                .doOnComplete(() -> log.debug("All products retrieved"));
    }

    public Flux<Product> getProductByBranch(String branchId) {
        log.debug("Getting product by branchId {}",branchId);

        BranchId id = BranchId.of(branchId);
        return productRepository.findByBranchId(id)
                .doOnComplete(() -> log.debug("All products by branch retrieved"));
    }
}
