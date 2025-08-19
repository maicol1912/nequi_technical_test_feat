package com.nequi.franchise_api.infrastructure.adapter.in.web;

import com.nequi.franchise_api.application.service.ProductApplicationService;
import com.nequi.franchise_api.domain.port.in.command.ProductCommandUseCase;
import com.nequi.franchise_api.domain.port.in.query.ProductQueryUseCase;
import com.nequi.franchise_api.infrastructure.dto.request.CreateProductRequest;
import com.nequi.franchise_api.infrastructure.dto.request.UpdateProductRequest;
import com.nequi.franchise_api.infrastructure.dto.request.UpdateStockRequest;
import com.nequi.franchise_api.infrastructure.dto.response.ApiResponse;
import com.nequi.franchise_api.infrastructure.dto.response.ProductResponse;
import com.nequi.franchise_api.infrastructure.mapper.ProductMapper;
import com.nequi.franchise_api.infrastructure.validation.annotation.ValidUuid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management operations")
public class ProductController {

    private final ProductApplicationService productApplicationService;
    private final ProductMapper productMapper;

    @GetMapping("/{productId}")
    @Operation(summary = "Get product by ID", description = "Retrieves product information by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    public Mono<ApiResponse<ProductResponse>> getProduct(
            @Parameter(description = "Product ID", required = true)
            @ValidUuid @PathVariable String productId) {

        log.info("Getting product: {}", productId);

        return productApplicationService
                .getProduct(new ProductQueryUseCase.GetProductQuery(productId))
                .map(productMapper::toResponse)
                .map(ApiResponse::success)
                .doOnSuccess(result -> log.info("Product found: {}", result.data().id()));
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get products by branchId", description = "Retrieves all products for a specific branch")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Branch not found or no products found")
    })
    public Mono<ApiResponse<List<ProductResponse>>> getProductsByBranchId(
            @Parameter(description = "Branch ID", required = true)
            @ValidUuid @PathVariable String branchId) {

        log.info("Getting products by branchId: {}", branchId);

        return productApplicationService
                .getProductsByBranch(new ProductQueryUseCase.GetProductsByBranchQuery(branchId))
                .map(productMapper::toResponse)
                .collectList()
                .map(products -> {
                    if (products.isEmpty()) {
                        return ApiResponse.<ProductResponse>emptyList("No products found for this branch");
                    } else {
                        return ApiResponse.<ProductResponse>successList("Products retrieved successfully", products);
                    }
                })
                .doOnSuccess(result -> log.info("Retrieved {} products for branch: {}", result.count(), branchId));
    }

    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves all products")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No products found")
    })
    public Mono<ApiResponse<List<ProductResponse>>> getAllProducts() {
        log.info("Getting all products");
        return productApplicationService
                .getAllProducts()
                .map(productMapper::toResponse)
                .collectList()
                .map(products -> {
                    if (products.isEmpty()) {
                        return ApiResponse.<ProductResponse>emptyList("No products found");
                    } else {
                        return ApiResponse.<ProductResponse>successList("Products retrieved successfully", products);
                    }
                })
                .doOnSuccess(result -> log.info("Retrieved {} products", result.count()));
    }

    @PostMapping("/branch/{branchId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add product to branch", description = "Adds a new product to an existing branch")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Product added successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Branch not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Product with same name already exists")
    })
    public Mono<ApiResponse<ProductResponse>> addProduct(
            @Parameter(description = "Branch ID", required = true)
            @ValidUuid @PathVariable String branchId,
            @Valid @RequestBody CreateProductRequest request) {

        log.info("Adding product to branch: {} with name: {} and stock: {}", branchId, request.name(), request.stock());

        return productApplicationService
                .addProduct(new ProductCommandUseCase.AddProductCommand(branchId, request.name(), request.stock()))
                .map(productMapper::toResponse)
                .map(response -> ApiResponse.success("Product added successfully", response))
                .doOnSuccess(result -> log.info("Product added: {}", result.data().id()));
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update product", description = "Updates product information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    public Mono<ApiResponse<ProductResponse>> updateProduct(
            @Parameter(description = "Product ID", required = true)
            @ValidUuid @PathVariable String productId,
            @Valid @RequestBody UpdateProductRequest request) {

        log.info("Updating product: {} with name: {}", productId, request.name());

        return productApplicationService
                .updateProduct(new ProductCommandUseCase.UpdateProductCommand(productId, request.name()))
                .map(productMapper::toResponse)
                .map(response -> ApiResponse.success("Product updated successfully", response))
                .doOnSuccess(result -> log.info("Product updated: {}", result.data().id()));
    }

    @PatchMapping("/{productId}/stock")
    @Operation(summary = "Update product stock", description = "Updates product stock quantity")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Stock updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    public Mono<ApiResponse<ProductResponse>> updateStock(
            @Parameter(description = "Product ID", required = true)
            @ValidUuid @PathVariable String productId,
            @Valid @RequestBody UpdateStockRequest request) {

        log.info("Updating stock for product: {} to: {}", productId, request.stock());

        return productApplicationService
                .updateStock(new ProductCommandUseCase.UpdateStockCommand(productId, request.stock()))
                .map(productMapper::toResponse)
                .map(response -> ApiResponse.success("Stock updated successfully", response))
                .doOnSuccess(result -> log.info("Stock updated for product: {}", result.data().id()));
    }
    
    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove product", description = "Removes a product from the system")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Product removed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Product not found")
    })
    public Mono<Void> removeProduct(
            @Parameter(description = "Product ID", required = true)
            @ValidUuid @PathVariable String productId) {

        log.info("Removing product: {}", productId);

        return productApplicationService
                .removeProduct(new ProductCommandUseCase.RemoveProductCommand(productId))
                .doOnSuccess(unused -> log.info("Product removed: {}", productId));
    }
}