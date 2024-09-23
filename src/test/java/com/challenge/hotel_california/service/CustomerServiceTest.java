package com.challenge.hotel_california.service;

import com.challenge.hotel_california.DTOs.*;
import com.challenge.hotel_california.exceptions.CustomerNotFoundException;
import com.challenge.hotel_california.model.Customer;
import com.challenge.hotel_california.repository.CustomerRepository;
import com.challenge.hotel_california.validatorRefactor.customers.IValidatorCustomers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @InjectMocks
    private CustomerService customerService;
    @Mock
    private CustomerRepository customerRepository;
    @Spy
    private List<IValidatorCustomers> verifyValidators = new ArrayList<>();
    @Mock
    private IValidatorCustomers validator1;
    @Mock
    private IValidatorCustomers validator2;
    @Mock
    private Pageable pageable;
    @Mock
    private Page<CustomerOutputGetListDTO> listAllCustomers;
    @Mock
    private Page<Customer> findCustomers;

    private CustomerEntryDTO customerEntryDTO;
    @Captor
    private ArgumentCaptor<Customer> argumentCaptor;
    @Mock
    private Customer customerObj;
    private CustomerUpdateEntryDTO customerUpdateEntryDTO;

    @Test
    @DisplayName("Should return list of all customers")
    void lisAllCustomersScenario01() {
        //Arrange
        this.customerEntryDTO = new CustomerEntryDTO("customer name", "customer#email.com", "94994594565");
        Customer customer1 = new Customer(customerEntryDTO);
        List<Customer> customerList = new ArrayList<>();
        customerList.add(customer1);
        this.findCustomers = new PageImpl<>(customerList, pageable, customerList.size());
        BDDMockito.given(customerRepository.findAll(pageable)).willReturn(findCustomers);

        //Act
        listAllCustomers = customerService.lisAllCustomers(pageable);

        //Assert
        Assertions.assertAll(
                () -> Assertions.assertNotNull(listAllCustomers),
                () -> Assertions.assertEquals(1, listAllCustomers.getTotalPages()),
                () -> Assertions.assertEquals(findCustomers.getTotalElements(), listAllCustomers.getTotalElements()),
                () -> Assertions.assertEquals(findCustomers.getNumber(), listAllCustomers.getNumber()),
                () -> Assertions.assertEquals(findCustomers.map(CustomerOutputGetListDTO::new), listAllCustomers)
        );
    }

    @Test
    @DisplayName("Should return an Exception when list is empty")
    void lisAllCustomersScenario02() {
        //Arrange
        BDDMockito.given(customerRepository.findAll(pageable)).willReturn(findCustomers);
        BDDMockito.given(findCustomers.isEmpty()).willReturn(true);

        //Assert //Act
        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService.lisAllCustomers(pageable));

    }

    @Test
    @DisplayName("Should save a customer when method is called")
    void addCustomerScenario01() {
        //Arrange
        this.customerEntryDTO = new CustomerEntryDTO("customer name", "customer#email.com", "94994594565");
        //Act
        customerService.addCustomer(customerEntryDTO);
        //assert
        BDDMockito.then(customerRepository).should().save(argumentCaptor.capture());
        Customer customerSave = argumentCaptor.getValue();
        Assertions.assertAll(
                () -> Assertions.assertEquals(customerEntryDTO.name(), customerSave.getName()),
                () -> Assertions.assertEquals(customerEntryDTO.email(), customerSave.getEmail()),
                () -> Assertions.assertEquals(customerEntryDTO.phone(), customerSave.getPhone())
        );
    }

    @Test
    @DisplayName("Should call validators list when add a customer")
    void addCustomerScenario02() {
        //Arrange
        this.customerEntryDTO = new CustomerEntryDTO("customer name", "customer#email.com", "94994594565");
        this.verifyValidators.add(validator1);
        this.verifyValidators.add(validator2);
        //Act
        customerService.addCustomer(customerEntryDTO);
        //assert
        BDDMockito.then(validator1).should().verifyCustomersValidators(customerEntryDTO);
        BDDMockito.then(validator2).should().verifyCustomersValidators(customerEntryDTO);
    }

    @Test
    @DisplayName("Should get a customer by id when validators pass")
    void getDetailsOfASpecificCustomerScenario01() {
        //Arrange
        Long id = 1l;
        BDDMockito.given(customerRepository.getReferenceById(id)).willReturn(customerObj);
        BDDMockito.given(customerRepository.existsById(id)).willReturn(true);
        //Act
        CustomerGetByIdDTO customerDTO = customerService.getDetailsOfASpecificCustomer(id);
        //Assert
        Assertions.assertEquals(customerDTO.name(), customerObj.getName());
        Assertions.assertEquals(customerDTO.email(), customerObj.getEmail());
        Assertions.assertEquals(customerDTO.phone(), customerObj.getPhone());
        Assertions.assertEquals(customerDTO.bookings(), customerObj.getBookings());
    }

    @Test
    @DisplayName("Should return an exception when customer not exist")
    void getDetailsOfASpecificCustomerScenario02() {
        //Arrange
        Long id = 1L;
        BDDMockito.given(customerRepository.getReferenceById(id)).willReturn(customerObj);
        BDDMockito.given(customerRepository.existsById(id)).willReturn(false);
        //Act &Assert
        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService
                .getDetailsOfASpecificCustomer(id));
    }

    @Test
    @DisplayName("Should return a updating customer when validators pass")
    void updateAnExistingCustomerScenario01() {
        //Arrange
        Long id = 1L;

        this.customerUpdateEntryDTO = new CustomerUpdateEntryDTO(id, "customer name", "customer#email.com", "94994594565");
        BDDMockito.given(customerRepository.existsById(id)).willReturn(true);
        BDDMockito.given(customerRepository.getReferenceById(id)).willReturn(customerObj);
        BDDMockito.given(customerObj.getId()).willReturn(id);
        // Act
        CustomerOutputDTO customerOutputDTO = customerService.updateAnExistingCustomer(customerUpdateEntryDTO, id);

        // Assert
        BDDMockito.then(customerObj).should().updateCustomer(customerUpdateEntryDTO);
        BDDMockito.then(customerRepository).should().save(customerObj);
        Assertions.assertEquals(customerOutputDTO.name(), customerObj.getName());
        Assertions.assertEquals(customerOutputDTO.email(), customerObj.getEmail());
        Assertions.assertEquals(customerOutputDTO.phone(), customerObj.getPhone());
    }

    @Test
    @DisplayName("Should throws an exception when id not exists not pass - UPDATE")
    void updateAnExistingCustomerScenario02() {
        //Arrange
        Long id = 1L;

        this.customerUpdateEntryDTO = new CustomerUpdateEntryDTO(id, "customer name", "customer#email.com", "94994594565");
        BDDMockito.given(customerRepository.existsById(id)).willReturn(false);
        // Act & Assert
        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService
                .updateAnExistingCustomer(customerUpdateEntryDTO, id));
    }

    @Test
    @DisplayName("Should throws an exception when id customer not equal customerDto - UPDATE")
    void updateAnExistingCustomerScenario03() {
        //Arrange
        Long id = 1L;

        this.customerUpdateEntryDTO = new CustomerUpdateEntryDTO(id, "customer name", "customer#email.com", "94994594565");
        BDDMockito.given(customerRepository.existsById(id)).willReturn(true);
        BDDMockito.given(customerRepository.getReferenceById(id)).willReturn(customerObj);
        BDDMockito.given(customerObj.getId()).willReturn(2L);
        // Act & Assert
        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService
                .updateAnExistingCustomer(customerUpdateEntryDTO, id));
    }

    @Test
    @DisplayName("Should delete a customer when validators pass")
    void deleteACustomerScenario01() {
        //Arrange
        Long id = 1L;
        BDDMockito.given(customerRepository.getReferenceById(id)).willReturn(customerObj);
        BDDMockito.given(!customerRepository.existsById(id)).willReturn(true);
        //Act
        customerService.deleteACustomer(id);
        //Assert
        BDDMockito.then(customerObj).should().deleteCustomer();
    }

    @Test
    @DisplayName("Should show an exception when id customer not exist - DELETE")
    void deleteACustomerScenario02() {
        //Arrange
        Long id = 1L;
        BDDMockito.given(customerRepository.getReferenceById(id)).willReturn(customerObj);
        BDDMockito.given(!customerRepository.existsById(id)).willReturn(false);
        // Act & Assert
        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService
                .deleteACustomer(id));
    }

    @Test
    @DisplayName("Should list all activated customers when validators pass")
    void listAllActivatedCustomersScenario01() {
        //Arrange
        Customer customer2 = new Customer(1L, "customer name", "customer@email.com", "94994394934", null, false);
        List<Customer> customers = new ArrayList<>();
        customers.add(customer2);
        Page<Customer> activeCustomer = new PageImpl<>(customers, pageable, customers.size());
        BDDMockito.given(customerRepository.findByIsDeletedIsFalse(pageable)).willReturn(activeCustomer);
        //Act
        Page<CustomerOutputGetActivatedListDTO> result =
                customerService.listAllActivatedCustomers(pageable);
        //Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getTotalPages(), 1);
        Assertions.assertEquals(result.getNumber(), activeCustomer.getNumber());
        Assertions.assertEquals(result.getContent().get(0).name(), activeCustomer.getContent().get(0).getName());
        Assertions.assertEquals(result.getContent().get(0).email(), activeCustomer.getContent().get(0).getEmail());
        Assertions.assertEquals(result.getContent().get(0).phone(), activeCustomer.getContent().get(0).getPhone());
    }

    @Test
    @DisplayName("Should return an exception when  validators not pass - ListActivated")
    void listAllActivatedCustomersScenario02() {
        //Arrange
        BDDMockito.given(customerRepository.findByIsDeletedIsFalse(pageable)).willReturn(findCustomers);
        BDDMockito.given(findCustomers.isEmpty()).willReturn(true);

        //Assert //Act
        Assertions.assertThrows(CustomerNotFoundException.class, () -> customerService
                .listAllActivatedCustomers(pageable));
    }
}