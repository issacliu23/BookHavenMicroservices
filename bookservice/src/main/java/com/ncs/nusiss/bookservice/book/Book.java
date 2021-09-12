package com.ncs.nusiss.bookservice.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ncs.nusiss.bookservice.book.chapter.Chapter;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class Book {
    @Id
    private String bookId;
    private String authorId;
    private String title;
    private String summary;
    private Genre genre;
    @DBRef(lazy = true)
    private List<Chapter> chapters = new ArrayList<>();
    // status
    // image/thumbnail
}
