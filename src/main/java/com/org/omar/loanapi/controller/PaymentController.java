package com.org.omar.loanapi.controller;


import com.org.omar.loanapi.entity.Payment;
import com.org.omar.loanapi.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "*") //  React frontend to call this API
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    /**
     * POST /payments -> Add a payment for a loan
     * This will trigger the business logic to update loan balance and status.
     */
    @PostMapping
    public ResponseEntity<Payment> addPayment(@RequestBody Payment payment) {
        try {
            Payment savedPayment = paymentService.addPayment(payment);
            return ResponseEntity.ok(savedPayment);
        } catch (RuntimeException e) {
            // Returns a 404 or 400 if the loan ID doesn't exist
            return ResponseEntity.badRequest().build();
        }
    }
}
