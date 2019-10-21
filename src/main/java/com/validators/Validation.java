package com.validators;

import com.Exceptions.ExceedLimits;
import com.models.FriendshipStatus;

public abstract class Validation {
    private Validation next;

    public Validation linkWith(Validation next) {
        this.next = next;
        return next;
    }

    public abstract boolean check(FriendshipStatus status) throws ExceedLimits;

    protected boolean checkNext(FriendshipStatus status) throws ExceedLimits {
        if (next == null) {
            return true;
        }
        return next.check(status);
    }


}
