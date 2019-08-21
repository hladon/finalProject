package com.repository;

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
}
