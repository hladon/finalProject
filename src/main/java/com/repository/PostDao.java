package com.repository;

import com.Exceptions.RepositoryException;
import com.models.FriendshipStatus;
import com.models.Post;
import com.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostDao extends DAO<Post> {
    public PostDao() {
        type = Post.class;
    }

    public List<Post> getPosts(long pageId) throws RepositoryException {
        try {
            return entityManager.createNativeQuery("SELECT * FROM POST WHERE USER_PAGE_POSTED=?1", Post.class).
                    setParameter(1, pageId)
                    .getResultList();
        }catch (Exception e){
            throw new  RepositoryException(e.getMessage());
        }
    }

    public List<Post> getPosts(long pageId, long userPostedId) throws RepositoryException {
        try{
        return entityManager.createNativeQuery("SELECT * FROM POST WHERE USER_PAGE_POSTED=?1 AND USER_POSTED=?2" +
                " ORDER BY DATE_POSTED", Post.class).
                setParameter(1, pageId)
                .setParameter(2, userPostedId)
                .getResultList();
        }catch (Exception e){
            throw new  RepositoryException(e.getMessage());
        }
    }

    public List<Post> getFriendsFeeds(long id) throws RepositoryException {
        try {
        return entityManager.createNativeQuery("" +
                "SELECT  * FROM POST WHERE USER_POSTED IN (\n" +
                "SELECT USER_PROFILE.ID FROM USER_PROFILE JOIN RELATIONSHIP ON  USER_PROFILE.ID=RELATIONSHIP.ID_USER_FROM\n" +
                "WHERE RELATIONSHIP.ID_USER_TO=?1 AND RELATES=?2 UNION\n" +
                "SELECT USER_PROFILE.ID FROM USER_PROFILE JOIN RELATIONSHIP ON  USER_PROFILE.ID=RELATIONSHIP.ID_USER_TO\n" +
                "WHERE RELATIONSHIP.ID_USER_FROM=?1 AND RELATES=?2) AND ROWNUM<=10 ORDER BY DATE_POSTED", Post.class)
                .setParameter(1, id)
                .setParameter(2, FriendshipStatus.FRIEND.name())
                .getResultList();
        }catch (Exception e){
            throw new  RepositoryException(e.getMessage());
        }
    }
}
