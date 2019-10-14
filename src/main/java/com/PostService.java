package com;

import com.models.FriendshipStatus;
import com.models.Post;
import com.models.Relationship;
import com.models.User;
import com.repository.PostDao;
import com.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PostService {

    @Autowired
    private PostDao postDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    public ResponseEntity<String> addPost(String message, String url, User userClient) {

        url = url.replaceAll("\\D+", "");
        long pageId = Long.parseLong(url);
        if (checkLinks(message))
            return new ResponseEntity<>("Links not allowed here!", HttpStatus.BAD_REQUEST);
        Relationship rel = userService.getRelationship(userClient.getId(), pageId);
        if ((rel == null && pageId == userClient.getId()) || !rel.getRelates().equals(FriendshipStatus.FRIEND))
            return new ResponseEntity<>("You have to be friends !", HttpStatus.NOT_ACCEPTABLE);
        User userPage = userDao.findById(pageId);
        Post post = new Post();
        post.setUserPagePosted(userPage);
        post.setUserPosted(userClient);
        post.setDatePosted(new Date());
        post.setMessage(message);
        postDao.save(post);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    public List<Post> getPosts(HttpSession session, long pageId) {
        try {
            String postFilter = (String) session.getAttribute("userPosted");
            if (postFilter == null || postFilter.equals("ALL_ID"))
                return postDao.getPosts(pageId);
            if (postFilter.equals("OWNER_ID"))
                return postDao.getPosts(pageId, pageId);
            if (postFilter.equals("USER_ID")) {
                User user = (User) session.getAttribute("user");
                return postDao.getPosts(pageId, user.getId());
            }
            Long userPostedId = Long.parseLong(postFilter);
            return postDao.getPosts(pageId, userPostedId);
        } catch (Exception e) {
            return null;
        }

    }

    public ResponseEntity<String> addPostId(HttpSession session, String userId) {
        Long user = Long.parseLong(userId);
        session.setAttribute("userPosted", user);
        return new ResponseEntity<String>("Reload page!", HttpStatus.ACCEPTED);
    }

    public ResponseEntity<String> addPostFilter(HttpSession session, String filter) {
        session.setAttribute("userPosted", filter);
        return new ResponseEntity<String>("Reload page!", HttpStatus.ACCEPTED);
    }

    public List<Post> getFriendsFeeds(long id){
        List<Post> posts=postDao.getFriendsFeeds(id);
        return posts;
    }

    private boolean checkLinks(String text) {
        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        return m.matches();
    }
}
