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
        // 1.
        Loan loan = loanRepo.findById(payment.getLoanId())
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        // 2.
        Double totalPaidBefore = paymentRepo.getTotalPaidByLoanId(loan.getId());
        if (totalPaidBefore == null) totalPaidBefore = 0.0;

        // 3.
        double currentPayment = payment.getAmountPaid();

        // 4.
        double newTotalPaid = totalPaidBefore + currentPayment;

        // 5.
        double remainingBalance = loan.getTotalExpectedAmount() - totalPaidBefore;

        // 6.
        if (currentPayment > (remainingBalance + 0.01)) {
            throw new RuntimeException("Loan amount exceeds payment amount. Remaining Balance: " + remainingBalance);
        }

        // 7.
        Payment savedPayment = paymentRepo.save(payment);

        // 8.
        double difference = loan.getTotalExpectedAmount() - newTotalPaid;

        if (difference <= 0.01) {
            loan.setStatus("CLOSED");
            loanRepo.save(loan);
        }

        return savedPayment;
    }
}
