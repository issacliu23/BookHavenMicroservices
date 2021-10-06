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
public class ChapterPointsTransfer extends Auditable{
    @Id
    private String transferId;
    @NotNull
    private String fromWalletId;
    @NotNull
    private String toWalletId;
    @NotNull
    private Double pointsTransferred;
}
