package com.ncs.nusiss.bookservice.book.chapter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ncs.nusiss.bookservice.book.Book;
import com.ncs.nusiss.bookservice.book.Genre;
import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.InputStream;

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
}
