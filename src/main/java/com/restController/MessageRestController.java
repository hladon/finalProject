package com.restController;

import com.Exceptions.ExceedLimits;
import com.models.Message;
import com.repository.MessageDAO;
import com.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import java.util.Date;

@RestController
public class MessageRestController extends HttpServlet {

    @Autowired
    private MessageDAO messageDAO;
    @Autowired
    private UserDao userDao;

    public ResponseEntity<Message> sendMessage(String text, Long userFrom, Long userTo) throws Exception{
        Message message=new Message();
        if (text.length()>140)
            throw new ExceedLimits();
        Date date=new Date();
        message.setText(text);
        message.setDateEdited(date);
        message.setDateSent(date);
        message.setUserFrom(userDao.findById(userFrom));
        message.setUserTo(userDao.findById(userTo));
        messageDAO.save(message);
        return new ResponseEntity<Message>(message, HttpStatus.CREATED);
    }

    public void deleteMessage(Long id){

    }

    public Message editeMessage(String text){
        Message message=new Message();


        return message;
    }
}
