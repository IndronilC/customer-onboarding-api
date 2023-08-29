package com.kanini.corebanking.custonboard.customeronboarding.unit.services;

import com.kanini.corebanking.custonboard.customeronboarding.common.util.datefunction.CustomerOnboardDateUtil;
import com.kanini.corebanking.custonboard.customeronboarding.data.model.entities.CustomerEntity;
import com.kanini.corebanking.custonboard.customeronboarding.data.model.entities.CustomerIdentificationEntity;
import com.kanini.corebanking.custonboard.customeronboarding.data.model.repositories.CustomerIdentificationRepository;
import com.kanini.corebanking.custonboard.customeronboarding.data.model.repositories.CustomerOnboardingRepository;
import com.kanini.corebanking.custonboard.customeronboarding.dto.CustomerDTO;
import com.kanini.corebanking.custonboard.customeronboarding.services.CustomerOnboardingService;
import com.kanini.corebanking.custonboard.customeronboarding.services.exception.CustomerOnboardingBusinessException;
import com.kanini.corebanking.custonboard.customeronboarding.services.impl.CustomerOnboardingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CustomerOnboardingServicesTest {
    @Mock
    CustomerOnboardingRepository customerOnboardingRepository;

    @Mock
    CustomerIdentificationRepository customerIdentificationRepository;

    @Mock
    ModelMapper modelMapper;
    @InjectMocks
    CustomerOnboardingService customerOnboardingService = new CustomerOnboardingServiceImpl();

    private CustomerDTO customerDTO;
    private CustomerEntity customerEntity;

    private CustomerDTO customerIdentificationDTO;

    private CustomerIdentificationEntity customerIdentificationEntity;

    @BeforeEach
    public void setup(){
        customerDTO = createMockCustomerDTO();
        customerEntity = createMockCustomerEntity();
        customerIdentificationEntity = createMockCustomerIdentificationEntity();
        customerEntity.setCustomerIdentificationEntity(customerIdentificationEntity);
        customerIdentificationEntity.setCustomerEntity(customerEntity);
        customerIdentificationDTO = createMockCustomerIdentificationDTO();
    }



    @DisplayName("This test case tests the basic flow or happy path for registerCustomer method in the Service Class")
    @Test
    public void givenCustomerObject_whenSavedCustomerObject_thenReturnSavedCustomer(){
        buildThePreConditionForTheSaveCustomerTest();


        // the condition to be tested which should return the saved customer entity
      CustomerDTO savedCustomerDTO = customerOnboardingService.registerCustomer(customerDTO);
      assertThat(savedCustomerDTO).isNotNull();
      assertThat(savedCustomerDTO.getAadharNo()).isEqualTo(customerDTO.getAadharNo());
    }

    private void buildThePreConditionForTheSaveCustomerTest() {
        // given - PreCondition
        // where the model mapper converts customerDTO to the corresponding customerEntity
        // where the customer repository saves the customerEntity.
        given(modelMapper.map(customerDTO, CustomerEntity.class)).willReturn(customerEntity);
        // where we need to also create the customerIdentification Entity as there is a one to one relationship
        // between the customer and customer_identification table using proof_id which we need to mock for the test.
        // the data for customerIdentification Entity will come from in coming Customer DTO which is formed from the
        // Web Request
        given(modelMapper.map(customerDTO, CustomerIdentificationEntity.class)).willReturn(customerIdentificationEntity);
        // As there is an one to one relationship we need to provide a Pre-Condition where we save both the data.
        given(customerOnboardingRepository.save(customerEntity)).willReturn(customerEntity);
        given(customerIdentificationRepository.save(customerIdentificationEntity)).willReturn(customerIdentificationEntity);
        // Pre-Condition needs to include the mocking of creation of customer DTO which will be returned to the
        // Controller layer once the data is saved in database, the same is mocked here
        given(modelMapper.map(customerEntity, CustomerDTO.class)).willReturn(customerDTO);
        given(modelMapper.map(customerEntity.getCustomerIdentificationEntity(), CustomerDTO.class)).willReturn(customerIdentificationDTO);
    }

    @DisplayName("This test case checks the negative test for registerCustomer method when customerEntity is Null")
    @Test
    public void givenCustomerDTO_whenMappedCustomerEntityIsNull_thenThrowBusinessException(){
        // given - PreCondition
        // when we provide CustomerDTO instance which has values but the customer entity is null
        // this may be a fictitious condition but may happen due to mapper failure or accidentally
        // not having a mapper instance or mapper could not convert may be
        given(modelMapper.map(customerDTO, CustomerEntity.class)).willReturn(null);

        // when -  action or the behaviour that we are going test
        org.junit.jupiter.api.Assertions.assertThrows(CustomerOnboardingBusinessException.class, () -> {
            customerOnboardingService.registerCustomer(customerDTO);
        });

        // then we verify that it is not allowing save in the customer table
        verify(customerOnboardingRepository, never()).save(any(CustomerEntity.class));
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

    private CustomerEntity createMockCustomerEntity() {
        CustomerEntity customerEntity = CustomerEntity.builder().id(UUID.randomUUID())
                .firstName("Indronil")
                .middleName("Kumar")
                .lastName("Chawkroborty")
                .dob(LocalDate.parse("1971-07-20"))
                .build();
        return customerEntity;
    }

    private CustomerDTO createMockCustomerIdentificationDTO() {
        CustomerDTO localCustomerIdentificationDTO = CustomerDTO.builder()
                .aadharNo("10295787898").build();
        return localCustomerIdentificationDTO;
    }

    private CustomerIdentificationEntity createMockCustomerIdentificationEntity() {
        CustomerIdentificationEntity customerIdentificationTempEntity =
                CustomerIdentificationEntity.builder()
                        .proofId(UUID.randomUUID())
                        .aadharNo(new BigInteger("10295787898"))
                        .createdAt(CustomerOnboardDateUtil.getNow())
                        .build();
        return customerIdentificationTempEntity;
    }

}
