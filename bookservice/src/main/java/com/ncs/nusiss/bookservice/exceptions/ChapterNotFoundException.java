package com.ncs.nusiss.bookservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ChapterNotFoundException extends Exception {
    public ChapterNotFoundException() {
        this("Chapter not found");
    }
    public ChapterNotFoundException(String message) {
        super(message);
    }

}