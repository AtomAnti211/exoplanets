package com.benyei.exoplanets.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ConstraintException extends RuntimeException{

    public ConstraintException(String message) {
        super(message);
    }
}
