package com.ncs.nusiss.bookservice.book.chapter;

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
public class Chapter {
    @Id
    private String chapterId;
    @JsonIgnore
    @DBRef(lazy = true)
    private Book book;
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
