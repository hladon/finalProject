package com.service;

import com.models.User;
import com.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Component
public class AuthintficationSuccesImpl implements AuthenticationSuccessHandler {


    @Autowired
    UserDao repository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String username;
        try {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails
        ) {
             username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
            User user=repository.isExist(username);
            System.out.println(user.toString());
            HttpSession session=httpServletRequest.getSession();
            if (user!=null)
                session.setAttribute("user",user);

        }catch (Exception e){
            throw new IOException(e.getMessage());
        }

    }

}
