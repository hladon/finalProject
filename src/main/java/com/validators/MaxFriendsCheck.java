package com.validators;

import com.models.FriendshipStatus;
import com.models.User;



import java.util.List;


public class MaxFriendsCheck extends Validation {

    List<User> list;

    public MaxFriendsCheck(List<User> list) {
        this.list=list;
    }

    @Override
    public boolean check(FriendshipStatus status) throws ExceedLimits {
        if(status.equals(FriendshipStatus.FRIEND)&&list.size()>100){
            throw new ExceedLimits();
        }
        return checkNext(status);
    }
}
