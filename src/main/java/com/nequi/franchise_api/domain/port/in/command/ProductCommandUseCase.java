package com.nequi.franchise_api.domain.port.in.command;

import com.nequi.franchise_api.domain.model.entity.Product;
import reactor.core.publisher.Mono;

public interface ProductCommandUseCase {

    Mono<Product> addProduct(AddProductCommand command);

    Mono<Product> updateProduct(UpdateProductCommand command);

    Mono<Product> updateStock(UpdateStockCommand command);

    Mono<Void> removeProduct(RemoveProductCommand command);

    record AddProductCommand(String branchId, String name, Integer stock) {
        public AddProductCommand {
            if (branchId == null || branchId.trim().isEmpty()) {
                throw new IllegalArgumentException("Branch ID cannot be null or empty");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Product name cannot be null or empty");
            }
            if (stock == null || stock < 0) {
                throw new IllegalArgumentException("Stock must be a non-negative number");
            }
        }
    }

    record UpdateProductCommand(String productId, String name) {
        public UpdateProductCommand {
            if (productId == null || productId.trim().isEmpty()) {
                throw new IllegalArgumentException("Product ID cannot be null or empty");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Product name cannot be null or empty");
            }
        }
    }

    record UpdateStockCommand(String productId, Integer stock) {
        public UpdateStockCommand {
            if (productId == null || productId.trim().isEmpty()) {
                throw new IllegalArgumentException("Product ID cannot be null or empty");
            }
            if (stock == null || stock < 0) {
                throw new IllegalArgumentException("Stock must be a non-negative number");
            }
        }
    }

    record RemoveProductCommand(String productId) {
        public RemoveProductCommand {
            if (productId == null || productId.trim().isEmpty()) {
                throw new IllegalArgumentException("Product ID cannot be null or empty");
            }
        }
    }
}