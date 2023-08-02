package com.kanini.corebanking.custonboard.customeronboarding.services.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@Data
@ToString
@EqualsAndHashCode(callSuper = false)
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CustomerOnboardingBusinessException extends RuntimeException {

    public CustomerOnboardingBusinessException(String message){
        super(message);
    }

    public CustomerOnboardingBusinessException(String message, Throwable cause){
        super(message, cause);
    }

}
