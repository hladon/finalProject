package com.finPr.repository;

import com.finPr.models.Post;

public class PostDao extends DAO<Post> {
    public PostDao() {
        type=Post.class;
    }
}
