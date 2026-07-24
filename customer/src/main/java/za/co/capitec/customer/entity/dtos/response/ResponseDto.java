package za.co.capitec.customer.entity.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseDto {
    private String statusCode;
    private String statusMsg;
}

