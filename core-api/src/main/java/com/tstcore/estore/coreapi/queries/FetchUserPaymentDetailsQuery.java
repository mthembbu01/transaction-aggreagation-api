package com.tstcore.estore.coreapi.queries;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FetchUserPaymentDetailsQuery {
    private String userId;
}
