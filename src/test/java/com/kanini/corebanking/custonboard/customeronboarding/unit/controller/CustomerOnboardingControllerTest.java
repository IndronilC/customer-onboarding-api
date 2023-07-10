package com.kanini.corebanking.custonboard.customeronboarding.unit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanini.corebanking.custonboard.api.model.Customer;
import com.kanini.corebanking.custonboard.api.model.CustomerRequest;
import com.kanini.corebanking.custonboard.customeronboarding.controller.CustomerOnboardingController;
import com.kanini.corebanking.custonboard.customeronboarding.controller.exception.CustomerOnboardingControllerException;
import com.kanini.corebanking.custonboard.customeronboarding.common.errormsg.ErrorMessages;
import com.kanini.corebanking.custonboard.customeronboarding.dto.CustomerDTO;
import com.kanini.corebanking.custonboard.customeronboarding.services.CustomerOnboardingService;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import java.time.LocalDate;

/**
 * This Class has the first Unit or Isolated Test Cases for the Controller
 * which implements CustomerOnboarding through /customers/register URI supported
 * by a registerCustomer method. For this test case to work we had to ensure
 * that we have the application context through Mock MVC and we created a mock
 * of Model Mapper which is then passing the stub values for the request and dto
 * for usage in the actual controller method which is called by the Mock MVC's perform
 * to make the model mapper available we have to when and thenReturn method's
 * and then at the end call the <code>mockMvc.perform</code>
 * The WebMVC annotation restricts the controller layer test to the current
 * <code>CustomerOnboardingController.class</code>

 * */
@WebMvcTest(controllers = CustomerOnboardingController.class)
public class CustomerOnboardingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ModelMapper modelMapper;

    @MockBean
    CustomerOnboardingService customerOnboardingService;

    @Autowired
    private ObjectMapper objectMapper;

    /*

     */
    @Test
    public void givenCustomerObject_whenCreateCustomer_thenReturnSavedCustomer()
            throws Exception {
        CustomerRequest customerRequest = createMockCustomerRequest();
        CustomerDTO customerDTO = createMockCustomerDTO();
        Customer customer = createMockCustomer();

        when(modelMapper.map(customerRequest, CustomerDTO.class)).thenReturn(customerDTO);
        when(modelMapper.map(customerDTO, Customer.class)).thenReturn(customer);
        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/customers/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getContent(customerRequest)))
                .andExpect(status().isOk());
        assertRequestAndResponseHasSameValues(customerRequest, customer);
    }

    private String getContent(CustomerRequest customerRequest)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(customerRequest);
    }


    /**
     This test method is created to use the latest assert features of Junit - 5
     Where we use <code>response</code> object which is an instance of
     <code>ResultAction</code> class which helps us in doing the verification
     when the action is called by <code>mockMvc.perform(mockRequest)</code>
     */
    @Test
    public void givenCustomerOnboardingRequestObject_whenCreateCustomerOnboarding_thenReturnSavedOnboardedCustomer()
            throws Exception {
        CustomerRequest customerRequest = createMockCustomerRequest();
        CustomerDTO customerDTO = createMockCustomerDTO();
        Customer customer = createMockCustomer();

        // where the mockbean of instance model mapper is called for conversion of POJO(s)
        when(modelMapper.map(customerRequest, CustomerDTO.class)).thenReturn(customerDTO);
        when(modelMapper.map(customerDTO, Customer.class)).thenReturn(customer);
        // given also that we make a call to the registerCustomer of mockbean of
        // customerOnboardingService with the stubbed CustomerDTO
        when(customerOnboardingService.registerCustomer(customerDTO)).thenReturn(customerDTO);

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

    @Test
    public void givenCustomerOnboardingRequestObject_whenNullCustomerRequestObject_thenReturnStatusAsBadRequest()
        throws Exception {
        // given we have a null request body when the operation we are going to check is performed with a null request body
        MockHttpServletRequestBuilder mockRequest = post("/customers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(getContent(null));

        // verify if we are receiving a bad request - if so test case has passed
        ResultActions response = mockMvc.perform(mockRequest);
        response.andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void givenCustomerRequestObj_whenCustomerDTOObjNull_thenReturnCustomerOnboardingException()
        throws Exception{
        // given the customer Request Object the customer DTO object is null from the mapper.
        CustomerRequest customerRequest = createMockCustomerRequest();
        when(modelMapper.map(customerRequest, CustomerDTO.class)).thenReturn(null);

        // when we perform the action with customer request by making the post request
        MockHttpServletRequestBuilder mockRequest = post("/customers/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(getContent(customerRequest));

        // verify if we are receiving a bad request - if so test case has passed
        // also verify the exception we are throwing and the message
        ResultActions response = mockMvc.perform(mockRequest);
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof CustomerOnboardingControllerException))
                .andExpect(result ->
                        assertEquals(ErrorMessages.ERROR_PLEASE_PROVIDE_CUSTOMER_ONBOARDING_INFO.toString(),
                                result.getResolvedException().getMessage()));


    }

    private static void assertRequestAndResponseHasSameValues(CustomerRequest customerRequest, Customer customer) {
        assertEquals(customerRequest.getFirstName(),
                customer.getFirstName(), "Request and Response has the same firstname");
        assertEquals(customerRequest.getMiddleName(),
                customer.getMiddleName(), "Request and Response has the same middlename");
        assertEquals(customerRequest.getLastName(),
                customer.getLastName(), "Request and Response has the same lastname");
        assertEquals(customerRequest.getDob(),
                customer.getDob(), "Request and Response has the same date of birth");
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

    private CustomerDTO createMockCustomerDTO() {
        CustomerDTO customerDTO = CustomerDTO.builder().firstName("Indronil")
                .lastName("Chakraborty")
                .middleName("Kunar")
                .dob(LocalDate.parse("1971-07-20"))
                .aadharNo("10295787898")
                .build();
        return customerDTO;

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


}
