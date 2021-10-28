package com.ncs.nusiss.bookservice.book.chapter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDTO {
    private String chapterId;
    private String chapterTitle;
    private Integer chapterNo;
    private Boolean isLocked = true;
    private LocalDate createdDate;
    private LocalDate updatedDate;
}
