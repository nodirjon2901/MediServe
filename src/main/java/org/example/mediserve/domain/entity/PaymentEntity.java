package org.example.mediserve.domain.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "payment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentEntity extends BaseEntity{

    private String receiptUsername;

    private String currency;

    private double amount;

}
