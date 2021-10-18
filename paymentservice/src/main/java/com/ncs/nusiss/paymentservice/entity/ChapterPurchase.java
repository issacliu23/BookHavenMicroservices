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
public class ChapterPurchase extends Auditable {
    @Id
    private String chapterPurchaseId;
    @NotNull
    private String chapterId;
    @NotNull
    private String userId;
    @NotNull
    private String transferId;
}
