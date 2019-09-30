package com.restrictions;

import com.UserService;
import com.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MaxRequests extends Validation {

    @Autowired
    private UserService userService;

    @Override
    public boolean check(User user) {
        int size=userService.getOutcomeRequests(user.getId()).size();
        if (size>10)
        return false;

        return checkNext(user);
    }
}
