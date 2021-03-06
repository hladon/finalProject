package com.Exceptions;

import com.controller.PostController;
import com.controller.UserController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(assignableTypes ={PostController.class, UserController.class} )
public class ViewsExceptionHandler {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ViewsExceptionHandler.class);

    @ExceptionHandler({Exception.class})
    public ModelAndView wrongInput(Exception e) {
        log.error("Function was interrupted by wrong input ", e);
        ModelAndView model = new ModelAndView("error500");
        model.addObject("Exception", e);
        return model;
    }

    @ExceptionHandler(NotAuthorized.class)
    public ModelAndView authrizationCheck() {
        ModelAndView model = new ModelAndView("login");
        return model;
    }

}
