package com.nequi.franchise_api.application.service;

import com.nequi.franchise_api.domain.model.entity.Product;
import com.nequi.franchise_api.domain.port.in.command.ProductCommandUseCase;
import com.nequi.franchise_api.domain.port.in.query.ProductQueryUseCase;
import com.nequi.franchise_api.domain.service.command.ProductCommandService;
import com.nequi.franchise_api.domain.service.query.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductApplicationService implements ProductQueryUseCase, ProductCommandUseCase {

    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    @Override
    public Mono<Product> addProduct(AddProductCommand command) {
        return productCommandService.addProduct(command.branchId(), command.name(), command.stock());
    }

    @Override
    public Mono<Product> updateProduct(UpdateProductCommand command) {
        return productCommandService.updateProduct(command.productId(), command.name());
    }

    @Override
    public Mono<Product> updateStock(UpdateStockCommand command) {
        return productCommandService.updateStock(command.productId(), command.stock());
    }

    @Override
    public Mono<Void> removeProduct(RemoveProductCommand command) {
        return productCommandService.removeProduct(command.productId());
    }

    @Override
    public Mono<Product> getProduct(GetProductQuery query) {
        return productQueryService.getProduct(query.productId());
    }

    @Override
    public Flux<Product> getProductsByBranch(GetProductsByBranchQuery query) {
        return productQueryService.getProductByBranch(query.branchId());
    }

    @Override
    public Flux<Product> getAllProducts() {
        return productQueryService.getAllProducts();
    }
}
