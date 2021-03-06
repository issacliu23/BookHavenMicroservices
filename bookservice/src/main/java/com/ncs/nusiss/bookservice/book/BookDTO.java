package com.ncs.nusiss.bookservice.book;

import com.ncs.nusiss.bookservice.book.chapter.Chapter;
import com.ncs.nusiss.bookservice.book.chapter.ChapterDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private String bookId;
    private String authorName;
    private String bookTitle;
    private String summary;
    private List<Genre> genreList = new ArrayList<>();
    private Integer pointsRequiredForChapter;
    private Binary coverImage;
    private Double review = 5.0;
    private List<ChapterDTO> chapterList = new ArrayList<>();
    private LocalDate createdDate;
    private LocalDate updatedDate;
}
