package com.Exceptions;

import com.restController.MessageRestController;
import com.restController.PostRestController;
import com.restController.UserRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {MessageRestController.class, PostRestController.class, UserRestController.class})
public class RestExceptionHandler {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(InternalError.class)
    public ResponseEntity<String> internalException(Exception e){
        log.error("Database error:  " + e);
        return new ResponseEntity<String>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> allExceptions(Exception e) {
        log.error("Internal server error " + e);
        return new ResponseEntity<String>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotAuthorized.class)
    public ResponseEntity<String> authorization(Exception e) {
        log.error("Performing request without authorization ");
        return new ResponseEntity<String>("Authorization needed", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> inputException(Exception e) {
        log.error("Wrong input ");
        return new ResponseEntity<String>("It have to be a number", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExceedLimits.class)
    public ResponseEntity<String> exceedLimits(Exception e){
        log.error("User exceed limits");
        return new ResponseEntity<String>("Such action forbiden because of limits",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFriend.class)
    public ResponseEntity<String> notFriends(Exception e){
        return new ResponseEntity<String>("You have to be friends first!",HttpStatus.BAD_REQUEST);
    }
}
