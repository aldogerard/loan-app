package com.enigmacamp.loanapp.entity;

import com.enigmacamp.loanapp.base.BaseEntity;
import com.enigmacamp.loanapp.constant.enums.EInstalmentType;
import com.enigmacamp.loanapp.constant.strings.PathDB;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = PathDB.INSTALMENT_TYPE)
public class InstalmentType extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private EInstalmentType name;
}
