package com.repository;

import com.models.FriendshipStatus;
import com.models.Relationship;
import com.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao extends DAO<User> {
    public UserDao() {
        type=User.class;
    }

    public User isExist(String phone){
        List<User> users =entityManager.createNativeQuery("SELECT * FROM USER_PROFILE WHERE PHONE=?1",User.class)
                .setParameter(1,phone)
                .getResultList();
        if (!users.isEmpty()){
            return users.get(0);
        }
        return null;
    }

    public List<User> getFriends(long id){
        return entityManager.createNativeQuery("" +
                "SELECT * FROM USER_PROFILE JOIN RELATIONSHIP ON  USER_PROFILE.ID=RELATIONSHIP.ID_USER_FROM" +
                " WHERE  USER_PROFILE.ID=?1  AND RELATES=?2 UNION" +
                "SELECT * FROM USER_PROFILE JOIN RELATIONSHIP ON  USER_PROFILE.ID=RELATIONSHIP.ID_USER_TO" +
                "WHERE  USER_PROFILE.ID=?1  AND RELATES=?2 ",User.class)
                .setParameter(1,id)
                .setParameter(2, FriendshipStatus.FRIEND.name())
                .getResultList();
    }
}
