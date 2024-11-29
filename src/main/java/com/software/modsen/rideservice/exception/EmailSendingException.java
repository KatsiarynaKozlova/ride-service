package com.software.modsen.rideservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
public class EmailSendingException extends RuntimeException{
    public EmailSendingException(String s) {
        super(s);
    }
}
