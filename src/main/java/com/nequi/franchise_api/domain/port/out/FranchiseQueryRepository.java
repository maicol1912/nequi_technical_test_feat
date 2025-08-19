package com.nequi.franchise_api.domain.port.out;

import com.nequi.franchise_api.domain.model.aggregate.FranchiseAggregate;
import com.nequi.franchise_api.domain.model.aggregate.ProductMaxStock;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranchiseQueryRepository {

    Mono<FranchiseAggregate> findFranchiseWithBranchesAndProducts(FranchiseId franchiseId);

    Flux<ProductMaxStock> findProductsWithMaxStockByFranchise(FranchiseId franchiseId);
}
