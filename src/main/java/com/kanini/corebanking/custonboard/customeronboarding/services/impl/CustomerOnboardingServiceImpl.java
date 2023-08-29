package com.kanini.corebanking.custonboard.customeronboarding.services.impl;

import com.kanini.corebanking.custonboard.customeronboarding.common.util.datefunction.CustomerOnboardDateUtil;
import com.kanini.corebanking.custonboard.customeronboarding.common.errormsg.ErrorMessages;
import com.kanini.corebanking.custonboard.customeronboarding.data.model.entities.CustomerEntity;
import com.kanini.corebanking.custonboard.customeronboarding.data.model.entities.CustomerIdentificationEntity;
import com.kanini.corebanking.custonboard.customeronboarding.data.model.repositories.CustomerIdentificationRepository;
import com.kanini.corebanking.custonboard.customeronboarding.data.model.repositories.CustomerOnboardingRepository;
import com.kanini.corebanking.custonboard.customeronboarding.dto.CustomerDTO;
import com.kanini.corebanking.custonboard.customeronboarding.services.CustomerOnboardingService;
import com.kanini.corebanking.custonboard.customeronboarding.services.exception.CustomerOnboardingBusinessException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class CustomerOnboardingServiceImpl implements CustomerOnboardingService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CustomerOnboardingRepository customerOnboardingRepository;

    @Autowired
    CustomerIdentificationRepository customerIdentificationRepository;

    /**
     * <p> The registerCustomer method implements how <br/>
     * 1. The customerDTO will be converted into the Customer Entity with the help of the mapper.
     * 2. There will be a null check on the converted Entity and if valid then the repository will
     * be called to save the data in the database.
     * 3. If the converted Entity is null we will create a Custom Exception and throw the same.
     * 4. Any other Exception related to data saving will be wrapped in the CustomerOnboardingException
     * and thrown.
     * </p>
     *
     * @param customerDTO
     * @return
     */
    @Override
    @Transactional
    public CustomerDTO registerCustomer(CustomerDTO customerDTO) {
        CustomerEntity customerEntity = getCustomerEntityMapped(customerDTO);
        try {

            CustomerEntity savedCustomerEntity = saveCustomer(customerEntity);
            CustomerIdentificationEntity savedCustomerIdentificationEntity =
                    saveCustomerIdentification(customerEntity.getCustomerIdentificationEntity());
            customerDTO = getSavedCustomerDTO(savedCustomerEntity);
        } catch (Exception e) {
            log.error("Exception in registerCustomer {},{}", e, e.getMessage());
            throw new CustomerOnboardingBusinessException(ErrorMessages.ERROR_PLEASE_PROVIDE_ALL_REQUISITE_CUSTOMER_ONBOARDING_INFO.getErrorValue(), e);
        }
        return customerDTO;
    }

    private CustomerIdentificationEntity saveCustomerIdentification
            (CustomerIdentificationEntity customerIdentificationEntity) {
        return customerIdentificationRepository.save(customerIdentificationEntity);
    }

    private CustomerEntity getCustomerEntityMapped(CustomerDTO customerDTO) {
        CustomerEntity customerEntity = getCustomerEntity(customerDTO);
        CustomerIdentificationEntity customerIdentificationEntity = getCustomerIdentificatiomEntity(customerDTO);
        checkCustomerEntityIsNullAndThrowBusinessException(customerEntity);
        addNeededDateValuesToCustomerEntity(customerEntity, customerIdentificationEntity);
        createOnetoOneRelationshipForCustomerEntity(
                customerEntity, customerIdentificationEntity);
        return customerEntity;
    }

    private void createOnetoOneRelationshipForCustomerEntity
            (CustomerEntity customerEntity, CustomerIdentificationEntity customerIdentificationEntity) {
        customerEntity.setCustomerIdentificationEntity(customerIdentificationEntity);
        customerIdentificationEntity.setCustomerEntity(customerEntity);
    }

    private CustomerIdentificationEntity
    getCustomerIdentificatiomEntity(CustomerDTO customerDTO) {
        CustomerIdentificationEntity customerIdentificationEntity =
                modelMapper.map(customerDTO, CustomerIdentificationEntity.class);
        return customerIdentificationEntity;

    }

    private void addNeededDateValuesToCustomerEntity(CustomerEntity customerEntity, CustomerIdentificationEntity customerIdentificationEntity) {
        addNeedSystemTimeValuesToCustomer(customerEntity);
        addNeededSystemTimeValuesToCustomerIdentification(customerIdentificationEntity);
    }

    private void addNeededSystemTimeValuesToCustomerIdentification
            (CustomerIdentificationEntity customerIdentificationEntity) {
        customerIdentificationEntity.setCreatedAt(CustomerOnboardDateUtil.getNow());
        customerIdentificationEntity.setUpdatedAt(CustomerOnboardDateUtil.getNow());

    }

    private static void addNeedSystemTimeValuesToCustomer(CustomerEntity customerEntity) {
        customerEntity.setCreatedAt(CustomerOnboardDateUtil.getNow());
        customerEntity.setUpdatedAt(CustomerOnboardDateUtil.getNow());
    }

    /**
     * <p>This method actually works to get back the customer onboarding data that is
     * saved in the customer schema in the MySQL database instance of Customer onboarding
     * service</p><br/>
     * <p>Customer onboarding data is saved into two tables <h2>customer</h2> and
     * <h2>cuctomer_identification</h2> where there is an one to one relationship using
     * <h3>proof_id</h3> of the <h3>customer_identification</h3> table, thus when the data is
     * saved and returned as an instance of
     * {@CCode com.kanini.corebanking.custonboard.customeronboarding.data.model.entities.CustomerEntity}
     * we need retrieved the data which is <h4>aadharNo</h4> stored in the child instance of
     * {@code com.kanini.corebanking.custonboard.customeronboarding.data.model.entities.CustomerIdentificationEntity}
     * </p>
     * <p>As just by mapping CustomerEntity instance we will not get all the saved data for the to be returned
     * instance of {@Code com.kanini.corebanking.custonboard.customeronboarding.dto.CustomerDTO}
     * So we are using the child instance of <h4>CustomerIdentificationEntity</h4> to get back the data
     * into another instance of <h4>customerIdentificationDTO</h4> which has only the <B>customer identification</B> data
     * and then adjust to the first or <h4>customerDTO</h4> which maps or gets the
     * Customer data from <h2>customer.customer</h2> table
     * </p>
     *
     * @param savedCustomerEntity - <p>takes the save customer data which includes the customer identification data also
     *                            once saved</p>
     * @return - returns all inclusive instance of
     * {@Code com.kanini.corebanking.custonboard.customeronboarding.dto.CustomerDTO} which has complete
     * saved <B>Customer</B> data which is maintained as a one ot one relationship between two tables
     * <h2>customer.customer</h2> and <h2>customer.customeridentification</h2>
     */
    private CustomerDTO getSavedCustomerDTO(CustomerEntity savedCustomerEntity) {
        CustomerDTO customerDTO;
        CustomerDTO customerIdentificationDTO;
        customerDTO = modelMapper.map(savedCustomerEntity, CustomerDTO.class);
        customerIdentificationDTO = modelMapper.map(savedCustomerEntity.getCustomerIdentificationEntity(),
                CustomerDTO.class);
        customerDTO.setAadharNo(customerIdentificationDTO.getAadharNo());
        return customerDTO;
    }

    private CustomerEntity getCustomerEntity(CustomerDTO customerDTO) {
        CustomerEntity customerEntity = modelMapper.map(customerDTO, CustomerEntity.class);
        return customerEntity;
    }

    private CustomerEntity saveCustomer(CustomerEntity customerEntity) {
        return customerOnboardingRepository.save(customerEntity);
    }

    private void checkCustomerEntityIsNullAndThrowBusinessException
            (CustomerEntity customerEntity) {
        if (Objects.isNull(customerEntity)) {
            log.error("Fatal::Customer data is Null in the Service Layer,");
            throw new CustomerOnboardingBusinessException
                    (ErrorMessages.ERROR_PLEASE_PROVIDE_CUSTOMER_ONBOARDING_INFO.toString());
        } else {
            log.info("Customer Entity has values = {} ", customerEntity);
        }
    }
}
