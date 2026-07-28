package com.tstcore.estore.coreapi.events;

import com.tstcore.estore.coreapi.entities.PaymentDetails;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentProcessedEvent {
    private String paymentId;
    private String orderId;
    private PaymentDetails paymentDetails;
}
