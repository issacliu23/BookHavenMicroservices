package com.ncs.nusiss.bookservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ChapterAccessNotFoundException extends Exception {
    public ChapterAccessNotFoundException() {
        this("Chapter Access not found for this chapter id and user id");
    }
    public ChapterAccessNotFoundException(String message) {
        super(message);
    }

}