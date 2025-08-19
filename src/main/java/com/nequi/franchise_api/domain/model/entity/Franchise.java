package com.nequi.franchise_api.domain.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Franchise {

    private final FranchiseId id;
    private Name name;
    @JsonIgnore
    private final LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;

    public static Franchise create(Name name) {
        LocalDateTime now = LocalDateTime.now();
        return new Franchise(
                null,
                name,
                now,
                now
        );
    }

    public static Franchise restore(FranchiseId id, Name name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new Franchise(id, name, createdAt, updatedAt);
    }

    public void updateName(Name newName) {
        this.name = newName;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Franchise franchise = (Franchise) o;
        return Objects.equals(id, franchise.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}