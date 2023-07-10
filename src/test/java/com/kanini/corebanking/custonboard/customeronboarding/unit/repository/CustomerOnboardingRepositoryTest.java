package com.kanini.corebanking.custonboard.customeronboarding.unit.repository;

import com.kanini.corebanking.custonboard.customeronboarding.data.model.entities.CustomerEntity;
import com.kanini.corebanking.custonboard.customeronboarding.data.model.repositories.CustomerOnboardingRepository;
import com.kanini.corebanking.custonboard.customeronboarding.util.CustomerOnboardingTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class CustomerOnboardingRepositoryTest {

    @Autowired
    CustomerOnboardingRepository customerOnboardingRepository;

    private CustomerEntity customerEntity;

    @BeforeEach
    public void setup(){
        customerOnboardingRepository.deleteAll();
        customerEntity = CustomerOnboardingTestUtil.createStubbedCustomerEntity();
    }

    @DisplayName("This test case tests the basic flow or happy path for saving customer in -> in-memory database")
    @Test
    public void givenCustomerObject_whenSavedCustomerObject_thenReturnSavedCustomer(){
        CustomerEntity customerLocalEntity = customerOnboardingRepository.save(customerEntity);
        assertThat(customerLocalEntity).isNotNull();
        assertThat(customerLocalEntity.getId()).isNotNull();

    }
}
