package com.lendingcalculator.lending_calculator.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RowRegister {

    //lending
    private LocalDate dateRegister;
    private BigDecimal lendingValue;
    private BigDecimal outstandingBalance;

    //installment
    private String consolidatedInstallment;
    private BigDecimal totalInstallment;

    //main
    private BigDecimal amortization;
    private BigDecimal outstanding;

    //fees
    private BigDecimal provision;
    private BigDecimal acumulated;
    private BigDecimal paid;

    public LocalDate getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(LocalDate dateRegister) {
        this.dateRegister = dateRegister;
    }

    public BigDecimal getLendingValue() {
        return lendingValue;
    }

    public void setLendingValue(BigDecimal lendingValue) {
        this.lendingValue = lendingValue;
    }

    public BigDecimal getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public String getConsolidatedInstallment() {
        return consolidatedInstallment;
    }

    public void setConsolidatedInstallment(String consolidatedInstallment) {
        this.consolidatedInstallment = consolidatedInstallment;
    }

    public BigDecimal getTotalInstallment() {
        return totalInstallment;
    }

    public void setTotalInstallment(BigDecimal totalInstallment) {
        this.totalInstallment = totalInstallment;
    }

    public BigDecimal getAmortization() {
        return amortization;
    }

    public void setAmortization(BigDecimal amortization) {
        this.amortization = amortization;
    }

    public BigDecimal getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(BigDecimal outstanding) {
        this.outstanding = outstanding;
    }

    public BigDecimal getProvision() {
        return provision;
    }

    public void setProvision(BigDecimal provision) {
        this.provision = provision;
    }

    public BigDecimal getAcumulated() {
        return acumulated;
    }

    public void setAcumulated(BigDecimal acumulated) {
        this.acumulated = acumulated;
    }

    public BigDecimal getPaid() {
        return paid;
    }

    public void setPaid(BigDecimal paid) {
        this.paid = paid;
    }
}
