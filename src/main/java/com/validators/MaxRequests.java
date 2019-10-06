package com.validators;

import com.UserService;
import com.models.User;



public class MaxRequests extends Validation {

    private UserService userService=  new UserService();

    @Override
    public boolean check(User user) {
        int size=userService.getOutcomeRequests(user.getId()).size();
        if (size>10)
        return false;

        return checkNext(user);
    }
}
