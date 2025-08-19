package com.nequi.franchise_api.infrastructure.adapter.in.web;

import com.nequi.franchise_api.application.service.BranchApplicationService;
import com.nequi.franchise_api.domain.port.in.command.BranchCommandUseCase;
import com.nequi.franchise_api.domain.port.in.query.BranchQueryUseCase;
import com.nequi.franchise_api.infrastructure.dto.request.CreateBranchRequest;
import com.nequi.franchise_api.infrastructure.dto.request.UpdateBranchRequest;
import com.nequi.franchise_api.infrastructure.dto.response.ApiResponse;
import com.nequi.franchise_api.infrastructure.dto.response.BranchResponse;
import com.nequi.franchise_api.infrastructure.mapper.BranchMapper;
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
@RequestMapping("/branches")
@RequiredArgsConstructor
@Tag(name = "Branches", description = "Branch management operations")
public class BranchController {

    private final BranchApplicationService branchApplicationService;
    private final BranchMapper branchMapper;

    @GetMapping("/{branchId}")
    @Operation(summary = "Get branch by ID", description = "Retrieves branch information by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Branch found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Branch not found")
    })
    public Mono<ApiResponse<BranchResponse>> getBranch(
            @Parameter(description = "Branch ID", required = true)
            @ValidUuid @PathVariable String branchId) {

        log.info("Getting branch: {}", branchId);

        return branchApplicationService
                .getBranch(new BranchQueryUseCase.GetBranchQuery(branchId))
                .map(branchMapper::toResponse)
                .map(ApiResponse::success)
                .doOnSuccess(result -> log.info("Branch found: {}", result.data().id()));
    }

    @GetMapping("/{branchId}/with-products")
    @Operation(summary = "Get branch with products by ID", description = "Retrieves branch information includes products by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Branch found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Branch not found")
    })
    public Mono<ApiResponse<BranchResponse>> getBranchWithProducts(
            @Parameter(description = "Branch ID", required = true)
            @ValidUuid @PathVariable String branchId) {

        log.info("Getting branch with products: {}", branchId);

        return branchApplicationService
                .getBranch(new BranchQueryUseCase.GetBranchQuery(branchId))
                .map(branchMapper::toResponse)
                .map(ApiResponse::success)
                .doOnSuccess(result -> log.info("Branch found: {}", result.data().id()));
    }

    @GetMapping("/franchises/{franchiseId}")
    @Operation(summary = "Get branches by franchise", description = "Retrieves all branches for a specific franchise")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Branches retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Franchise not found")
    })
    public Mono<ApiResponse<List<BranchResponse>>> getBranchesByFranchise(
            @Parameter(description = "Franchise ID", required = true)
            @ValidUuid @PathVariable String franchiseId) {

        log.info("Getting branches for franchise: {}", franchiseId);

        return branchApplicationService
                .getBranchesByFranchise(new BranchQueryUseCase.GetBranchesByFranchiseQuery(franchiseId))
                .map(branchMapper::toResponse)
                .collectList()
                .map(branches -> {
                    if (branches.isEmpty()) {
                        return ApiResponse.<BranchResponse>emptyList("No branches found for this franchise");
                    } else {
                        return ApiResponse.<BranchResponse>successList("Branches retrieved successfully", branches);
                    }
                })
                .doOnSuccess(result -> log.info("Retrieved {} branches for franchise: {}", result.count(), franchiseId));
    }

    @PostMapping("/{franchiseId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new branch", description = "Creates a new branch for a specific franchise")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Branch created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Franchise not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Branch with same name already exists in this franchise")
    })
    public Mono<ApiResponse<BranchResponse>> createBranch(
            @Parameter(description = "Franchise ID", required = true)
            @ValidUuid @PathVariable String franchiseId,
            @Valid @RequestBody CreateBranchRequest request) {

        log.info("Creating branch with name: {} for franchise: {}", request.name(), franchiseId);

        return branchApplicationService
                .addBranch(new BranchCommandUseCase.AddBranchCommand(franchiseId, request.name()))
                .map(branchMapper::toResponse)
                .map(response -> ApiResponse.success("Branch created successfully", response))
                .doOnSuccess(result -> log.info("Branch created: {} for franchise: {}", result.data().id(), franchiseId));
    }

    @PutMapping("/{branchId}")
    @Operation(summary = "Update branch", description = "Updates branch information")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Branch updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Branch not found")
    })
    public Mono<ApiResponse<BranchResponse>> updateBranch(
            @Parameter(description = "Branch ID", required = true)
            @ValidUuid @PathVariable String branchId,
            @Valid @RequestBody UpdateBranchRequest request) {

        log.info("Updating branch: {} with name: {}", branchId, request.name());

        return branchApplicationService
                .updateBranch(new BranchCommandUseCase.UpdateBranchCommand(branchId, request.name()))
                .map(branchMapper::toResponse)
                .map(response -> ApiResponse.success("Branch updated successfully", response))
                .doOnSuccess(result -> log.info("Branch updated: {}", result.data().id()));
    }


}