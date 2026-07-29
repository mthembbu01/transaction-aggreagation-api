package za.co.capitec.customer.utilities.dates;

import org.springframework.data.domain.Page;
import za.co.capitec.coreapi.dtos.customer.records.CustomersRecord;
import za.co.capitec.coreapi.dtos.customer.response.CustomerResponse;
import za.co.capitec.customer.entity.Customers;

import java.util.List;

public class CustomerUtils {
    /**
     *
     * @param customer
     * @return
     */
    public static CustomersRecord toRecord(Customers customer) {
        return new CustomersRecord(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getMobileNumber(),
                customer.getIdNumber(),
                customer.getEmail(),
                customer.getAddress(),
                customer.isActiveSw()
        );
    }
    /**
     *
     * @param pages
     * @return
     */
    public static CustomerResponse toCustomerResponse(Page<Customers> pages){
        //-- 1. Define a list of Customer Records
        List<CustomersRecord> content = pages.getContent()
                .stream()
                .map(CustomerUtils::toRecord)
                .toList();
        //-- 2. Return the Customer Response for pagination
        return CustomerResponse.builder()
                .content(content)
                .pageNo(pages.getNumber())
                .pageSize(pages.getSize())
                .totalPages(pages.getTotalPages())
                .totalElements(pages.getTotalElements())
                .isLast(pages.isLast())
                .build();
    }
}
