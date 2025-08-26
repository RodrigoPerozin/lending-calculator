package com.lendingcalculator.lending_calculator.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;

@Data
public class CalculationEntryData {

    @NotNull
    @NotBlank
    @NotEmpty
    private String initialDate;    
    
    @NotNull
    @NotBlank
    @NotEmpty
    private String finalDate;

    @NotNull
    @NotBlank
    @NotEmpty
    private String firstPay;

    @NotNull
    @NotBlank
    @NotEmpty
    private String lendingValue;

    @Digits(integer = 15, fraction = 2)
    @DecimalMin("0.5")
    @DecimalMax("1000000000000.00") 
    private BigDecimal rawLendingValue;
    
    @NotNull
    @NotBlank
    @NotEmpty
    private String tax;

    @DecimalMin("0.00")
    @DecimalMax("100.00") 
    private Double rawTax;

    public CalculationEntryData(String initialDate, String finalDate, String firstPay, String lendingValue,
        BigDecimal rawLendingValue, String tax, Double rawTax) {
        this.initialDate = initialDate;
        this.finalDate = finalDate;
        this.firstPay = firstPay;
        this.lendingValue = lendingValue;
        this.rawLendingValue = rawLendingValue;
        this.tax = tax;
        this.rawTax = rawTax;
    }

    public String getInitialDate() {
        return initialDate;
    }

    public LocalDate getLocalDateInitialDate() {
        return LocalDate.parse(this.initialDate);
    }

    public void setInitialDate(String initialDate) {
        this.initialDate = initialDate;
    }

    public String getFinalDate() {
        return finalDate;
    }

    public LocalDate getLocalDateFinalDate() {
        return LocalDate.parse(this.finalDate);
    }

    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }

    public String getFirstPay() {
        return firstPay;
    }

    public LocalDate getLocalDateFirstPay() {
        return LocalDate.parse(this.firstPay);
    }

    public void setFirstPay(String firstPay) {
        this.firstPay = firstPay;
    }

    public String getLendingValue() {
        return lendingValue;
    }

    public void setLendingValue(String lendingValue) {
        this.lendingValue = lendingValue;
    }

    public BigDecimal getRawLendingValue() {
        return rawLendingValue;
    }

    public void setRawLendingValue(BigDecimal rawLendingValue) {
        this.rawLendingValue = rawLendingValue;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public Double getRawTax() {
        return rawTax;
    }

    public void setRawTax(Double rawTax) {
        this.rawTax = rawTax;
    }

}
