package com.enigmacamp.loanapp.entity;

import com.enigmacamp.loanapp.base.BaseEntity;
import com.enigmacamp.loanapp.constant.enums.EApprovalStatus;
import com.enigmacamp.loanapp.constant.strings.PathDB;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.swing.text.View;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = PathDB.LOAN_TRANSACTION)
public class LoanTransaction extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "loan_type_id")
    private LoanType loanType;

    @ManyToOne
    @JoinColumn(name = "instalment_type_id")
    private InstalmentType instalmentType;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column
    private Double nominal;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "approved_by")
    private String approvedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status")
    private EApprovalStatus approvalStatus;

    @OneToMany(mappedBy = "loanTransaction")
    @JsonView
    @JsonManagedReference
    private List<LoanTransactionDetail> loanTransactionDetails;
}
