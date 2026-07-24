package za.co.capitec.accounts.dtos.requests;

public record UpdateAccountDto(String mobileNumber,
                               String idNumber,
                               String branchAddress) {
}
