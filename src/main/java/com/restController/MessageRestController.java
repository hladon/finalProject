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
import java.util.List;


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
    public ResponseEntity<String> deleteMessage(HttpSession session, @RequestParam List<Long> ids) throws Exception {
        User user = (User) session.getAttribute("user");
        messageService.deleteMessage( ids);
        return new ResponseEntity<String>( HttpStatus.ACCEPTED);
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
