package com.restController;

import com.Exceptions.ExceedLimits;
import com.models.Message;
import com.models.User;
import com.repository.MessageDAO;
import com.repository.UserDao;
import com.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
public class MessageRestController extends HttpServlet {

    @Autowired
    private MessageService messageService;

    @RequestMapping(path = "/message",method = RequestMethod.POST)
    public ResponseEntity<Message> sendMessage(HttpSession session,
                                               @RequestParam String text,
                                               @RequestParam String userTo) throws Exception{
        User user=(User)session.getAttribute("user");
        Long idTo=Long.parseLong(userTo);
        Message message=messageService.sendMessage(text,user.getId(),idTo);
        return new ResponseEntity<Message>(message, HttpStatus.CREATED);
    }
    @RequestMapping(path = "/message",method = RequestMethod.DELETE)
    public void deleteMessage(String id)throws Exception{
        Long messageId=Long.parseLong(id);
        messageService.deleteMessage(messageId);
    }
    @RequestMapping(path = "/message",method = RequestMethod.PUT)
    public Message editeMessage(String text,String id) throws Exception{
        Long messageId=Long.parseLong(id);
        return messageService.editeMessage(text,messageId);
    }
}
