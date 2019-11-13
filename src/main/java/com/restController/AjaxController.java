package com.restController;

import com.Exceptions.NotAuthorized;
import com.service.PostService;
import com.service.UserService;
import com.models.Password;
import com.models.Post;
import com.models.User;
import com.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

@RestController
public class AjaxController extends HttpServlet {


    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AjaxController.class);

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@ModelAttribute Password pass, HttpSession session) throws Exception {
        User user = null;
        user = userDao.isExist(pass.getPhone());
        if (user == null || !user.getPassword().equals(pass.getPassword())) {
            log.info("Log in failure");
            return new ResponseEntity<String>("Wrong phone or password", HttpStatus.NOT_FOUND);
        }
        session.setAttribute("user", user);
        log.info("User log in!");
        return new ResponseEntity<String>(HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/user-registration", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@ModelAttribute User user) throws Exception {
        if (userService.registerUser(user) == null)
            return new ResponseEntity<String>("Such user exist!", HttpStatus.CONFLICT);
        log.info("User registered in system ");
        return new ResponseEntity<String>(HttpStatus.CREATED);

    }

    @RequestMapping(path = "/addRelationship", method = RequestMethod.GET)
    public ResponseEntity<String> addRelationship(HttpSession session) throws Exception {
        User userClient = (User) session.getAttribute("user");
        if (userClient == null)
            throw new NotAuthorized();
        long userIdTo = (Long) session.getAttribute("lastVisited");
        long userIdFrom = userClient.getId();
        userService.addRelationship(userIdFrom, userIdTo);
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/updateRelationship", method = RequestMethod.GET)
    public ResponseEntity<String> updateRelationship(HttpSession session,
                                                     @RequestParam String userIdTo,
                                                     @RequestParam String status) throws Exception {

        User userClient = (User) session.getAttribute("user");
        if (userClient == null)
            throw new NotAuthorized();
        long userIdFrom = userClient.getId();
        userService.updateRelationship(userIdFrom, userIdTo, status);
        return new ResponseEntity<String>(HttpStatus.CREATED);
    }


}
