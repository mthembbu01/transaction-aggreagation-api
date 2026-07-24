package za.co.capitec.creditcards.dtos.response;

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

