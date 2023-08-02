package com.kanini.corebanking.custonboard.customeronboarding.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.kanini.corebanking.custonboard.api.model.Customer;
import com.kanini.corebanking.custonboard.api.model.CustomerRequest;
import com.kanini.corebanking.custonboard.customeronboarding.common.util.CustomerOnboardDateUtil;
import com.kanini.corebanking.custonboard.customeronboarding.data.model.entities.CustomerEntity;

import java.time.LocalDate;
import java.util.UUID;

public class CustomerOnboardingTestUtil {
    protected CustomerOnboardingTestUtil() {}
    public static CustomerEntity createStubbedCustomerEntity() {
        CustomerEntity customerEntity = CustomerEntity.builder().id(UUID.randomUUID())
                .firstName("Indronil")
                .middleName("Kumar")
                .lastName("Chawkroborty")
                .dob(LocalDate.parse("1971-07-20"))
                .createdAt(CustomerOnboardDateUtil.getNow())
                .updatedAt(CustomerOnboardDateUtil.getNow())
                .build();
        return customerEntity;
    }

    public static Customer createMockCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Indronil");
        customer.setMiddleName("Kumar");
        customer.setLastName("Chakraborty");
        customer.setDob(LocalDate.parse("1971-07-20"));
        customer.setAadharNo("10295787898");
        return customer;
    }

    public static CustomerRequest createMockCustomerRequest() {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setFirstName("Indronil");
        customerRequest.setMiddleName("Kumar");
        customerRequest.setLastName("Chakraborty");
        customerRequest.setDob(LocalDate.parse("1971-07-20"));
        customerRequest.setAadharNo("10295787898");
        return customerRequest;
    }

    public static String getContent(ObjectMapper objectMapper,CustomerRequest customerRequest)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(customerRequest);
    }

}
