package com.repository;

import com.models.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends DAO<User> {
    public UserDao() {
        type=User.class;
    }
}
