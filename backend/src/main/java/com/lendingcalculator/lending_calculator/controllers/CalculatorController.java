package com.lendingcalculator.lending_calculator.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lendingcalculator.lending_calculator.ErrorResponse;
import com.lendingcalculator.lending_calculator.models.CalculationEntryData;
import com.lendingcalculator.lending_calculator.models.RowRegister;
import com.lendingcalculator.lending_calculator.repository.RegistersRepository;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private RegistersRepository registersRepository = new RegistersRepository();
    
    @PostMapping("calculate")
    public ResponseEntity<?> calculate(@RequestBody @Valid CalculationEntryData entryData) {
        
        registersRepository.clearRegisters();

        LocalDate initialDate = entryData.getLocalDateInitialDate();
        LocalDate finalDate = entryData.getLocalDateFinalDate();
        LocalDate firstPay = entryData.getLocalDateFirstPay();
        BigDecimal rawLendingValue = entryData.getRawLendingValue();
        Long installmentNumber = calculateInstallments(firstPay, finalDate); 
        Double dayBase = 360.0; //Fixed value

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

        Integer roundingScale = 4;
        RoundingMode roundingMode = RoundingMode.HALF_DOWN;

        RowRegister firstRegister = new RowRegister();
        firstRegister.setDateRegister(initialDate);
        firstRegister.setLendingValue(rawLendingValue);
        firstRegister.setConsolidatedInstallment("");
        firstRegister.setAmortization(BigDecimal.ZERO);
        firstRegister.setOutstanding(rawLendingValue);
        firstRegister.setProvision(BigDecimal.ZERO);
        firstRegister.setAcumulated(BigDecimal.ZERO);
        firstRegister.setPaid(BigDecimal.ZERO);
        firstRegister.setOutstandingBalance(firstRegister.getOutstanding().add(firstRegister.getAcumulated()).setScale(roundingScale, roundingMode));
        firstRegister.setTotalInstallment(firstRegister.getAmortization().add(firstRegister.getPaid()).setScale(roundingScale, roundingMode));

        registersRepository.addRegister(firstRegister);

        List<LocalDate> lastDays = getLastDaysOfMonths(initialDate, finalDate);

        Integer installmentCounter = 0;

        for(int y = initialDate.getYear(); y <= finalDate.getYear(); y++){
            
            final int year = y;
            int endMonth = (year == finalDate.getYear()) ? finalDate.getMonthValue() : 12;
            int startMonth = (year == initialDate.getYear()) ? initialDate.getMonthValue() : 1;

            for(int m = startMonth; m <= endMonth; m++){
                final int month = m;
                
                LocalDate currentYearMonth = LocalDate.of(year, month, 1);
                Boolean hasPayment = 
                (
                    (currentYearMonth.getYear() == firstPay.getYear()) && 
                    (currentYearMonth.getMonthValue() == firstPay.getMonthValue()) ||
                    currentYearMonth.isAfter(firstPay)
                )
                ;

                if(installmentCounter <= installmentNumber){
                    
                    if(!hasPayment){
    
                        Optional<LocalDate> foundLastDay = lastDays.stream()
                            .filter(date -> date.getYear() == year && date.getMonthValue() == month)
                        .findFirst();
                        RowRegister lastRegister = registersRepository.getLastRegister();
                        Long daysDifferenceLastRegister = ChronoUnit.DAYS.between(lastRegister.getDateRegister(), foundLastDay.get());
                        RowRegister register = new RowRegister();
                        register.setDateRegister(foundLastDay.get());
                        register.setLendingValue(new BigDecimal("0.00"));
                        register.setConsolidatedInstallment("");
                        register.setAmortization(BigDecimal.ZERO);
                        register.setPaid(BigDecimal.ZERO);
                        register.setTotalInstallment(BigDecimal.ZERO);
                        register.setOutstanding(lastRegister.getOutstanding().subtract(register.getAmortization()).setScale(roundingScale, roundingMode));
    
                        //=((($E$2+1)^((A7-A6)/$F$2))-1)*(G6+I6)
                        Double base = (entryData.getRawTax()/100.0)+1;
                        Double exponent = (double) daysDifferenceLastRegister / dayBase;
                        BigDecimal factor = BigDecimal.valueOf(Math.pow(base, exponent)).subtract(BigDecimal.ONE);
                        BigDecimal result = factor.multiply(
                            lastRegister.getOutstanding().add(lastRegister.getAcumulated()).setScale(roundingScale, roundingMode)
                        );
                        register.setProvision(result.setScale(roundingScale, roundingMode));
    
                        register.setAcumulated((lastRegister.getAcumulated().add(register.getProvision())).subtract(register.getPaid()).setScale(roundingScale, roundingMode));
                        register.setOutstandingBalance(register.getOutstanding().add(register.getAcumulated()).setScale(roundingScale, roundingMode));
    
                        registersRepository.addRegister(register);
    
                    }else{
    
                        Optional<LocalDate> foundLastDay = lastDays.stream()
                            .filter(date -> date.getYear() == year && date.getMonthValue() == month)
                        .findFirst();

                        RowRegister lastRegister = registersRepository.getLastRegister();
                        LocalDate nextInstallment = lastRegister.getDateRegister().plusMonths(1);
    
                        //Payment day
                        installmentCounter++;

                        Boolean isLastInstallment = (installmentCounter == (long) installmentNumber);
                        Integer paymentDay = firstPay.getDayOfMonth();
                        boolean firstPayAtLeastDay = firstPay.getDayOfMonth() == firstPay.lengthOfMonth();
                        if(firstPayAtLeastDay){
                            paymentDay = isLastInstallment ? finalDate.getDayOfMonth() : foundLastDay.get().getDayOfMonth();
                        }else{
                            paymentDay = isLastInstallment ? finalDate.getDayOfMonth() : firstPay.getDayOfMonth();
                            
                            if(LocalDate.of(year, month, 1).lengthOfMonth() < firstPay.getDayOfMonth()){
                                paymentDay = LocalDate.of(year, month, 1).lengthOfMonth();
                            }
                        }

                        LocalDate dateRegister = LocalDate.of(year, month, paymentDay);
                        dateRegister = (isLastInstallment) ? dateRegister : adjustToNextUtilDayIfNeeded(dateRegister);

                        if (nextInstallment.isAfter(finalDate) || nextInstallment.equals(finalDate)) {
                            dateRegister = finalDate;
                        }


                        Long daysDifferenceLastRegister = ChronoUnit.DAYS.between(lastRegister.getDateRegister(), dateRegister);
                        RowRegister register = new RowRegister();
    
                        register.setDateRegister(dateRegister);
                        register.setLendingValue(new BigDecimal("0.00"));
                        register.setConsolidatedInstallment(Integer.toString(installmentCounter).concat("/".concat(installmentNumber.toString())));
                        register.setAmortization(firstRegister.getOutstanding().divide(BigDecimal.valueOf(installmentNumber), roundingScale, roundingMode));
    
                        //=((($E$2+1)^((A7-A6)/$F$2))-1)*(G6+I6)
                        Double base = (entryData.getRawTax()/100.0)+1;
                        Double exponent = (double) daysDifferenceLastRegister / dayBase;
                        BigDecimal factor = BigDecimal.valueOf(Math.pow(base, exponent)).subtract(BigDecimal.ONE);
                        BigDecimal result = factor.multiply(
                            lastRegister.getOutstanding().add(lastRegister.getAcumulated()).setScale(roundingScale, roundingMode)
                        );
                        register.setProvision(result.setScale(roundingScale, roundingMode));
    
                        register.setPaid(lastRegister.getAcumulated().add(register.getProvision()).setScale(roundingScale, roundingMode));
                        register.setTotalInstallment(register.getAmortization().add(register.getPaid()).setScale(roundingScale, roundingMode));
                        register.setOutstanding(lastRegister.getOutstanding().subtract(register.getAmortization()).setScale(roundingScale, roundingMode));
                        register.setAcumulated((lastRegister.getAcumulated().add(register.getProvision())).subtract(register.getPaid()).setScale(roundingScale, roundingMode));
                        register.setOutstandingBalance(register.getOutstanding().add(register.getAcumulated()).setScale(roundingScale, roundingMode));
    
                        registersRepository.addRegister(register);
    
                        if(isLastInstallment || dateRegister.equals(finalDate)) break;
    
                        //Last day of month after payment
    
                        if(firstPayAtLeastDay && installmentCounter == 1){
                            continue;
                        }
    
                        lastRegister = registersRepository.getLastRegister();
                        daysDifferenceLastRegister = ChronoUnit.DAYS.between(lastRegister.getDateRegister(), foundLastDay.get());
                        register = new RowRegister();
                        register.setDateRegister(foundLastDay.get());
                        register.setLendingValue(new BigDecimal("0.00"));
                        register.setConsolidatedInstallment("");
                        register.setAmortization(BigDecimal.ZERO);
    
                        //=((($E$2+1)^((A7-A6)/$F$2))-1)*(G6+I6)
                        base = (entryData.getRawTax()/100.0)+1;
                        exponent = (double) daysDifferenceLastRegister / dayBase;
                        factor = BigDecimal.valueOf(Math.pow(base, exponent)).subtract(BigDecimal.ONE);
                        result = factor.multiply(
                            lastRegister.getOutstanding().add(lastRegister.getAcumulated()).setScale(roundingScale, roundingMode)
                        );
                        register.setProvision(result.setScale(roundingScale, roundingMode));
    
                        register.setPaid(BigDecimal.ZERO);
                        register.setTotalInstallment(BigDecimal.ZERO);
                        register.setOutstanding(lastRegister.getOutstanding().subtract(register.getAmortization()).setScale(roundingScale, roundingMode));
                        register.setAcumulated((lastRegister.getAcumulated().add(register.getProvision())).subtract(register.getPaid()).setScale(roundingScale, roundingMode));
                        register.setOutstandingBalance(register.getOutstanding().add(register.getAcumulated()).setScale(roundingScale, roundingMode));
    
                        registersRepository.addRegister(register);
                    }
                }
            }
        }

        return ResponseEntity.ok(registersRepository.getRegisters());
    }

    public static List<LocalDate> getLastDaysOfMonths(LocalDate initialDate, LocalDate finalDate) {
        List<LocalDate> lastDays= new ArrayList<>();

        YearMonth startMonth = YearMonth.from(initialDate);
        YearMonth finalMonth = YearMonth.from(finalDate);

        while (!startMonth.isAfter(finalMonth)) {
            LocalDate lastDay = startMonth.atEndOfMonth();
            
            while (lastDay.getDayOfWeek() == DayOfWeek.SATURDAY || lastDay.getDayOfWeek() == DayOfWeek.SUNDAY) {
                lastDay = lastDay.minusDays(1);
            }

            if (!lastDay.isBefore(initialDate) && !lastDay.isAfter(finalDate)) {
                lastDays.add(lastDay);
            }
            startMonth = startMonth.plusMonths(1);
        }

        return lastDays;
    }

    private static long calculateInstallments(LocalDate firstPay, LocalDate finalDate) {
        long monthsBetween = ChronoUnit.MONTHS.between(
            firstPay.withDayOfMonth(1), 
            finalDate.withDayOfMonth(1)
        );
        return monthsBetween + 1;
    }

    private static Set<MonthDay> getFixedHolidays() {
        Set<MonthDay> holiday = new HashSet<>();
        holiday.add(MonthDay.of(1, 1));   // Confraternização Universal
        holiday.add(MonthDay.of(4, 21));  // Tiradentes
        holiday.add(MonthDay.of(5, 1));   // Dia do Trabalho
        holiday.add(MonthDay.of(9, 7));   // Independência
        holiday.add(MonthDay.of(10, 12)); // Nossa Senhora Aparecida
        holiday.add(MonthDay.of(11, 2));  // Finados
        holiday.add(MonthDay.of(11, 15)); // Proclamação da República
        holiday.add(MonthDay.of(12, 25)); // Natal
        return holiday;
    }

    private static boolean isWeekend(LocalDate date) { //I'm blinding in the lights
        DayOfWeek weekDay = date.getDayOfWeek();
        return weekDay == DayOfWeek.SATURDAY || weekDay == DayOfWeek.SUNDAY;
    }

    private static LocalDate adjustToNextUtilDayIfNeeded(LocalDate data) {
        Set<MonthDay> fixedHolidays = getFixedHolidays();

        LocalDate adjusted = data;

        if (!isWeekend(adjusted) && !fixedHolidays.contains(MonthDay.from(adjusted))) {
            return adjusted;
        }

        LocalDate nextDay = adjusted.plusDays(1);
        if (nextDay.getMonth() == adjusted.getMonth()) {
            while ((isWeekend(nextDay) || fixedHolidays.contains(MonthDay.from(nextDay))) && 
                nextDay.getMonth() == adjusted.getMonth()) {
                nextDay = nextDay.plusDays(1);
            }
            if (nextDay.getMonth() == adjusted.getMonth() && 
                !isWeekend(nextDay) && !fixedHolidays.contains(MonthDay.from(nextDay))) {
                return nextDay;
            }
        }

        LocalDate previousDay = adjusted.minusDays(1);
        while (isWeekend(previousDay) || fixedHolidays.contains(MonthDay.from(previousDay))) {
            previousDay = previousDay.minusDays(1);
        }
        
        return previousDay;
    }
    
}
