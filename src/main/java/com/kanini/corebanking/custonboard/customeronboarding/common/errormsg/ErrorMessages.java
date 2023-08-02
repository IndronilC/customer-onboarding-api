package com.kanini.corebanking.custonboard.customeronboarding.common.errormsg;

public enum ErrorMessages {

    ERROR_PLEASE_PROVIDE_CUSTOMER_ONBOARDING_INFO("Please provide Customer " +
            "Onboarding Information, " +
            "this cannot be null"),
    ERROR_PLEASE_PROVIDE_ALL_REQUISITE_CUSTOMER_ONBOARDING_INFO("Please provide All needed " +
       "Customer Data For Onboarding " +
            "and Registration");

    private String errorValue;

    ErrorMessages(String errorValue) {
        this.errorValue = errorValue;
    }

    public String getErrorValue() {
        return this.errorValue;
    }
}
