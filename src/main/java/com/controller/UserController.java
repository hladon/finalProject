package com.controller;

import com.Exceptions.NotAuthorized;
import com.models.Post;
import com.models.Relationship;
import com.models.User;
import com.repository.UserDao;
import com.service.PostService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
}
