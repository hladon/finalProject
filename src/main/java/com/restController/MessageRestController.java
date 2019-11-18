package com.restController;

import com.models.Message;
import com.models.User;
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


@RestController
public class MessageRestController extends HttpServlet {

    @Autowired
    private MessageService messageService;

    @RequestMapping(path = "/message", method = RequestMethod.POST)
    public ResponseEntity<String> sendMessage(HttpSession session,
                                              @RequestParam String text,
                                              @RequestParam String url) throws Exception {
        User user = (User) session.getAttribute("user");
        long idTo = Long.parseLong(url);
        Message message = messageService.sendMessage(text, user.getId(), idTo);
        return new ResponseEntity<String>("Message send", HttpStatus.CREATED);
    }

    @RequestMapping(path = "/message", method = RequestMethod.DELETE)
    public void deleteMessage(HttpSession session, @RequestParam String id) throws Exception {
        Long messageId = Long.parseLong(id);
        User user = (User) session.getAttribute("user");
        messageService.deleteMessage(messageId, user.getId());
    }

    @RequestMapping(path = "/message", method = RequestMethod.PUT)
    public ResponseEntity<String> editeMessage(HttpSession session,
                                               @RequestParam String text,
                                               @RequestParam String url) throws Exception {
        Long messageId = Long.parseLong(url);
        User user = (User) session.getAttribute("user");
        messageService.editeMessage(text, messageId, user.getId());
        return new ResponseEntity<String>("Message send", HttpStatus.CREATED);
    }


}
