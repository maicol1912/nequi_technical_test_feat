package com.nequi.franchise_api.domain.service.command;

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
import com.nequi.franchise_api.domain.port.out.BranchRepository;
import com.nequi.franchise_api.domain.port.out.EventPublisher;
import com.nequi.franchise_api.domain.port.out.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCommandService {

    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final EventPublisher eventPublisher;

    public Mono<Product> addProduct(String branchId, String name, Integer stock) {
        log.debug("Adding product to branch {} with name: {} and stock: {}", branchId, name, stock);

        BranchId branId = BranchId.of(branchId);
        Name productName = Name.of(name);
        Stock productStock = Stock.of(stock);

        return branchRepository.existsById(branId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new BranchNotFoundException(branId));
                    }

                    return productRepository.existsByBranchIdAndName(branId, name)
                            .flatMap(productExists -> {
                                if (productExists) {
                                    return Mono.error(new IllegalArgumentException("Product with name '" + name + "' already exists in this branch"));
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

    public Mono<Product> updateProduct(String productId, String name) {
        log.debug("Updating product {} with name: {}", productId, name);

        ProductId id = ProductId.of(productId);
        Name newName = Name.of(name);

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

    public Mono<Product> updateStock(String productId, Integer stock) {
        log.debug("Updating product {} stock to: {}", productId, stock);

        ProductId id = ProductId.of(productId);
        Stock newStock = Stock.of(stock);

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

    public Mono<Void> removeProduct(String productId) {
        log.debug("Removing product: {}", productId);

        ProductId id = ProductId.of(productId);

        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)))
                .flatMap(product -> {
                    return productRepository.deleteById(id)
                            .doOnSuccess(unused -> {
                                log.info("Product removed successfully: {}", productId);
                                eventPublisher.publishEvent(
                                        new ProductRemovedEvent(this, product.getBranchId(), id,
                                                product.getName().getValue(), product.getStock().getValue())
                                );
                            });
                });
    }
}
