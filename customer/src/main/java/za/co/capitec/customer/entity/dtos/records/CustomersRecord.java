package za.co.capitec.customer.entity.dtos.records;

public record CustomersRecord (String firstName,
        String lastName,
        String mobileNumber,
        String idNumber,
        String email,
        String address,
        boolean activeSw) {
}
