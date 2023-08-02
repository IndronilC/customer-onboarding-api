package com.kanini.corebanking.custonboard.customeronboarding.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanini.corebanking.custonboard.api.model.Customer;
import com.kanini.corebanking.custonboard.api.model.CustomerRequest;
import com.kanini.corebanking.custonboard.customeronboarding.controller.exception.CustomerOnboardingRequestNotFoundException;
import com.kanini.corebanking.custonboard.customeronboarding.common.errormsg.ErrorMessages;
import com.kanini.corebanking.custonboard.customeronboarding.data.model.repositories.CustomerOnboardingRepository;

import static com.kanini.corebanking.custonboard.customeronboarding.util.CustomerOnboardingTestUtil.createMockCustomer;
import static com.kanini.corebanking.custonboard.customeronboarding.util.CustomerOnboardingTestUtil.createMockCustomerRequest;
import static com.kanini.corebanking.custonboard.customeronboarding.util.CustomerOnboardingTestUtil.getContent;

import com.kanini.corebanking.custonboard.customeronboarding.services.exception.CustomerOnboardingBusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.stream.Stream;

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
                .content(getContent(objectMapper,customerRequest));

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
     * should throw an application exception of type {@link CustomerOnboardingRequestNotFoundException}
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
                .content(getContent(objectMapper, customerRequest));

        ResultActions response = mockMvc.perform(mockRequest);
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof CustomerOnboardingRequestNotFoundException))
                .andExpect(result ->
                        assertEquals(ErrorMessages.ERROR_PLEASE_PROVIDE_CUSTOMER_ONBOARDING_INFO.getErrorValue(),
                                result.getResolvedException().getMessage()));


    }

    @ParameterizedTest
    @MethodSource("createStubOfInCompleteCustomerRequest")
    public void givenInCompleteCustomerRequestObject_whenCreateCustomer_thenReturnErrorResponse
            (CustomerRequest inCompleteCustomerRequest) throws Exception {
        CustomerRequest localInCompleteCustomerRequest = inCompleteCustomerRequest;
        MockHttpServletRequestBuilder mockRequest = post("/customers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(getContent(objectMapper, localInCompleteCustomerRequest));
        ResultActions response = mockMvc.perform(mockRequest);
        response.andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof CustomerOnboardingBusinessException))
                .andExpect(result -> assertEquals(
                        ErrorMessages.ERROR_PLEASE_PROVIDE_ALL_REQUISITE_CUSTOMER_ONBOARDING_INFO.getErrorValue(),
                        result.getResolvedException().getMessage()));
    }

    private static Stream<CustomerRequest> createStubOfInCompleteCustomerRequest(){
        CustomerRequest inCompleteCustomerRequest = new CustomerRequest();
        inCompleteCustomerRequest.setFirstName(null);
        inCompleteCustomerRequest.setDob(LocalDate.parse("1971-07-17"));
        inCompleteCustomerRequest.setLastName("Rahman");
        inCompleteCustomerRequest.setFirstName("Syed");
        inCompleteCustomerRequest.setMiddleName("Arifur");
        inCompleteCustomerRequest.aadharNo(null);
        return Stream.of(inCompleteCustomerRequest);
    }







}
