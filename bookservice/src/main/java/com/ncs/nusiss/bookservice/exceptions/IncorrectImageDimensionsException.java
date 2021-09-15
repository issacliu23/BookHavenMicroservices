package com.ncs.nusiss.bookservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IncorrectImageDimensionsException extends Exception {
    public IncorrectImageDimensionsException(int actualHeight, int actualWidth, int permittedHeight, int permittedWidth) {
        super("Incorrect image size:" +actualHeight+"x"+actualWidth+",accepted size:"+ permittedHeight+"x"+permittedWidth);
    }

}
