package com;

import com.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PostService {

    public ResponseEntity<String> addPost (String post, String url, User userClient){
        long pageId=Long.parseLong(url.substring(1));
        if(checkLinks(post))
            return new ResponseEntity<>("Links not allowed here!",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    private boolean checkLinks(String text) {
        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        return m.matches();
    }
}
