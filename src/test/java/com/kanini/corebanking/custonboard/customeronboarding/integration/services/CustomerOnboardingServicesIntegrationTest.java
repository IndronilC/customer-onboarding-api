package com.kanini.corebanking.custonboard.customeronboarding.integration.services;

import com.kanini.corebanking.custonboard.customeronboarding.data.model.repositories.CustomerOnboardingRepository;
import com.kanini.corebanking.custonboard.customeronboarding.dto.CustomerDTO;
import com.kanini.corebanking.custonboard.customeronboarding.services.CustomerOnboardingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <p> This test case is basically checking out the integration
 * service with the data layer, thus, we are bringing in the
 * actual service component and testing the service component's
 * each and every contract or public method which
 * internally calls the repository component which talks to
 * the data layer
 * </p>
 * <p>
 * Here we are using <code>@SpringBootTest</code> to provide
 * us the application context but as we are not testing the
 * web layer we are setting the Web Application Type to None
 * to not have any web environment aka
 * <code>@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)</code>
 * </p>
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("dev")
public class CustomerOnboardingServicesIntegrationTest {

    @Autowired
    CustomerOnboardingService customerOnboardingService;

    private CustomerDTO customerDTO;

    @Autowired
    CustomerOnboardingRepository customerOnboardingRepository;

    @BeforeEach
    public void setup() {
        customerOnboardingRepository.deleteAll();
        customerDTO = createMockCustomerDTO();
    }

    @DisplayName("This test case tests the basic flow or happy path for registerCustomer method in the Service Class")
    @Test
    public void givenCustomerObject_whenSavedCustomerObject_thenReturnSavedCustomer() {
        // the condition to be tested which should return the saved customer entity
        CustomerDTO savedCustomerDTO = customerOnboardingService.registerCustomer(customerDTO);
        assertThat(savedCustomerDTO).isNotNull();

    }

    private CustomerDTO createMockCustomerDTO() {
        CustomerDTO customerDTO = CustomerDTO.builder().firstName("Indronil")
                .lastName("Chawkroborty")
                .middleName("Kumar")
                .dob(LocalDate.parse("1971-07-20"))
                .aadharNo("10295787898")
                .build();
        return customerDTO;
    }


}
