package com.org.omar.loanapi.service;

import com.org.omar.loanapi.dto.LoanSummaryDTO;
import com.org.omar.loanapi.entity.Loan;
import com.org.omar.loanapi.repository.LoanRepository;
import com.org.omar.loanapi.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepo;

    @Autowired
    private PaymentRepository paymentRepo;

    public Loan createLoan(Loan loan) {
        loan.calculateLoanTerms(); //auto calculation EMI
        loan.setStatus("ACTIVE");
        return loanRepo.save(loan);
    }

    public LoanSummaryDTO getSummary(Long id) {
        Loan loan = loanRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id:" + id));

        Double totalPaid = paymentRepo.getTotalPaidByLoanId(id);
        if (totalPaid == null) totalPaid = 0.0;

        double remainingBalance = loan.getTotalExpectedAmount() - totalPaid;

        return new LoanSummaryDTO(
                totalPaid, remainingBalance, loan
        );
    }
}