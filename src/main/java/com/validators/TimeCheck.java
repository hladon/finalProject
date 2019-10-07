package com.validators;

import com.models.FriendshipStatus;
import com.models.Relationship;
import java.util.Date;


public class TimeCheck extends Validation {

    private Relationship relationship;

    public TimeCheck(Relationship relationship) {
        this.relationship = relationship;
    }

    @Override
    public boolean check(FriendshipStatus status) throws ExceedLimits {

        if (relationship.equals(FriendshipStatus.FORMER_FRIEND)
                &&(relationship.getLastChanges().getTime()-new Date().getTime()<86400000*3))
            throw new ExceedLimits();
        return checkNext(status);
    }


}
