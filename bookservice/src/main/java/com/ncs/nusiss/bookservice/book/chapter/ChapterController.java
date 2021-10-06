package com.ncs.nusiss.bookservice.book.chapter;

import com.ncs.nusiss.bookservice.book.Book;
import com.ncs.nusiss.bookservice.book.BookRepository;
import com.ncs.nusiss.bookservice.book.BookService;
import com.ncs.nusiss.bookservice.exceptions.BookNotFoundException;
import com.ncs.nusiss.bookservice.exceptions.ChapterNotFoundException;
import com.ncs.nusiss.bookservice.exceptions.IncorrectFileExtensionException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

import static com.ncs.nusiss.bookservice.BookServiceConstants.CHAPTER_FILE_NAME;

@RestController
@RequestMapping
public class ChapterController {

    @Autowired
    private ChapterService chapterService;

    @GetMapping("/chapter/{chapterId}")
    public ResponseEntity<?> getChapterDetail(@PathVariable(value = "chapterId") String chapterId) {
        try {
            Chapter chapter = chapterService.getChapterDetail(chapterId);
            if (chapter != null)
                return new ResponseEntity<>(chapter, HttpStatus.OK);
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (ChapterNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/chapter/stream/{chapterId}")
    public void getChapterContent(@PathVariable(value = "chapterId") String chapterId, HttpServletResponse response) throws Exception {
        try {
            Chapter chapter = chapterService.getChapterContent(chapterId);
            if (chapter != null)
                FileCopyUtils.copy(chapter.getStream(), response.getOutputStream());
        } catch (ChapterNotFoundException | IllegalStateException  | IOException e)  {
            e.printStackTrace();
        }
    }

    @GetMapping("/chapter/stream2/{chapterId}")
    public ResponseEntity<?> getChapterContent2(@PathVariable(value = "chapterId") String chapterId, HttpServletResponse response) throws Exception {
        try {
            Chapter chapter = chapterService.getChapterContent(chapterId);
            if (chapter != null)
                return new ResponseEntity<>(chapter, HttpStatus.OK);
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        } catch (ChapterNotFoundException | IllegalStateException  | IOException e)  {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
