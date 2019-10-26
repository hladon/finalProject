package com.repository;

import com.Exceptions.InternalServerError;
import com.models.FriendshipStatus;
import com.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao extends DAO<User> {
    public UserDao() {
        type = User.class;
    }

    private static final String SELECT_PHONE = "SELECT * FROM USER_PROFILE WHERE PHONE=?1";
    private static final String GET_FRIENDS = "SELECT USER_PROFILE.ID FROM USER_PROFILE JOIN RELATIONSHIP ON  USER_PROFILE.ID=RELATIONSHIP.ID_USER_FROM\n" +
            "WHERE RELATIONSHIP.ID_USER_TO=?1 AND RELATES=?2 UNION\n" +
            "SELECT USER_PROFILE.ID FROM USER_PROFILE JOIN RELATIONSHIP ON  USER_PROFILE.ID=RELATIONSHIP.ID_USER_TO\n" +
            "WHERE RELATIONSHIP.ID_USER_FROM=?1 AND RELATES=?2";

    public User isExist(String phone) throws InternalServerError {
        try {
            List<User> users = entityManager.createNativeQuery(SELECT_PHONE, User.class)
                    .setParameter(1, phone)
                    .getResultList();
            if (!users.isEmpty()) {
                return users.get(0);
            }
            return null;
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }

    public List<User> getFriends(long id) throws InternalServerError {
        try {
            return entityManager.createNativeQuery(GET_FRIENDS, User.class)
                    .setParameter(1, id)
                    .setParameter(2, FriendshipStatus.FRIEND.name())
                    .getResultList();
        } catch (Exception e) {
            throw new InternalServerError(e.getMessage());
        }
    }
}
