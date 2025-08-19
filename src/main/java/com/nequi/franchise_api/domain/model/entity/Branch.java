package com.nequi.franchise_api.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nequi.franchise_api.domain.model.valueobject.BranchId;
import com.nequi.franchise_api.domain.model.valueobject.FranchiseId;
import com.nequi.franchise_api.domain.model.valueobject.Name;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@AllArgsConstructor()
public class Branch {

    private final BranchId id;
    private final FranchiseId franchiseId;
    private Name name;
    @JsonIgnore
    private final LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;

    public static Branch create(FranchiseId franchiseId, Name name) {
        LocalDateTime now = LocalDateTime.now();
        return new Branch(
                null,
                franchiseId,
                name,
                now,
                now
        );
    }

    public static Branch restore(BranchId id, FranchiseId franchiseId, Name name,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Branch(id, franchiseId, name, createdAt, updatedAt);
    }

    public void updateName(Name newName) {
        this.name = newName;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return Objects.equals(id, branch.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}