package com.validators;

import com.models.User;
import com.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;



public class MaxFriendsCheck extends Validation {

    @Autowired
    private UserDao userDao;
    @Autowired
    public MaxFriendsCheck() {
    }

    @Override
    public boolean check(User user) {
        if(userDao.getFriends(user.getId()).size()>100){
            return false;
        }
        return checkNext(user);
    }
}
