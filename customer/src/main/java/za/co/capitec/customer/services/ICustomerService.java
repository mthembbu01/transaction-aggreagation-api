package za.co.capitec.customer.services;



import za.co.capitec.coreapi.dtos.ResponseDto;
import za.co.capitec.coreapi.dtos.customer.records.CustomersRecord;
import za.co.capitec.coreapi.dtos.customer.requests.CreateCustomerRequest;
import za.co.capitec.coreapi.dtos.customer.requests.UpdateCustomerRequest;
import za.co.capitec.coreapi.dtos.customer.response.CustomerResponse;

public interface ICustomerService {
    CustomersRecord findByIdNumber(String idNumber);
    CustomerResponse findAll(int pageNo, int pageSize, String sortBy, String sortDir);
    ResponseDto createCustomer(CreateCustomerRequest createCustomerRequest);
    ResponseDto updateCustomerByIdNumber(String idNumber, UpdateCustomerRequest updateCustomerRequest);
    ResponseDto deleteCustomerByIdNumber(String idNumber);
}

