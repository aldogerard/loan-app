package com.enigmacamp.loanapp.entity;

import com.enigmacamp.loanapp.base.BaseEntity;
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
@Table(name = PathDB.LOAN_DOCUMENT)
public class LoanDocument extends BaseEntity {
    @Column(name = "content_type")
    private String contentType;

    @Column
    private String name;

    @Column
    private String path;

    @Column
    private Long size;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
