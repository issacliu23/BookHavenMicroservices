package com.ncs.nusiss.bookservice.book.chapterAccess;

import com.ncs.nusiss.bookservice.book.chapter.Chapter;
import com.ncs.nusiss.bookservice.book.chapter.ChapterService;
import com.ncs.nusiss.bookservice.exceptions.ChapterAccessExistsException;
import com.ncs.nusiss.bookservice.exceptions.ChapterAccessNotFoundException;
import com.ncs.nusiss.bookservice.exceptions.ChapterNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class ChapterAccessController {

    @Autowired
    private ChapterAccessService chapterAccessService;

    @Autowired
    private ChapterService chapterService;

    @GetMapping("/chapterAccess/{chapterId}/{userId}")
    public ResponseEntity<?> getChapterAccess(@PathVariable(value = "chapterId") String chapterId, @PathVariable(value = "userId") String userId) {
        try {
            ChapterAccess chapterAccess = chapterAccessService.getChapterAccess(chapterId, userId);
            if (chapterAccess != null)
                return new ResponseEntity<>(chapterAccess, HttpStatus.OK);
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (IllegalArgumentException | ChapterAccessNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/chapterAccess/{chapterId}/{userId}")
    public ResponseEntity<?> addChapterAccess(@PathVariable(value = "chapterId") String chapterId, @PathVariable(value = "userId") String userId) {
        try {
            Chapter chapter = chapterService.getChapterDetail(chapterId);
            if (chapter != null) {
                ChapterAccess chapterAccess = chapterAccessService.addChapterAccess(chapterId, userId);
                return new ResponseEntity<>(chapterAccess, HttpStatus.OK);
            } else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (IllegalArgumentException | ChapterNotFoundException | ChapterAccessExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
