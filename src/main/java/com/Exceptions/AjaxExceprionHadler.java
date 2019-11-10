package com.Exceptions;

import com.validators.AjaxController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = AjaxController.class)
public class AjaxExceprionHadler {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AjaxController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> allExceptions(Exception e){
        log.error("Internal server error " + e);
        return new ResponseEntity<String>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotAuthorized.class)
    public ResponseEntity<String> authorization(Exception e){
        log.error("Performing request without authorization ");
        return new ResponseEntity<String>("Authorization needed", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> inputException(Exception e){
        log.error("Wrong input ");
        return new ResponseEntity<String>("It have to be number", HttpStatus.BAD_REQUEST);
    }
}
