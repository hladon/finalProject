package com.repository;

import com.models.Post;

public class PostDao extends DAO<Post> {
    public PostDao() {
        type=Post.class;
    }
}
