package com.ncs.nusiss.bookservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ChapterAccessExistsException extends Exception {
    public ChapterAccessExistsException() {
        this("Chapter Access exists for this chapter id and user id");
    }
    public ChapterAccessExistsException(String message) {
        super(message);
    }

}