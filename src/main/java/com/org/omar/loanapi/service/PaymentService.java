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
    @Autowired
    private LoanRepository loanRepo;

    @Transactional
    public Payment addPayment(Payment payment) {
        // 1. Fetch the associated loan from the database
        Loan loan = loanRepo.findById(payment.getLoanId())
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        // 2. Retrieve the total amount already paid for this loan
        Double totalPaidBefore = paymentRepo.getTotalPaidByLoanId(loan.getId());
        if (totalPaidBefore == null) totalPaidBefore = 0.0;

        // 3. Get the amount the user is currently trying to pay
        double currentPayment = payment.getAmountPaid();

        // 4. Calculate what the new total paid amount will be after this transaction
        double newTotalPaid = totalPaidBefore + currentPayment;

        // 5. Calculate the remaining balance before this payment is processed
        double remainingBalance = loan.getTotalExpectedAmount() - totalPaidBefore;

        // 6. Validation: Prevent overpayment.
        // A small buffer (0.01) is added to handle floating-point precision issues.
        if (currentPayment > (remainingBalance + 0.01)) {
            throw new RuntimeException("Loan amount exceeds payment amount. Remaining Balance: " + remainingBalance);
        }

        // 7. Save the current payment record to the database
        Payment savedPayment = paymentRepo.save(payment);

        // 8. Closure Logic: Calculate the difference between Expected Total and New Total Paid
        double difference = loan.getTotalExpectedAmount() - newTotalPaid;
        // If the difference is zero or negligible (<= 0.01), mark the loan as CLOSED
        if (difference <= 0.01) {
            loan.setStatus("CLOSED");
            loanRepo.save(loan);
        }

        return savedPayment;
    }
}
