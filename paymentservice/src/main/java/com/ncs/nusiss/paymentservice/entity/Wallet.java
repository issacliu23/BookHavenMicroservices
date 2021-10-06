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
public class Wallet extends Auditable {
    @Id
    private String walletId;
    @NotNull
    private String userId;
    private Double currentPoints;

}
