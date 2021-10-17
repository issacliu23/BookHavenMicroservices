package com.ncs.nusiss.bookservice.book;

import com.ncs.nusiss.bookservice.book.chapter.Chapter;
import com.ncs.nusiss.bookservice.exceptions.BookNotFoundException;
import com.ncs.nusiss.bookservice.exceptions.IncorrectImageDimensionsException;
import com.ncs.nusiss.bookservice.exceptions.IncorrectFileExtensionException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.io.IOException;
import java.util.List;

import static com.ncs.nusiss.bookservice.BookServiceConstants.CHAPTER_FILE_NAME;
import static com.ncs.nusiss.bookservice.BookServiceConstants.COVER_IMAGE_FILE_NAME;

@RestController
@RequestMapping(path = "book")
public class BookController {
    private final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<?> publishBook(@Valid @ModelAttribute Book book, @RequestParam(COVER_IMAGE_FILE_NAME) MultipartFile coverImage) {
        try {
            Book createdBook = bookService.createBook(book, coverImage);
            if (createdBook != null)
                return new ResponseEntity<>(createdBook, HttpStatus.OK);
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (SizeLimitExceededException | IncorrectFileExtensionException | IncorrectImageDimensionsException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Unable to use PutMapping due to multipart file
    @PostMapping("/{bookId}")
    public ResponseEntity<?> updateBook(@PathVariable String bookId, @Valid @ModelAttribute Book newBook, @RequestParam(COVER_IMAGE_FILE_NAME) MultipartFile coverImage) {
        try {
            if(bookService.updateBook(bookId, newBook, coverImage))
                return ResponseEntity.status(HttpStatus.OK).build();
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (BookNotFoundException | IncorrectFileExtensionException | IncorrectImageDimensionsException | SizeLimitExceededException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{bookId}/chapter")
    public ResponseEntity<?> uploadChapter(@PathVariable(value = "bookId") String bookId, @Valid @ModelAttribute Chapter chapter, @RequestParam(CHAPTER_FILE_NAME) MultipartFile chapterFile) {
        try {
            Chapter addedChapter = bookService.addChapter(bookId, chapter, chapterFile);
            if (addedChapter != null)
                return new ResponseEntity<>(addedChapter, HttpStatus.OK);
            else
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        catch (SizeLimitExceededException | IncorrectFileExtensionException | BookNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> findAllBooks(@RequestBody(required = false) BookCriteria criteria) {
        List<BookDTO> bookList = bookService.getBooks(criteria);
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }


    // createBook
    // uploadChapters
    // updateBook
    // getAllBooks
    // getBook
    //
}
