package com.ncs.nusiss.bookservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IncorrectFileExtensionException extends Exception {
    public IncorrectFileExtensionException(String incorrectExtension, List<String> requiredExtension) {
        super("Incorrect file extension: " +incorrectExtension+", Accepted extensions: "+requiredExtension);
    }

}
