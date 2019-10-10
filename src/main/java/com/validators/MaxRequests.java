package com.validators;


import com.models.FriendshipStatus;
import com.models.Relationship;

import java.util.List;


public class MaxRequests extends Validation {

    private List<Relationship> list;

    public MaxRequests(List<Relationship> list) {
        this.list = list;
    }

    @Override
    public boolean check(FriendshipStatus status) throws ExceedLimits {
        if (status.equals(FriendshipStatus.REQUEST_SEND) && list.size() > 10)
            throw new ExceedLimits();

        return checkNext(status);
    }
}
