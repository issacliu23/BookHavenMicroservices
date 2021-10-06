package com.ncs.nusiss.bookservice.book.chapter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDTO {
    private String chapterId;
    private String chapterTitle;
    private Integer chapterNo;
}
