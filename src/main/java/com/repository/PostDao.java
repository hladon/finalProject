package com.repository;

import com.models.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostDao extends DAO<Post> {
    public PostDao() {
        type=Post.class;
    }

    public List<Post> getPosts (long pageId){
        return entityManager.createNativeQuery("SELECT * FROM POST WHERE USER_PAGE_POSTED=?1",Post.class).
                setParameter(1,pageId)
                .getResultList();
    }
    public List<Post> getPosts (long pageId,long userPostedId){
        return entityManager.createNativeQuery("SELECT * FROM POST WHERE USER_PAGE_POSTED=?1 AND USER_POSTED=?2",Post.class).
                setParameter(1,pageId)
                .setParameter(2,userPostedId)
                .getResultList();
    }
}
