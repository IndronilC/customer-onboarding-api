package com.kanini.corebanking.custonboard.customeronboarding.services.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class CustomerOnboardingBusinessException extends RuntimeException {

    public CustomerOnboardingBusinessException(String message){
        super(message);
    }

    public CustomerOnboardingBusinessException(String message, Throwable cause){
        super(message, cause);
    }

}
