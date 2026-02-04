package com.org.omar.loanapi.repository;

import com.org.omar.loanapi.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT SUM(p.amountPaid) FROM Payment p WHERE p.loanId = :loanId")
    Double getTotalPaidByLoanId(@Param("loanId") Long loanId);
}
