package com.restController;

import com.models.Post;
import com.models.User;
import com.service.PostService;
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
public class PostRestController extends HttpServlet {

    @Autowired
    private PostService postService;

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PostRestController.class);

    @RequestMapping(path = "/addPost", method = RequestMethod.POST)
    public ResponseEntity<String> addPost(HttpSession session, @RequestParam String message,
                                          @RequestParam String url) throws Exception {
        User userClient = (User) session.getAttribute("user");
        if (userClient == null)
            return new ResponseEntity<>("You have to be friends !", HttpStatus.NOT_ACCEPTABLE);
        Post post = postService.addPost(message, url, userClient);
        if (post == null)
            return new ResponseEntity<>("Post warn`t created", HttpStatus.NOT_ACCEPTABLE);
        log.info("User " + userClient.getId() + " made a post on " + post.getUserPagePosted().getId() + " page");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

    @RequestMapping(path = "/addPostId", method = RequestMethod.GET)
    public ResponseEntity<String> addPostFilter(HttpSession session, @RequestParam String postedId) {
        if (postedId == null)
            return new ResponseEntity<String>("First select user ID", HttpStatus.BAD_REQUEST);
        Long user = Long.parseLong(postedId);
        session.setAttribute("filterId", user);
        return new ResponseEntity<String>("Reload page!", HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/addPostFilter", method = RequestMethod.GET)
    public ResponseEntity<String> addFilter(HttpSession session, @RequestParam String filter) {
        session.setAttribute("filterId", filter);
        return new ResponseEntity<String>("Reload page!", HttpStatus.ACCEPTED);
    }
}
