package com.org.omar.loanapi.service;

import com.org.omar.loanapi.entity.Loan;
import com.org.omar.loanapi.repository.LoanRepository;
import com.org.omar.loanapi.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Object> getSummary(Long id) {
        Loan loan = loanRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan not found with id:" + id));

        Double totalPaid = paymentRepo.getTotalPaidByLoanId(id);
        if (totalPaid == null) totalPaid = 0.0;

        Double remainingBalance = loan.getTotalExpectedAmount() - totalPaid;

        Map<String, Object> response = new java.util.LinkedHashMap<>();
        response.put("Total Paid: ",totalPaid);
        response.put("Remaining Balance:", remainingBalance > 0 ? remainingBalance : 0.0);

        response.put("loanDetails", loan);

        return response;
    }
}