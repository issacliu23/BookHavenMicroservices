package com.ncs.nusiss.bookservice.book;

import com.ncs.nusiss.bookservice.book.chapter.Chapter;
import com.ncs.nusiss.bookservice.exceptions.BookNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Book {
    @Id
    private String bookId;
    private String authorId;
    @NotNull
    private String title;
    @NotNull
    private String summary;
    @NotEmpty
    private List<Genre> genreList = new ArrayList<>();
    @DBRef(lazy = true)
    private List<Chapter> chapterList = new ArrayList<>();
    private Binary coverImage;
    // status

    public void addChapter(Chapter c) throws BookNotFoundException {
        if(this.bookId != null) {
            chapterList.add(c);
            c.setBookId(bookId);
        }
        else
            throw new BookNotFoundException();
    }
}
