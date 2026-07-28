package za.co.capitec.coreapi.dtos.accounts.requests;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountsDto {
    private String accountType;

    @Pattern(regexp = "^(\\+27|0)?[1-9]\\d{8}$", message = "Invalid mobile number")
    private String mobileNumber;

    @Pattern(regexp = "^\\d{13}$", message = "Invalid ID Number")
    private String idNumber;

    private String branchAddress;
}
