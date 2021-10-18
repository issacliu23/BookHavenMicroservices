package com.ncs.nusiss.paymentservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.time.LocalDate;

@Data
@Document
public class Chapter {
    @Id
    private String chapterId;
    @NotNull
    private String chapterTitle;
    @NotNull
    private Integer chapterNo;
    private InputStream stream;
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private String authorId;
    private Integer pointsRequiredForChapter;
}
