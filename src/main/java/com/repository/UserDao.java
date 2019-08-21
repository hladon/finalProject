package com.repository;

import com.models.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends DAO<User> {
    public UserDao() {
        type=User.class;
    }

    public User isExist(String phone){
        return entityManager.createNativeQuery("SELECT * FROM USER_PROFILE WHERE PHONE=?1",type)
                .setParameter(1,phone)
                .getSingleResult();
    }
}
