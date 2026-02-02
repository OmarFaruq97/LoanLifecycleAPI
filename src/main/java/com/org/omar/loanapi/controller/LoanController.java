package com.org.omar.loanapi.controller;

import com.org.omar.loanapi.entity.Loan;
import com.org.omar.loanapi.repository.LoanRepository;
import com.org.omar.loanapi.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/loans")
@CrossOrigin(origins = "*") // Allows React to connect
public class LoanController {
    @Autowired private LoanService loanService;
    @Autowired private LoanRepository loanRepo;

    @PostMapping
    public Loan create(@RequestBody Loan loan) {
        return loanService.createLoan(loan);
    }

    @GetMapping
    public Page<Loan> list(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(required = false) String status) {
        if (status != null) {
            return loanRepo.findByStatus(status, PageRequest.of(page, 10));
        }
        return loanRepo.findAll(PageRequest.of(page, 10));
    }

    @GetMapping("/{id}/summary")
    public Map<String, Object> getSummary(@PathVariable Long id) {
        return loanService.getSummary(id);
    }
}