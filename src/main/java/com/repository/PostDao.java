package com.repository;

import com.models.Post;
import org.springframework.stereotype.Repository;

@Repository
public class PostDao extends DAO<Post> {
    public PostDao() {
        type=Post.class;
    }


}
