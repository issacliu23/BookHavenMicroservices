package com.ncs.nusiss.bookservice.book.chapter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ncs.nusiss.bookservice.book.Genre;
import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Chapter {
    @Id
    private String chapterId;
    private String bookId;
    private String chapterTitle;
    private Integer chapterNumber;
    private Binary file;
}
