package com.ncs.nusiss.bookservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IncorrectFileExtensionException extends Exception {
    public IncorrectFileExtensionException(String errorMessage) {
        super(errorMessage);
    }

}
