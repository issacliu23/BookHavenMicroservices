package com.ncs.nusiss.bookservice.book;

import com.ncs.nusiss.bookservice.book.chapter.Chapter;
import com.ncs.nusiss.bookservice.exceptions.BookNotFoundException;
import com.querydsl.core.annotations.QueryEntity;
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
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@QueryEntity
@Document
public class Book {
    @Id
    private String bookId;
    private String authorId;
    private String authorName;
    @NotNull
    private String bookTitle;
    @NotNull
    private String summary;
    @NotEmpty
    private List<Genre> genreList = new ArrayList<>();
    @NotNull
    private Integer pointsRequiredForChapter;
    @DBRef(lazy = true)
    private List<Chapter> chapterList = new ArrayList<>();
    private Binary coverImage;
    // status

    public void addChapter(Chapter c) throws BookNotFoundException {
        if(this.bookId != null) {
            chapterList.add(c);
            c.setBook(this);
        }
        else
            throw new BookNotFoundException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;

        Book book = (Book) o;
        // field comparison
        return Objects.equals(bookId, book.bookId)
                && Objects.equals(authorId, book.authorId)
                && Objects.equals(bookTitle, book.bookTitle)
                && Objects.equals(summary, book.summary)
                && genreList.equals(book.genreList)
                && Objects.equals(pointsRequiredForChapter, book.pointsRequiredForChapter)
                && coverImage.equals(book.coverImage);

    }
}
