package com.ncs.nusiss.bookservice.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "book")
public class BookController {
    @Autowired
    private BookRepository bookRepository;
    @PostMapping
    public void createBook(@RequestBody Book book) {
        bookRepository.save(book);
    }
    // createBook
    // uploadChapters
    // updateBook
    // getAllBooks
    // getBook
    //
}
