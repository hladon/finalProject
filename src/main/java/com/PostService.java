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

    public ResponseEntity<String> addPost (String message, String url, User userClient){
        try {
            url = url.replaceAll("\\D+","");
            long pageId=Long.parseLong(url);
            if(checkLinks(message))
                return new ResponseEntity<>("Links not allowed here!",HttpStatus.BAD_REQUEST);
            Relationship rel=userService.getRelationship(userClient.getId(),pageId);
            if ((rel==null&&pageId==userClient.getId())||!rel.getRelates().equals(FriendshipStatus.FRIEND))
                return new ResponseEntity<>("You have to be friends !",HttpStatus.NOT_ACCEPTABLE);
            User userPage=userDao.findById(pageId);
            Post post=new Post();
            post.setUserPagePosted(userPage);
            post.setUserPosted(userClient);
            post.setDatePosted(new Date());
            post.setMessage(message);
            postDao.save(post);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    public List<Post> getPosts(HttpSession session,long pageId){
        Long userPostedId=(Long)session.getAttribute("userPosted");
        if (userPostedId==null)
            return postDao.getPosts(pageId);
        return postDao.getPosts(pageId,userPostedId);
    }

    private boolean checkLinks(String text) {
        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        return m.matches();
    }
}
