package com.restController;

import com.models.Message;
import com.models.User;
import com.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;


@RestController
public class MessageRestController extends HttpServlet {

    @Autowired
    private MessageService messageService;

    @RequestMapping(path = "/message", method = RequestMethod.POST)
    public ResponseEntity<String> sendMessage(HttpSession session,
                                              @ModelAttribute Message message,
                                              @RequestParam long url) throws Exception {
        User user = (User) session.getAttribute("user");
        messageService.sendMessage(message, user, url);
        return new ResponseEntity<String>("Message send", HttpStatus.CREATED);
    }

    @RequestMapping(path = "/message", method = RequestMethod.DELETE)
    public void deleteMessage(HttpSession session, @RequestParam long id) throws Exception {
        User user = (User) session.getAttribute("user");
        messageService.deleteMessage(id, user.getId());
    }

    @RequestMapping(path = "/message", method = RequestMethod.PUT)
    public ResponseEntity<String> editeMessage(HttpSession session,
                                               @RequestParam String text,
                                               @RequestParam long messageId) throws Exception {
        User user = (User) session.getAttribute("user");
        messageService.editeMessage(text, messageId, user.getId());
        return new ResponseEntity<String>("Message send", HttpStatus.CREATED);
    }


}
