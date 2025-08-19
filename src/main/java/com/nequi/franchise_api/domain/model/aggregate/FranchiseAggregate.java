package com.nequi.franchise_api.domain.model.aggregate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.nequi.franchise_api.domain.model.entity.Branch;
import com.nequi.franchise_api.domain.model.entity.Franchise;
import com.nequi.franchise_api.domain.model.entity.Product;
import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class FranchiseAggregate {
    private final Franchise franchise;

    private final List<BranchWithProducts> branches;
    @JsonIgnore
    public FranchiseId getId() {
        return franchise.getId();
    }
    @JsonIgnore
    public String getName() {
        return franchise.getName().getValue();
    }


    public int getTotalBranches() {
        return branches.size();
    }

    public boolean hasBranches() {
        return !branches.isEmpty();
    }

}
