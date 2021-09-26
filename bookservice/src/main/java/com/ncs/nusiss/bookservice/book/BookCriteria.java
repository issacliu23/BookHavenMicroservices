package com.ncs.nusiss.bookservice.book;

import com.ncs.nusiss.bookservice.book.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCriteria {
    private List<Genre> genreList = new ArrayList<>();
    private String authorId;
    private String authorName;
    private String bookTitle;
}
