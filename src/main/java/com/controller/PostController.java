package com.controller;

import com.Exceptions.NotAuthorized;
import com.models.Post;
import com.models.User;
import com.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PostController extends HttpServlet {


    @Autowired
    private PostService postService;

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PostController.class);

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
