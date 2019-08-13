package com.finPr.repository;

import com.finPr.models.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends DAO<User> {
    public UserDao() {
        type=User.class;
    }
}
