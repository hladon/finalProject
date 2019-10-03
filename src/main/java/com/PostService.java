package com;

import com.models.Post;
import com.models.User;
import com.repository.PostDao;
import com.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PostService {

    @Autowired
    private PostDao postDao;

    private UserDao userDao;

    public ResponseEntity<String> addPost (String message, String url, User userClient){
        try {
//        long pageId=Long.parseLong(url.substring(1));
            System.out.println(url);
            if(checkLinks(message))
                return new ResponseEntity<>("Links not allowed here!",HttpStatus.BAD_REQUEST);
//            User userPage=userDao.findById(pageId);
//            Post post=new Post();
//            post.setDatePosted(new Date());
//            post.setMessage(message);
//            postDao.save(post);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    public List<Post> getPosts(String userIdPage){


    }

    private boolean checkLinks(String text) {
        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        return m.matches();
    }
}
