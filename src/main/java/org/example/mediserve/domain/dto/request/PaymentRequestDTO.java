package org.example.mediserve.domain.dto.request;

import lombok.Data;

@Data
public class PaymentRequestDTO {

    private int amount;

    private String currency;

    private String receiptUsername;

}
