package com.org.omar.loanapi.controller;

import com.org.omar.loanapi.dto.LoanSummaryDTO;
import com.org.omar.loanapi.entity.Loan;
import com.org.omar.loanapi.repository.LoanRepository;
import com.org.omar.loanapi.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
@CrossOrigin(origins = "*") // Allows React to connect
public class LoanController {
    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanRepository loanRepo;

    @PostMapping
    public Loan create(@RequestBody Loan loan) {
        return loanService.createLoan(loan);
    }

    //getList
    @GetMapping
    public Page<Loan> list(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(required = false) String status) {
        if (status != null) {
            return loanRepo.findByStatus(status, PageRequest.of(page, 10));
        }
        return loanRepo.findAll(PageRequest.of(page, 10));
    }

    //getByLoanId
    @GetMapping("/{id}/summary")
    public LoanSummaryDTO getSummary(@PathVariable Long id) {
        return loanService.getSummary(id);
    }

    //updateById
    @PutMapping("/{id}")
    public Loan updateLoan(@PathVariable Long id, @RequestBody Loan loanDetails) {
        Loan loan = loanRepo.findById(id).orElseThrow();
        loan.setCustomerName(loanDetails.getCustomerName());

        // Recalculate if financial terms changed
        loan.calculateLoanTerms();
        loan.setInterestRate(loanDetails.getInterestRate());
        loan.setTenureMonths(loanDetails.getTenureMonths());
        return loanRepo.save(loan);
    }
}