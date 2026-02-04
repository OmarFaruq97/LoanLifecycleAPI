package com.org.omar.loanapi.service;

import com.org.omar.loanapi.entity.Loan;
import com.org.omar.loanapi.entity.Payment;
import com.org.omar.loanapi.repository.LoanRepository;
import com.org.omar.loanapi.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepo;
    @Autowired private LoanRepository loanRepo;

    @Transactional
    public Payment addPayment(Payment payment) {
        // 1. Save the payment
        Payment savedPayment = paymentRepo.save(payment);

        // 2. Fetch the associated loan
        Loan loan = loanRepo.findById(payment.getLoanId())
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        // 3. Calculate total paid so far using your JPQL query
        Double totalPaid = paymentRepo.getTotalPaidByLoanId(loan.getId());
        if (totalPaid == null) totalPaid = 0.0;

        // 4. Business Rule: If total paid >= total expected -> status = CLOSED
        if (totalPaid >= loan.getTotalExpectedAmount()) {
            loan.setStatus("CLOSED");
            loanRepo.save(loan);
        }
        return savedPayment;
    }
}
