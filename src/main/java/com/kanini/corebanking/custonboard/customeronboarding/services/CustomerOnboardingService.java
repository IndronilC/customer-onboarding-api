package com.kanini.corebanking.custonboard.customeronboarding.services;

import com.kanini.corebanking.custonboard.customeronboarding.dto.CustomerDTO;


public interface CustomerOnboardingService {
    public CustomerDTO registerCustomer(CustomerDTO customerDTO);
}
