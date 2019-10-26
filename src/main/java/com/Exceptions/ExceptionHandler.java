package com.Exceptions;

import com.UserController;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandler {

    private org.apache.log4j.Logger log= org.apache.log4j.Logger.getLogger(UserController.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler({ExceedLimits.class,IllegalArgumentException.class, ConstraintViolationException.class})
    public void wrongInput(Exception e){  log.error("Function was interrupted by wrong input "+e.getStackTrace());  }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @org.springframework.web.bind.annotation.ExceptionHandler(InternalServerError.class)
    public void internalError(Exception e){
        log.error("Repository exception "+e.getStackTrace());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotAuthorized.class)
    public ModelAndView authrizationCheck(){
        ModelAndView model=new ModelAndView("login");
        return model ;
    }
}
