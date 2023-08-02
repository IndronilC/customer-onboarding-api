package com.kanini.corebanking.custonboard.customeronboarding.controller;

import com.kanini.corebanking.custonboard.api.CustomersApi;
import com.kanini.corebanking.custonboard.api.model.Customer;
import com.kanini.corebanking.custonboard.api.model.CustomerRequest;
import com.kanini.corebanking.custonboard.customeronboarding.controller.exception.CustomerOnboardingRequestNotFoundException;
import com.kanini.corebanking.custonboard.customeronboarding.dto.CustomerDTO;
import com.kanini.corebanking.custonboard.customeronboarding.common.errormsg.ErrorMessages;
import com.kanini.corebanking.custonboard.customeronboarding.services.CustomerOnboardingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * <p>{@code CustomerOnboardingController} class acts as the implementor of the
 * {@link com.kanini.corebanking.custonboard.api.CustomersApi} where we have
 * currently implemented the registerCustomer method which handles the end point for
 * {@code POST /customers/register : Registers a new Customer} <em>this onboards a new
 * customer</em> who is trying to create a saving account in our <h2>Core Banking System</h2>
 * using our {@code CustomersApi} which are exposed from <em>Customer Onboarding Service</em>
 * to help a new customer register for a <h2>Savings Account</h2> or <h2>Current Account</h2>
 *</p>
 *
 */
@RestController
@Slf4j
public class CustomerOnboardingController implements CustomersApi{
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CustomerOnboardingService customerOnboardingService;

    @Override
    public ResponseEntity<Customer> registerCustomer(CustomerRequest customerRequest) {
        CustomerDTO customerDTO = getCustomerDTO(customerRequest);
        checkifCustomerDTOIsNullAndThrowError(customerDTO);
        logDebugRequestInformation(customerDTO);
        logCustomerDetails(customerRequest, customerDTO);
        customerDTO = returnTheSavedCustomerDetails(customerDTO);
        return getCustomerResponseEntity(customerDTO);
    }

    private CustomerDTO returnTheSavedCustomerDetails(CustomerDTO customerDTO) {
        CustomerDTO savedCustomerDetails = customerOnboardingService.registerCustomer(customerDTO);
        return savedCustomerDetails;
    }

    private ResponseEntity<Customer> getCustomerResponseEntity(CustomerDTO customerDTO) {
        Customer customer = getCustomer(customerDTO);
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }

    private static void logCustomerDetails(CustomerRequest customerRequest, CustomerDTO customerDTO) {
        log.info("In registerCustomer method post converting {} to {}", customerRequest, customerDTO);
    }

    private Customer getCustomer(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        return customer;
    }

    private CustomerDTO getCustomerDTO(CustomerRequest customerRequest) {
        CustomerDTO customerDTO = modelMapper.map(customerRequest, CustomerDTO.class);
        return customerDTO;
    }

    private void logDebugRequestInformation(CustomerDTO customerDTO) {
        log.info("values of customer dto {} ", customerDTO);
    }

    private void checkifCustomerDTOIsNullAndThrowError(CustomerDTO customerDTO) {
        if(Objects.isNull(customerDTO) || checkAllNullObject(customerDTO)){
            log.error("Customer Data should not be Null, cannot allow progress", customerDTO);
            throw new CustomerOnboardingRequestNotFoundException
                    (ErrorMessages.ERROR_PLEASE_PROVIDE_CUSTOMER_ONBOARDING_INFO.getErrorValue());
        }
    }

    private boolean checkAllNullObject(CustomerDTO customerDTO){
        boolean isAllNull = ObjectUtils.allNull(customerDTO.getAadharNo(),
                customerDTO.getFirstName(),
                customerDTO.getLastName(),
                customerDTO.getMiddleName(), customerDTO.getDob());
        return isAllNull;

    }
}
