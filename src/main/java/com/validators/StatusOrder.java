package com.validators;

import com.models.FriendshipStatus;

public class StatusOrder extends Validation {
    private FriendshipStatus currentStatus;

    public StatusOrder(FriendshipStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    @Override
    public boolean check(FriendshipStatus status) throws ExceedLimits {
        if (currentStatus.equals(FriendshipStatus.FRIEND)&&status.equals(FriendshipStatus.REQUEST_SEND))
            throw new ExceedLimits();
        if (currentStatus.equals(FriendshipStatus.REQUEST_SEND)&&status.equals(FriendshipStatus.FORMER_FRIEND))
            throw new ExceedLimits();

        return checkNext(status);
    }
}
