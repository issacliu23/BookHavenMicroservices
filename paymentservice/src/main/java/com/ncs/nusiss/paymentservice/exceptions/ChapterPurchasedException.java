package com.ncs.nusiss.paymentservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ChapterPurchasedException extends Exception {
    public ChapterPurchasedException() {
        this("user has purchased the chapter");
    }
    public ChapterPurchasedException(String message) {
        super(message);
    }

}