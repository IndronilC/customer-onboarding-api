package com.kanini.corebanking.custonboard.customeronboarding.common.errormsg;

public enum ErrorMessages {

    ERROR_PLEASE_PROVIDE_CUSTOMER_ONBOARDING_INFO("Please provide Customer " +
            "Onboarding Information, " +
            "this cannot be null");

    private String errorValue;

    ErrorMessages(String errorValue) {
        this.errorValue = errorValue;
    }
}
