package com.nequi.franchise_api.infrastructure.adapter.out.persistence.entity;

import com.nequi.franchise_api.shared.audit.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table("branches")
public class BranchEntity extends AuditableEntity {

    @Column("franchise_id")
    private UUID franchiseId;

    @Column("name")
    private String name;
}
