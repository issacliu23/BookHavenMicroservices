package com.ncs.nusiss.bookservice.book;

import com.ncs.nusiss.bookservice.book.chapter.Chapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "book")
public class BookController {
    @Autowired
    private BookRepository bookRepository;
    @PostMapping
    public void createBook(@RequestBody Book book) {
        bookRepository.save(book);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllHouseholds() {
        List<Book> books = bookRepository.findAll();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }


    // createBook
    // uploadChapters
    // updateBook
    // getAllBooks
    // getBook
    //
}
