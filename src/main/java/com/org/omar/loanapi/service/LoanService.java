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
    @Autowired private LoanRepository loanRepo;
    @Autowired private PaymentRepository paymentRepo;

    public Loan createLoan(Loan loan) {
        loan.calculateExpected();
        loan.setStatus("ACTIVE");
        return loanRepo.save(loan);
    }

    public Map<String, Object> getSummary(Long id) {
        Loan loan = loanRepo.findById(id).orElseThrow();
        Double totalPaid = paymentRepo.getTotalPaidByLoanId(id);
        if (totalPaid == null) totalPaid = 0.0;

        Map<String, Object> summary = new HashMap<>();
      //  summary.add("loanDetails", loan);
        summary.put("totalPaid", totalPaid);
        summary.put("remainingBalance", loan.getTotalExpectedAmount() - totalPaid);
        return summary;
    }
}