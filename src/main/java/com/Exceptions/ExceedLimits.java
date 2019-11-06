package com.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExceedLimits extends Exception {
    public ExceedLimits() {
        super("You exceed the limits!");
    }
}
