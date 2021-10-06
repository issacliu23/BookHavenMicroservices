package com.ncs.nusiss.paymentservice.entity;

import com.ncs.nusiss.paymentservice.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class TransactionHistory extends Auditable {
    @Id
    private String transactionId;
    private BigDecimal cashDeducted;
    private Double pointsDeducted;
    private Double pointsAwarded;
    private TransactionType transactionType;
    private String referenceId;
}
