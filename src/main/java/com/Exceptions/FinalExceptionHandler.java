package com.Exceptions;

import com.UserController;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class FinalExceptionHandler {

    private org.apache.log4j.Logger log= org.apache.log4j.Logger.getLogger(UserController.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void generalException(Exception e){
        log.error("Function was interrupted by error "+e.getStackTrace());
    }
}
