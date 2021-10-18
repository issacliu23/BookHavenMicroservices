package com.ncs.nusiss.bookservice.book.chapterAccess;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ncs.nusiss.bookservice.book.Book;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
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
