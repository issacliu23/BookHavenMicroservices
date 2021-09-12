package com.ncs.nusiss.bookservice.book;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Book {
    @Id
    private String bookId;
    private String authorId;
    private String title;
    private String summary;
    private Genre genre;
    // status
    // image/thumbnail
}
