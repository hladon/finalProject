package com.controller;

import com.models.User;
import com.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

@Controller
public class MessageController extends HttpServlet {

    @Autowired
    private MessageService messageService;

    @RequestMapping(path = "/dialogs", method = RequestMethod.GET)
    public String dialogs(Model model, HttpSession session) throws Exception{
        User user=(User)session.getAttribute("user");
        model.addAttribute("users",messageService.getUserWithDialogs(user));
        return "dialogs";
    }
}
