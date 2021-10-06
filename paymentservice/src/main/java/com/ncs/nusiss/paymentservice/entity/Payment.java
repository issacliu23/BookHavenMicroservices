package com.ncs.nusiss.paymentservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Payment extends Auditable {
    @Id
    private String paymentId;
    @NotNull
    private String userId;
    @NotNull
    private Double amountPaid;
}
