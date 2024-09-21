package com.enigmacamp.loanapp.repository;

import com.enigmacamp.loanapp.entity.LoanDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanDocumentRepository extends JpaRepository<LoanDocument, String> {
}
