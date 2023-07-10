package com.kanini.corebanking.custonboard.customeronboarding.controller.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ToString
@EqualsAndHashCode
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomerOnboardingControllerException extends RuntimeException {

    String errorMessage;
    public CustomerOnboardingControllerException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
