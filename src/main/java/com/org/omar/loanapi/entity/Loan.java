package com.org.omar.loanapi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_seq_gen")
    @SequenceGenerator(
            name = "loan_seq_gen",
            sequenceName = "loan_sequence",
            allocationSize = 1
    )

    private Long id;
    private String customerName;
    private Double principalAmount;
    private Double interestRate;
    private Integer tenureMonths;
    private Double totalExpectedAmount;
    private Double emiAmount;
    private String status; // ACTIVE, CLOSED, DEFAULTED
    @CreatedDate
    @Column(updatable = false, nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime createdDate = LocalDateTime.now();

    public void calculateLoanTerms() {
        // total interest = principal * (rate/100)
        this.totalExpectedAmount = principalAmount + (principalAmount * (interestRate / 100));
        // EMI = (principal + interest) / tenure
        this.emiAmount = this.totalExpectedAmount / this.tenureMonths;
    }
}
