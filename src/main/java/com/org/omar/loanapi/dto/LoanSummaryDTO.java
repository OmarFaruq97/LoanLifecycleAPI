package com.org.omar.loanapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.org.omar.loanapi.entity.Loan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanSummaryDTO {

    @JsonProperty("Total Deposit: ")
    private Double totalPaid;

    @JsonProperty("Remaining Balance: ")
    private Double remainingBalance;

    private Loan loanDetails;
}
