package com.Exceptions;

import com.UserController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class HandlerForExceptios {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UserController.class);

    @ExceptionHandler({Exception.class})
    public ModelAndView wrongInput(Exception e) {
        log.error("Function was interrupted by wrong input ", e);
        ModelAndView model = new ModelAndView("error");
        model.addObject("Exception", e);
        return model;
    }

    @ExceptionHandler(NotAuthorized.class)
    public ModelAndView authrizationCheck() {
        ModelAndView model = new ModelAndView("login");
        return model;
    }

}
