package com.enigmacamp.loanapp.repository;

import com.enigmacamp.loanapp.entity.LoanTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanTransactionRepository extends JpaRepository<LoanTransaction, String> {
}
