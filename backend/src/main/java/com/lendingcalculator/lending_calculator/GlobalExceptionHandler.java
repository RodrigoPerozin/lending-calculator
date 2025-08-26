package com.lendingcalculator.lending_calculator;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.http.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handle404(NoHandlerFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                "Not Found",
                "The requested endpoint does not exist",
                404
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
