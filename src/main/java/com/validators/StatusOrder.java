package com.validators;

import com.models.FriendshipStatus;

public class StatusOrder extends Validation {
    private FriendshipStatus currentStatus;

    public StatusOrder(FriendshipStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    @Override
    public boolean check(FriendshipStatus status) throws ExceedLimits {
        if ((currentStatus.equals(FriendshipStatus.FRIEND) && status.equals(FriendshipStatus.FORMER_FRIEND))
                || (currentStatus.equals(FriendshipStatus.FORMER_FRIEND) && status.equals(FriendshipStatus.REQUEST_SEND))
                || (currentStatus.equals(FriendshipStatus.REQUEST_SEND) && !status.equals(FriendshipStatus.FORMER_FRIEND))
                || (currentStatus.equals(FriendshipStatus.REQUEST_DECLINE) && status.equals(FriendshipStatus.FRIEND))
                || (currentStatus.equals(FriendshipStatus.REQUEST_CANCELED) && status.equals(FriendshipStatus.REQUEST_SEND)))
            return checkNext(status);

        throw new IllegalArgumentException();
    }
}
