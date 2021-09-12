package com.ncs.nusiss.bookservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BookNotFoundException extends Exception {
    public BookNotFoundException() {
        this("Book not found");
    }
    public BookNotFoundException(String message) {
        super(message);
    }

}