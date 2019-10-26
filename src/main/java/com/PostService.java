package com;

import com.Exceptions.InternalServerError;
import com.models.FriendshipStatus;
import com.models.Post;
import com.models.Relationship;
import com.models.User;
import com.repository.PostDao;
import com.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

    public Post addPost(String message, String url, User userClient) throws Exception {
        url = url.replaceAll("\\D+", "");
        long pageId = Long.parseLong(url);
        if (checkLinks(message))
            throw new IllegalArgumentException();
        Relationship rel = userService.getRelationship(userClient.getId(), pageId);
        if ((rel == null && pageId == userClient.getId()) || !rel.getRelates().equals(FriendshipStatus.FRIEND))
            return null;
        User userPage = userDao.findById(pageId);
        Post post = new Post();
        post.setUserPagePosted(userPage);
        post.setUserPosted(userClient);
        post.setDatePosted(new Date());
        post.setMessage(message);
        postDao.save(post);
        return post;
    }

    public List<Post> getPosts(String postFilter, User user, long pageId) {
        try {
            if (postFilter == null || postFilter.equals("ALL_ID"))
                return postDao.getPosts(pageId);
            if (postFilter.equals("OWNER_ID"))
                return postDao.getPosts(pageId, pageId);
            if (postFilter.equals("USER_ID"))
                return postDao.getPosts(pageId, user.getId());
            Long userPostedId = Long.parseLong(postFilter);
            return postDao.getPosts(pageId, userPostedId);
        } catch (Exception e) {
            return null;
        }

    }

    public List<Post> getFriendsFeeds(long id) throws InternalServerError {
        List<Post> posts = postDao.getFriendsFeeds(id);
        return posts;
    }

    private boolean checkLinks(String text) {
        String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        return m.matches();
    }
}
