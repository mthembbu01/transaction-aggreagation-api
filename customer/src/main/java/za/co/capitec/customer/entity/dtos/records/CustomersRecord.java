package za.co.capitec.customer.entity.dtos.records;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomersRecord {
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String idNumber;
    private String email;
    private String address;
    private boolean activeSw;
}
