package com.org.omar.loanapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String customerName;
    private Double principalAmount;
    private Double interestRate;
    private Integer tenureMonths;
    private Double totalExpectedAmount;
    private String status; // ACTIVE, CLOSED, DEFAULTED
    private LocalDateTime createdDate = LocalDateTime.now();

    /*
    // Empty constructor (needed by JPA)
    /*
    public Loan() {
    }

    // Constructor with fields
    public Loan(long id, String customerName, Double principalAmount, Double interestRate, Integer tenureMonths, Double totalExpectedAmount, String status, LocalDateTime createdDate) {
        this.id = id;
        this.customerName = customerName;
        this.principalAmount = principalAmount;
        this.interestRate = interestRate;
        this.tenureMonths = tenureMonths;
        this.totalExpectedAmount = totalExpectedAmount;
        this.status = status;
        this.createdDate = createdDate;
    }
    */

    // Method to calculate expected total
    public void calculateExpected(){
        this.totalExpectedAmount = principalAmount +  (principalAmount * (interestRate / 100));
    }
}
