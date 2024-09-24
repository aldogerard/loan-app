package com.enigmacamp.loanapp.entity;

import com.enigmacamp.loanapp.base.BaseEntity;
import com.enigmacamp.loanapp.constant.enums.ELoanStatus;
import com.enigmacamp.loanapp.constant.strings.PathDB;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = PathDB.LOAN_TRANSACTION_DETAIL)
public class LoanTransactionDetail extends BaseEntity {
    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Column
    private Double nominal;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "loan_transaction_id")
    private LoanTransaction loanTransaction;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_status")
    private ELoanStatus loanStatus;
}
