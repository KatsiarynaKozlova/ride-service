package com.software.modsen.rideservice.exception.handler;

import com.software.modsen.rideservice.exception.EmailSendingException;
import com.software.modsen.rideservice.exception.RideNotFoundException;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionApiHandler {
    @ExceptionHandler(RideNotFoundException.class)
    public ResponseEntity<ErrorMessage> notFoundException(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<ErrorMessage> emailException(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.FAILED_DEPENDENCY)
                .body(new ErrorMessage(exception.getMessage()));
    }
}
