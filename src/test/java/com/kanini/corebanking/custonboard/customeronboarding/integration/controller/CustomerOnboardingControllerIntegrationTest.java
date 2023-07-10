package com.kanini.corebanking.custonboard.customeronboarding.integration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanini.corebanking.custonboard.api.model.Customer;
import com.kanini.corebanking.custonboard.api.model.CustomerRequest;
import com.kanini.corebanking.custonboard.customeronboarding.controller.exception.CustomerOnboardingControllerException;
import com.kanini.corebanking.custonboard.customeronboarding.common.errormsg.ErrorMessages;
import com.kanini.corebanking.custonboard.customeronboarding.data.model.repositories.CustomerOnboardingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class CustomerOnboardingControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CustomerOnboardingRepository customerOnboardingRepository;

    @BeforeEach
    public void setUp() {
        customerOnboardingRepository.deleteAll();
    }

    @Test
    public void givenCustomerOnboardingRequestObject_whenCreateCustomerOnboarding_thenReturnSavedOnboardedCustomer()
            throws Exception {
        CustomerRequest customerRequest = createMockCustomerRequest();
        Customer customer = createMockCustomer();

        // when the operation we are going to check is performed
        MockHttpServletRequestBuilder mockRequest = post("/customers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(getContent(customerRequest));

        ResultActions response = mockMvc.perform(mockRequest);

        // then - verify the result with the new bunch of assert statements.
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.firstName", is(customer.getFirstName())))
                .andExpect(jsonPath("$.middleName", is(customer.getMiddleName())))
                .andExpect(jsonPath("$.lastName", is(customer.getLastName())))
                .andExpect(jsonPath("$.dob", is(customer.getDob().toString())))
                .andExpect(jsonPath("$.aadharNo", is(customer.getAadharNo())));

    }

    /**
     * <p> This is an unit integration level test case that calls the
     * {@link com.kanini.corebanking.custonboard.customeronboarding.controller.CustomerOnboardingController}
     * to test the <em>registerCustomer</em> method which enables {@code @PostMapping} for adding customer
     * details for customer on boarding module</p>
     * <p> This method tests a negative scenario which could be fictitious
     * where an empty {@link com.kanini.corebanking.custonboard.api.model.CustomerRequest} which has null fields
     * if is sent the <em>registerCustomer</em> method of <em>CustomerOnbaordingController</em> class
     * should throw an application exception of type {@link com.kanini.corebanking.custonboard.customeronboarding.controller.exception.CustomerOnboardingControllerException}
     * </p>
     * <p>This test case verifies the same by mocking the Spring MVC layer by using
     * {@link org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc}
     * <em>annotation</em> which in turn allows classes like
     * {@link org.springframework.test.web.servlet.MockMvc} to simulate the Rest API Calls
     * </p>
     *
     * @throws Exception - <p> throws all the uncaught exception to the Spring boot test parent stack
     *                   indicating test failures due to issues which is not being tested currently</p>
     */
    @Test
    public void givenNullCustomerRequestObject_whenCreateCustomer_thenReturnErrorResponse() throws Exception {
        CustomerRequest customerRequest = new CustomerRequest();

        // when the operation we are going to check is performed
        MockHttpServletRequestBuilder mockRequest = post("/customers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(getContent(customerRequest));

        ResultActions response = mockMvc.perform(mockRequest);
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof CustomerOnboardingControllerException))
                .andExpect(result ->
                        assertEquals(ErrorMessages.ERROR_PLEASE_PROVIDE_CUSTOMER_ONBOARDING_INFO.toString(),
                                result.getResolvedException().getMessage()));


    }

    private String getContent(CustomerRequest customerRequest)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(customerRequest);
    }

    private CustomerRequest createMockCustomerRequest() {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setFirstName("Indronil");
        customerRequest.setMiddleName("Kumar");
        customerRequest.setLastName("Chakraborty");
        customerRequest.setDob(LocalDate.parse("1971-07-20"));
        customerRequest.setAadharNo("10295787898");
        return customerRequest;
    }

    private Customer createMockCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("Indronil");
        customer.setMiddleName("Kumar");
        customer.setLastName("Chakraborty");
        customer.setDob(LocalDate.parse("1971-07-20"));
        customer.setAadharNo("10295787898");
        return customer;
    }

}
