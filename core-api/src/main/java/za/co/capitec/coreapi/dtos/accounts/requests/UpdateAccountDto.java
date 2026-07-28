package za.co.capitec.coreapi.dtos.accounts.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountDto {
    private String mobileNumber;
    private String idNumber;
    private String branchAddress;
}
