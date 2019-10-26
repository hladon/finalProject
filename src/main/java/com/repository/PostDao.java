package com.repository;

import com.Exceptions.InternalServerError;
import com.models.FriendshipStatus;
import com.models.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostDao extends DAO<Post> {
    public PostDao() {
        type = Post.class;
    }

    private static final String GET_POSTS = "SELECT * FROM POST WHERE USER_PAGE_POSTED=?1";
    private static final String GET_POSTS2 = "SELECT * FROM POST WHERE USER_PAGE_POSTED=?1 AND USER_POSTED=?2 ORDER BY DATE_POSTED";
    private static final String GET_FEEDS = "SELECT  * FROM POST WHERE USER_POSTED IN (\n" +
            "SELECT USER_PROFILE.ID FROM USER_PROFILE JOIN RELATIONSHIP ON  USER_PROFILE.ID=RELATIONSHIP.ID_USER_FROM\n" +
            "WHERE RELATIONSHIP.ID_USER_TO=?1 AND RELATES=?2 UNION\n" +
            "SELECT USER_PROFILE.ID FROM USER_PROFILE JOIN RELATIONSHIP ON  USER_PROFILE.ID=RELATIONSHIP.ID_USER_TO\n" +
            "WHERE RELATIONSHIP.ID_USER_FROM=?1 AND RELATES=?2) AND ROWNUM<=10 ORDER BY DATE_POSTED";

    public List<Post> getPosts(long pageId) throws InternalServerError {
        try {
            return entityManager.createNativeQuery(GET_POSTS, Post.class).
                    setParameter(1, pageId)
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public List<Post> getPosts(long pageId, long userPostedId) throws InternalServerError {
        try {
            return entityManager.createNativeQuery(GET_POSTS2, Post.class).
                    setParameter(1, pageId)
                    .setParameter(2, userPostedId)
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public List<Post> getFriendsFeeds(long id) throws InternalServerError {
        try {
            return entityManager.createNativeQuery(GET_FEEDS, Post.class)
                    .setParameter(1, id)
                    .setParameter(2, FriendshipStatus.FRIEND.name())
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }
}
