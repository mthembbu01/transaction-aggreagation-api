package za.co.capitec.customer.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

//@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Customers extends BaseEntity {
    private Long id;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String idNumber;
    private String email;
    private String address;
    private boolean activeSw;

}
