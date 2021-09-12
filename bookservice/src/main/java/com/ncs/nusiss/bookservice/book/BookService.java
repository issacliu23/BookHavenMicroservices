package com.ncs.nusiss.bookservice.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    public Book createBook(Book book) {
        return bookRepository.insert(book);
    }
}
