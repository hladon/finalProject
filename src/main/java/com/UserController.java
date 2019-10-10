package com;


import com.models.Password;
import com.models.Post;
import com.models.Relationship;
import com.models.User;
import com.repository.UserDao;
import com.validators.ExceedLimits;
import com.validators.MaxFriendsCheck;
import com.validators.Validation;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class UserController extends HttpServlet {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String main() {
        return "index";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@ModelAttribute Password pass, HttpSession session) {
        User user = null;
        try {
            return userService.login(pass, session);
        } catch (Exception e) {
            return new ResponseEntity<String>("Internal error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public String profile(HttpSession session, Model model, @PathVariable long userId) {
        User userClient = (User) session.getAttribute("user");
        if (userClient == null)
            return "login";
        User user = null;
        Relationship rel = null;
        long id = userClient.getId();
        try {
            user = userDao.findById(userId);
            model.addAttribute("user", user);
            List<Post> posts = postService.getPosts(session, userId);
            model.addAttribute("posts", posts);
            if (id != userId) {
                rel = userService.getRelationship(id, userId);
                model.addAttribute("relationship", rel);
                session.setAttribute("lastVisited", userId);
            } else {
                List<Relationship> outRequests = userService.getOutcomeRequests(userId);
                List<Relationship> inRequests = userService.getIncomeRequests(userId);
                model.addAttribute("outRequests", outRequests);
                model.addAttribute("inRequests", inRequests);
                return "personalProfile";
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "profile";
    }

    @RequestMapping(path = "/user-registration", method = RequestMethod.POST)
    public ResponseEntity<String> registerUser(@ModelAttribute User user) {
        try {
            return userService.registerUser(user);
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/addRelationship", method = RequestMethod.GET)
    public ResponseEntity<String> addRelationship(HttpSession session) {
        try {
            User userClient = (User) session.getAttribute("user");
            if (userClient == null)
                new ResponseEntity<String>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
            long userIdTo = (Long) session.getAttribute("lastVisited");
            long userIdFrom = userClient.getId();
            return userService.addRelationship(userIdFrom, userIdTo);
        } catch (ConstraintViolationException | NumberFormatException cve) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ExceedLimits e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/updateRelationship", method = RequestMethod.GET)
    public ResponseEntity<String> updateRelationship(HttpSession session, @RequestParam String userIdTo, @RequestParam String status) {
        try {
            User userClient = (User) session.getAttribute("user");
            if (userClient == null)
                new ResponseEntity<String>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
            long userIdFrom = userClient.getId();
            return userService.updateRelationship(userIdFrom, userIdTo, status);
        } catch (ConstraintViolationException | IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (ExceedLimits e) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/addPost", method = RequestMethod.POST)
    public ResponseEntity<String> addPost(HttpSession session, @RequestParam String message, @RequestParam String url) {
        try {
            User userClient = (User) session.getAttribute("user");
            if (userClient == null)
                new ResponseEntity<String>(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
            return postService.addPost(message, url, userClient);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(path = "/addPostId", method = RequestMethod.GET)
    public ResponseEntity<String> addPostFilter(HttpSession session, @RequestParam String postedId) {
        if (postedId == null)
            return new ResponseEntity<String>("First select user ID", HttpStatus.BAD_REQUEST);
        try {
            return postService.addPostId(session, postedId);
        } catch (Exception e) {
            return new ResponseEntity<>("Enter Id", HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(path = "/addPostFilter", method = RequestMethod.GET)
    public ResponseEntity<String> addFilter(HttpSession session, @RequestParam String filter) {
        return postService.addPostFilter(session, filter);
    }


}
