package com.nequi.franchise_api.infrastructure.adapter.in.web;

import com.nequi.franchise_api.application.service.FranchiseApplicationService;
import com.nequi.franchise_api.domain.model.aggregate.FranchiseAggregate;
import com.nequi.franchise_api.domain.port.in.command.FranchiseCommandUseCase;
import com.nequi.franchise_api.domain.port.in.query.FranchiseQueryUseCase;
import com.nequi.franchise_api.domain.port.in.query.ProductsWithMaxStockQueryUseCase;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.projection.FranchiseRawData;
import com.nequi.franchise_api.infrastructure.adapter.out.persistence.repository.FranchiseQueryR2dbcRepository;
import com.nequi.franchise_api.infrastructure.dto.request.CreateFranchiseRequest;
import com.nequi.franchise_api.infrastructure.dto.request.UpdateFranchiseRequest;
import com.nequi.franchise_api.infrastructure.dto.response.ApiResponse;
import com.nequi.franchise_api.infrastructure.dto.response.FranchiseDetailResponse;
import com.nequi.franchise_api.infrastructure.dto.response.FranchiseResponse;
import com.nequi.franchise_api.infrastructure.dto.response.ProductMaxStockResponse;
import com.nequi.franchise_api.infrastructure.mapper.FranchiseMapper;
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
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/franchises")
@RequiredArgsConstructor
@Tag(name = "Franchises", description = "Franchise management operations")
public class FranchiseController {

    private final FranchiseApplicationService franchiseApplicationService;
    private final FranchiseMapper franchiseMapper;
    private final ProductMapper productMapper;
    private final FranchiseQueryR2dbcRepository franchiseQueryR2dbcRepository;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new franchise", description = "Creates a new franchise with the provided information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Franchise created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Franchise with same name already exists")
    })
    public Mono<ApiResponse<FranchiseResponse>> createFranchise(
            @Valid @RequestBody CreateFranchiseRequest request) {

        log.info("Creating franchise with name: {}", request.name());

        return franchiseApplicationService
                .createFranchise(new FranchiseCommandUseCase.CreateFranchiseCommand(request.name()))
                .map(franchiseMapper::toResponse)
                .map(response -> ApiResponse.success("Franchise created successfully", response))
                .doOnSuccess(result -> log.info("Franchise created: {}", result.data().id()));
    }

    @PutMapping("/{franchiseId}")
    @Operation(summary = "Update franchise", description = "Updates franchise information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Franchise updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Franchise not found")
    })
    public Mono<ApiResponse<FranchiseResponse>> updateFranchise(
            @Parameter(description = "Franchise ID", required = true)
            @ValidUuid @PathVariable String franchiseId,
            @Valid @RequestBody UpdateFranchiseRequest request) {

        log.info("Updating franchise: {} with name: {}", franchiseId, request.name());

        return franchiseApplicationService
                .updateFranchise(new FranchiseCommandUseCase.UpdateFranchiseCommand(franchiseId, request.name()))
                .map(franchiseMapper::toResponse)
                .map(response -> ApiResponse.success("Franchise updated successfully", response))
                .doOnSuccess(result -> log.info("Franchise updated: {}", result.data().id()));
    }

    @GetMapping("/{franchiseId}")
    @Operation(summary = "Get franchise by ID", description = "Retrieves franchise information by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Franchise found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Franchise not found")
    })
    public Mono<ApiResponse<FranchiseResponse>> getFranchise(
            @Parameter(description = "Franchise ID", required = true)
            @ValidUuid @PathVariable String franchiseId) {

        log.info("Getting franchise: {}", franchiseId);

        return franchiseApplicationService
                .getFranchise(new FranchiseQueryUseCase.GetFranchiseQuery(franchiseId))
                .map(franchiseMapper::toResponse)
                .map(ApiResponse::success)
                .doOnSuccess(result -> log.info("Franchise found: {}", result.data().id()));
    }

    @GetMapping
    @Operation(summary = "Get all franchises", description = "Retrieves all franchises")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Franchises retrieved successfully")
    })
    public Mono<ApiResponse<List<FranchiseResponse>>> getAllFranchises() {
        log.info("Getting all franchises");

        return franchiseApplicationService
                .getAllFranchises()
                .map(franchiseMapper::toResponse)
                .collectList()
                .map(franchises -> {
                    if (franchises.isEmpty()) {
                        return ApiResponse.<FranchiseResponse>emptyList("No franchises found");
                    } else {
                        return ApiResponse.<FranchiseResponse>successList("All franchises retrieved successfully", franchises);
                    }
                })
                .doOnSuccess(result -> log.info("Retrieved {} franchises", result.count()));
    }

    @GetMapping("/{franchiseId}/complete")
    @Operation(summary = "Get complete franchise", description = "Retrieves franchise with all branches and products")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Complete franchise information retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Franchise not found")
    })
    public Mono<FranchiseAggregate> getCompleteFranchise(
            @Parameter(description = "Franchise ID", required = true)
            @ValidUuid @PathVariable String franchiseId) {

        return franchiseApplicationService.getFranchiseComplete(franchiseId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Franchise not found")));
    }


    @GetMapping("/{franchiseId}/products-max-stock")
    @Operation(summary = "Get products with maximum stock",
            description = "Retrieves products with maximum stock per branch for a franchise")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Products with maximum stock retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Franchise not found")
    })
    public Mono<ApiResponse<List<ProductMaxStockResponse>>> getProductsWithMaxStock(
            @Parameter(description = "Franchise ID", required = true)
            @ValidUuid @PathVariable String franchiseId) {

        log.info("Getting products with max stock for franchise: {}", franchiseId);

        return franchiseApplicationService
                .getProductsWithMaxStock(new ProductsWithMaxStockQueryUseCase.GetProductsWithMaxStockQuery(franchiseId))
                .map(productMapper::toMaxStockResponse)
                .collectList()
                .map(products -> {
                    if (products.isEmpty()) {
                        return ApiResponse.<ProductMaxStockResponse>emptyList("No products with max stock found for this franchise");
                    } else {
                        return ApiResponse.<ProductMaxStockResponse>successList("Products with max stock retrieved successfully", products);
                    }
                })
                .doOnSuccess(result -> log.info("Retrieved {} products with max stock for franchise: {}", result.count(), franchiseId));
    }
}
