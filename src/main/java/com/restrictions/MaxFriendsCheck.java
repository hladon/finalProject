package com.restrictions;

import com.models.User;
import com.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MaxFriendsCheck extends Validation {

    @Autowired
    private UserDao userDao;
    @Override
    public boolean check(User user) {
        if(userDao.getFriends(user.getId()).size()>100){
            return false;
        }
        return checkNext(user);
    }
}
