package com.Exceptions;

import com.UserController;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class HandlerForExceptios {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UserController.class);

    @ExceptionHandler({NumberFormatException.class,ExceedLimits.class})
    public ResponseEntity wrongInputFormat(Exception e) {
        log.error("Wrong data format was input ");
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({IllegalArgumentException.class, ConstraintViolationException.class})
    public ModelAndView wrongInput(Exception e) {
        log.error("Function was interrupted by wrong input " , e);
        ModelAndView model=new ModelAndView("error");
        model.addObject("Exception",e);
        return model;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerError.class)
    public void internalError(Exception e) {
        log.error("Repository exception ",e);
    }

    @ExceptionHandler(NotAuthorized.class)
    public ModelAndView authrizationCheck() {
        ModelAndView model = new ModelAndView("login");
        return model;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void generalException(Exception e) {
        log.error("Function was interrupted by error ",e);
    }
}
