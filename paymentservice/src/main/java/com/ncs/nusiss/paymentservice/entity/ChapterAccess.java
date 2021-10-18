package com.ncs.nusiss.paymentservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Document
public class ChapterAccess {
    @Id
    private String chapterAccessId;
    @NotNull
    private String chapterId;
    @NotNull
    private String userId;
    private LocalDate createdDate;
    private LocalDate updatedDate;
}
