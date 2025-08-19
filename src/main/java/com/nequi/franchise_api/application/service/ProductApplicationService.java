package com.nequi.franchise_api.application.service;

import com.nequi.franchise_api.domain.exception.BranchNotFoundException;
import com.nequi.franchise_api.domain.exception.ProductNotFoundException;
import com.nequi.franchise_api.domain.model.entity.Product;
import com.nequi.franchise_api.domain.model.event.ProductAddedEvent;
import com.nequi.franchise_api.domain.model.event.ProductRemovedEvent;
import com.nequi.franchise_api.domain.model.event.ProductUpdatedEvent;
import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.Name;
import com.nequi.franchise_api.domain.model.valueobject.ProductId;
import com.nequi.franchise_api.domain.model.valueobject.Stock;
import com.nequi.franchise_api.domain.port.in.command.ProductCommandUseCase;
import com.nequi.franchise_api.domain.port.in.query.ProductQueryUseCase;
import com.nequi.franchise_api.domain.port.out.BranchRepository;
import com.nequi.franchise_api.domain.port.out.EventPublisher;
import com.nequi.franchise_api.domain.port.out.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductApplicationService implements ProductQueryUseCase, ProductCommandUseCase {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final EventPublisher eventPublisher;

    @Override
    public Mono<Product> addProduct(AddProductCommand command) {
        log.debug("Adding product to branch {} with name: {} and stock: {}", command.branchId(), command.name(), command.stock());

        BranchId branId = BranchId.of(command.branchId());
        Name productName = Name.of(command.name());
        Stock productStock = Stock.of(command.stock());

        return branchRepository.existsById(branId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BranchNotFoundException(branId));
                    }

                    return productRepository.existsByBranchIdAndName(branId, command.name())
                            .flatMap(productExists -> {
                                if (productExists) {
                                    return Mono.error(new IllegalArgumentException("Product with name '" + command.name() + "' already exists in this branch"));
                                }

                                Product product = Product.create(branId, productName, productStock);

                                return productRepository.save(product)
                                        .doOnSuccess(savedProduct -> {
                                            log.info("Product added successfully: {}", savedProduct.getId());
                                            eventPublisher.publishEvent(
                                                    new ProductAddedEvent(this, branId, savedProduct.getId(), savedProduct.getName().getValue(), savedProduct.getStock().getValue())
                                            );
                                        });
                            });
                });
    }

    @Override
    public Mono<Product> updateProduct(UpdateProductCommand command) {
        log.debug("Updating product {} with name: {}", command.productId(), command.name());

        ProductId id = ProductId.of(command.productId());
        Name newName = Name.of(command.name());

        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .flatMap(product -> {
                    String oldName = product.getName().getValue();
                    product.updateName(newName);

                    return productRepository.save(product)
                            .doOnSuccess(updatedProduct -> {
                                log.info("Product updated successfully: {}", updatedProduct.getId());
                                eventPublisher.publishEvent(
                                        new ProductUpdatedEvent(this, id, oldName, newName.getValue(),
                                                updatedProduct.getStock().getValue(), updatedProduct.getStock().getValue())
                                );
                            });
                });
    }

    @Override
    public Mono<Product> updateStock(UpdateStockCommand command) {
        log.debug("Updating product {} stock to: {}", command.productId(), command.stock());

        ProductId id = ProductId.of(command.productId());
        Stock newStock = Stock.of(command.stock());

        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .flatMap(product -> {
                    Integer oldStock = product.getStock().getValue();
                    product.updateStock(newStock);

                    return productRepository.save(product)
                            .doOnSuccess(updatedProduct -> {
                                log.info("Product stock updated successfully: {}", updatedProduct.getId());
                                eventPublisher.publishEvent(
                                        new ProductUpdatedEvent(this, id, product.getName().getValue(), product.getName().getValue(),
                                                oldStock, newStock.getValue())
                                );
                            });
                });
    }

    @Override
    public Mono<Void> removeProduct(RemoveProductCommand command) {
        log.debug("Removing product: {}", command.productId());

        ProductId id = ProductId.of(command.productId());

        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .flatMap(product -> {
                    return productRepository.deleteById(id)
                            .doOnSuccess(unused -> {
                                log.info("Product removed successfully: {}", command.productId());
                                eventPublisher.publishEvent(
                                        new ProductRemovedEvent(this, product.getBranchId(), id,
                                                product.getName().getValue(), product.getStock().getValue())
                                );
                            });
                });
    }

    @Override
    public Mono<Product> getProduct(GetProductQuery query) {
        log.debug("Getting product: {}", query.productId());

        ProductId id = ProductId.of(query.productId());

        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .doOnSuccess(product -> log.debug("Product found: {}", product.getId()));
    }

    @Override
    public Flux<Product> getProductsByBranch(GetProductsByBranchQuery query) {
        log.debug("Getting product by branchId {}",query.branchId());

        BranchId id = BranchId.of(query.branchId());
        return productRepository.findByBranchId(id)
                .doOnComplete(() -> log.debug("All products by branch retrieved"));
    }

    @Override
    public Flux<Product> getAllProducts() {

        log.debug("Getting all products");

        return productRepository.findAll()
                .doOnComplete(() -> log.debug("All products retrieved"));
    }
}
