package com.kanini.corebanking.custonboard.customeronboarding.util;

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
}
