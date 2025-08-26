package com.lendingcalculator.lending_calculator.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lendingcalculator.lending_calculator.ErrorResponse;
import com.lendingcalculator.lending_calculator.models.CalculationEntryData;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {
    
    @PostMapping("calculate")
    public ResponseEntity<?> calculate(@RequestBody @Valid CalculationEntryData entryData) {
        
        LocalDate initialDate = entryData.getLocalDateInitialDate();
        LocalDate finalDate = entryData.getLocalDateFinalDate();
        LocalDate firstPay = entryData.getLocalDateFirstPay();
        BigDecimal rawLendingValue = entryData.getRawLendingValue();

        if(finalDate.isBefore(initialDate) || finalDate.isEqual(initialDate)) {
            ErrorResponse error = new ErrorResponse("Incorrect Parameter", "Final date must be after initial date.",400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }else if(initialDate.isAfter(finalDate) || initialDate.isEqual(finalDate)) {
            ErrorResponse error = new ErrorResponse("Incorrect Parameter", "Initial date must be before final date.",400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }else if(firstPay.isBefore(initialDate) || firstPay.isAfter(finalDate) || firstPay.isEqual(initialDate) || firstPay.isEqual(finalDate)) {
            ErrorResponse error = new ErrorResponse("Incorrect Parameter", "First pay date must be major than initial and minor than final date.",400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }else if(rawLendingValue.compareTo(new BigDecimal("0.5")) < 0) {
            ErrorResponse error = new ErrorResponse("Incorrect Parameter", "Lending value must be at least 0.5.",400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        
        
        return ResponseEntity.ok(entryData.toString());
    }
    
}
