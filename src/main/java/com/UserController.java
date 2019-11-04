package com;


import com.Exceptions.NotAuthorized;
import com.models.Password;
import com.models.Post;
import com.models.Relationship;
import com.models.User;
import com.repository.UserDao;
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


    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UserController.class);


    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String main() {
        return "index";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginPage() {
        log.info("Login page opened");
        return "login";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@ModelAttribute Password pass, HttpSession session) throws Exception {
        User user = null;
        user = userDao.isExist(pass.getPhone());
        if (user == null || !user.getPassword().equals(pass.getPassword())) {
            log.info("Log in failure");
            throw new NotAuthorized();
        }
        session.setAttribute("user", user);
        log.info("User log in!");
        return new ResponseEntity<String>(HttpStatus.ACCEPTED);

    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public String profile(HttpSession session, Model model, @PathVariable long userId) throws Exception {
        User userClient = (User) session.getAttribute("user");
        if (userClient == null)
            throw new NotAuthorized();
        User user = null;
        Relationship rel = null;
        long id = userClient.getId();
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
            log.info("User enter personal page!");
            return "personalProfile";
        }

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        log.info("User enter to page " + userId);
        return "profile";
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

    @RequestMapping(path = "/addPost", method = RequestMethod.POST)
    public ResponseEntity<String> addPost(HttpSession session, @RequestParam String message,
                                          @RequestParam String url) throws Exception {
        User userClient = (User) session.getAttribute("user");
        if (userClient == null)
            throw new NotAuthorized();
        Post post = postService.addPost(message, url, userClient);
        if (post == null)
            return new ResponseEntity<>("You have to be friends !", HttpStatus.NOT_ACCEPTABLE);
        log.info("User " + userClient.getId() + " made a post on " + post.getUserPagePosted().getId() + " page");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);


    }

    @RequestMapping(path = "/addPostId", method = RequestMethod.GET)
    public ResponseEntity<String> addPostFilter(HttpSession session, @RequestParam String postedId) {
        if (postedId == null)
            return new ResponseEntity<String>("First select user ID", HttpStatus.BAD_REQUEST);
        Long user = Long.parseLong(postedId);
        session.setAttribute("userPosted", user);
        return new ResponseEntity<String>("Reload page!", HttpStatus.ACCEPTED);


    }

    @RequestMapping(path = "/addPostFilter", method = RequestMethod.GET)
    public ResponseEntity<String> addFilter(HttpSession session, @RequestParam String filter) {
        session.setAttribute("userPosted", filter);
        return new ResponseEntity<String>("Reload page!", HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/user/feed", method = RequestMethod.GET)
    public String getFeed(HttpSession session, Model model) throws Exception {
        User userClient = (User) session.getAttribute("user");
        if (userClient == null)
            throw new NotAuthorized();
        List<Post> posts;
        posts = postService.getFriendsFeeds(userClient.getId());
        model.addAttribute("posts", posts);
        log.info("User " + userClient.getId() + " check his feeds ");
        return "feed";

    }


}
