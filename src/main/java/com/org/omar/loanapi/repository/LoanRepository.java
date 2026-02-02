package com.org.omar.loanapi.repository;

import com.org.omar.loanapi.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
        Page<Loan> findByStatus(String status, Pageable pageable);
}
