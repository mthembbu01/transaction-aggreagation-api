package za.co.capitec.customer.services;

import za.co.capitec.customer.entity.dtos.records.CustomersRecord;
import za.co.capitec.customer.entity.dtos.requests.CreateCustomerRequest;
import za.co.capitec.customer.entity.dtos.requests.UpdateCustomerRequest;
import za.co.capitec.customer.entity.dtos.response.ResponseDto;

import java.util.List;

public interface ICustomerService {
    CustomersRecord findByIdNumber(String idNumber);
    List<CustomersRecord> findCustomersByIdNumber(String idNumber);
    ResponseDto createCustomer(CreateCustomerRequest createCustomerRequest);
    ResponseDto updateCustomerByIdNumber(String idNumber, UpdateCustomerRequest updateCustomerRequest);
    ResponseDto deleteCustomerByIdNumber(String idNumber);
}

