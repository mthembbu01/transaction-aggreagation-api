package za.co.capitec.customer.controllers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import za.co.capitec.coreapi.dtos.ResponseDto;
import za.co.capitec.coreapi.dtos.customer.records.CustomersRecord;
import za.co.capitec.coreapi.dtos.customer.requests.CreateCustomerRequest;
import za.co.capitec.coreapi.dtos.customer.requests.UpdateCustomerRequest;
import za.co.capitec.coreapi.dtos.customer.response.CustomerResponse;
import za.co.capitec.customer.Abstracts.AbstractContainersTest;
import za.co.capitec.customer.entity.Customers;
import za.co.capitec.customer.utils.CustomerUtilities;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CustomerIntegrationTest extends AbstractContainersTest {

    final JdbcTemplate jdbcTemplate;
    final TestRestTemplate testRestTemplate;

    String API_PATH = "/api/v1";
    Customers activeCustomer;
    CreateCustomerRequest createCustomerRequest;
    UpdateCustomerRequest updateCustomerRequest;
    ResponseEntity<Customers> responseEntity;
    String newEmail = UUID.randomUUID().toString()+"@gmail.com";

    @BeforeEach
    void setUp(){
        jdbcTemplate.execute("TRUNCATE TABLE customers RESTART IDENTITY");

        activeCustomer = Customers.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .mobileNumber(CustomerUtilities.setContactNumber())
                .idNumber(CustomerUtilities.setIdNumber())
                .email(CustomerUtilities.setEmail())
                .address("129 Botanic Gardens")
                .activeSw(true)
                .build();

        createCustomerRequest = new CreateCustomerRequest(
                "John",
                "Doe",
                "129 Botanic Gardens",
                CustomerUtilities.setEmail(),
                CustomerUtilities.setContactNumber(),
                CustomerUtilities.setIdNumber());
        updateCustomerRequest = new UpdateCustomerRequest("John",
                "Doe",
                "129 Botanic Gardens",
                CustomerUtilities.updatedEmail(),
                CustomerUtilities.setContactNumber(),
                CustomerUtilities.updatedIdNumber());
    }

    @Test
    @DisplayName(value = "1. Should create a new customer")
    void shouldCreateNewCustomer() {
        //-- Given - see the setup method above
        //-- When
        ResponseEntity<ResponseDto> createCustomerResponse = testRestTemplate.exchange(
                API_PATH
                , POST
                , new HttpEntity<>(createCustomerRequest)
                , ResponseDto.class);
        assertThat(createCustomerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ResponseEntity<CustomerResponse> customers = testRestTemplate.exchange(
                API_PATH+"/customers?pageNo=0&pageSize=10&sortBy=lastName&sortDir=asc"
                , GET
                , null
                , new ParameterizedTypeReference<>() {});
        assertThat(customers.getStatusCode()).isEqualTo(HttpStatus.OK);
        CustomersRecord customerCreated = customers.getBody().getContent()
                .stream()
                .findFirst()
                .orElseThrow();
        //-- comparison for customer request
        assertThat(customerCreated.getFirstName()).isEqualTo(createCustomerRequest.getFirstname());
        assertThat(customerCreated.getEmail()).isEqualTo(createCustomerRequest.getEmail());
        assertThat(customerCreated.getAddress()).isEqualTo(createCustomerRequest.getAddress());
        assertThat(customerCreated.getMobileNumber()).isEqualTo(createCustomerRequest.getMobileNumber());
        assertThat(customerCreated.getIdNumber()).isEqualTo(createCustomerRequest.getIdNumber());
    }

    @Test
    @DisplayName("2. Should update a created customer")
    void shouldUpdateCreatedCustomer() {
        //Given
        //-- When
        ResponseEntity<ResponseDto> createCustomerResponse = testRestTemplate.exchange(
                API_PATH
                , POST
                , new HttpEntity<>(createCustomerRequest)
                , ResponseDto.class);
        //-- then
        assertThat(createCustomerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ResponseEntity<CustomerResponse> customers = testRestTemplate.exchange(
                API_PATH+"/customers?pageNo=0&pageSize=10&sortBy=lastName&sortDir=asc"
                , GET
                , null
                , new ParameterizedTypeReference<>() {});
        assertThat(customers.getStatusCode()).isEqualTo(HttpStatus.OK);
        CustomersRecord customerCreated = customers.getBody().getContent()
                .stream()
                .findFirst()
                .orElseThrow();
        //-- To use to update the existing customer
        String idNumber  = customers.getBody().getContent()
                .stream()
                .filter(customer -> customer.getIdNumber().equals(createCustomerRequest.getIdNumber()))
                .map(CustomersRecord::getIdNumber)
                .findFirst()
                .orElseThrow();
        //-- When
        boolean success = testRestTemplate.exchange(API_PATH+"/"+idNumber
                , PUT
                , new  HttpEntity<>(updateCustomerRequest)
                , Void.class).getStatusCode().is2xxSuccessful();
        //-- get the updated customer using the NEW id number (updated during PUT)
        String updatedIdNumber = updateCustomerRequest.getIdNumber();
        ResponseEntity<CustomersRecord> reloaded = testRestTemplate.exchange(
                API_PATH+"/"+updatedIdNumber
                , GET
                , null
                , new ParameterizedTypeReference<>() {});
        CustomersRecord reloadedCustomer = reloaded.getBody();
        assertThat(reloaded.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(reloadedCustomer.getEmail()).isEqualTo(updateCustomerRequest.getEmail());
    }

    @Test
    @DisplayName("3. Should delete a created customer")
    void shouldDeleteCreatedCustomer() {
        //Given
        //-- When
        ResponseEntity<ResponseDto> createCustomerResponse = testRestTemplate.exchange(
                API_PATH
                , POST
                , new HttpEntity<>(createCustomerRequest)
                , ResponseDto.class);
        //-- then
        assertThat(createCustomerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ResponseEntity<CustomerResponse> customers = testRestTemplate.exchange(
                API_PATH+"/customers?pageNo=0&pageSize=10&sortBy=lastName&sortDir=asc"
                , GET
                , null
                , new ParameterizedTypeReference<>() {});
        assertThat(customers.getStatusCode()).isEqualTo(HttpStatus.OK);
        CustomersRecord customerCreated = customers.getBody().getContent()
                .stream()
                .findFirst()
                .orElseThrow();
        //-- To use to update the existing customer
        String idNumber  = customers.getBody().getContent()
                .stream()
                .filter(customer -> customer.getIdNumber().equals(createCustomerRequest.getIdNumber()))
                .map(CustomersRecord::getIdNumber)
                .findFirst()
                .orElseThrow();
        //-- When
        //-- get the updated customer using the NEW id number (updated during PUT)
        testRestTemplate.exchange(
                API_PATH+"/"+idNumber
                , DELETE
                , null
                , ResponseDto.class)
                .getStatusCode().is2xxSuccessful();
        ResponseEntity<Object> customerByIdResponse = testRestTemplate.exchange(
                API_PATH+"/"+idNumber
                , GET
                , null
                , new ParameterizedTypeReference<>() {});
        assertThat(customerByIdResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }



}
