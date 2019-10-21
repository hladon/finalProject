package com;


import com.models.Password;
import com.models.Post;
import com.models.Relationship;
import com.models.User;
import com.repository.UserDao;
import com.validators.ExceedLimits;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.logging.LogManager;


@Controller
public class UserController extends HttpServlet {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    private static final Logger logger= LogManager.getLogger(UserController.class);

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String main() {
        return "index";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginPage() {
        logger.info("Main page opened!");
        logger.error("error");
        return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@ModelAttribute Password pass, HttpSession session) {
        User user = null;
        try {
            user = userDao.isExist(pass.getPhone());
            if (user == null || !user.getPassword().equals(pass.getPassword())) {
                return new ResponseEntity<String>("Wrong phone or password!", HttpStatus.BAD_REQUEST);
            }
            session.setAttribute("user", user);
            return new ResponseEntity<String>(HttpStatus.ACCEPTED);
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
            String postFilter = (String) session.getAttribute("userPosted");
            List<Post> posts = postService.getPosts(postFilter, user, userId);
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
            if (userService.registerUser(user) == null)
                return new ResponseEntity<String>("Such user exist!", HttpStatus.CONFLICT);
            return new ResponseEntity<String>(HttpStatus.CREATED);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Wrong phone number!", HttpStatus.BAD_REQUEST);
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
            userService.addRelationship(userIdFrom, userIdTo);
            return new ResponseEntity<String>(HttpStatus.CREATED);
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
            userService.updateRelationship(userIdFrom, userIdTo, status);
            return new ResponseEntity<String>(HttpStatus.CREATED);
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
            if (postService.addPost(message, url, userClient) == null)
                return new ResponseEntity<>("You have to be friends !", HttpStatus.NOT_ACCEPTABLE);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Links not allowed here!", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(path = "/addPostId", method = RequestMethod.GET)
    public ResponseEntity<String> addPostFilter(HttpSession session, @RequestParam String postedId) {
        if (postedId == null)
            return new ResponseEntity<String>("First select user ID", HttpStatus.BAD_REQUEST);
        try {
            Long user = Long.parseLong(postedId);
            session.setAttribute("userPosted", user);
            return new ResponseEntity<String>("Reload page!", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>("Enter Id", HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(path = "/addPostFilter", method = RequestMethod.GET)
    public ResponseEntity<String> addFilter(HttpSession session, @RequestParam String filter) {
        session.setAttribute("userPosted", filter);
        return new ResponseEntity<String>("Reload page!", HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/user/feed", method = RequestMethod.GET)
    public String getFeed(HttpSession session, Model model) {
        User userClient = (User) session.getAttribute("user");
        if (userClient == null)
            return "login";
        List<Post> posts;
        try {
            posts = postService.getFriendsFeeds(userClient.getId());
            model.addAttribute("posts", posts);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return "feed";

    }


}
