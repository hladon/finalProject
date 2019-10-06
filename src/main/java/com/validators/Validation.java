package com.validators;

import com.models.User;

public abstract class Validation {
    private Validation next;

    public Validation linkWith(Validation next){
        this.next=next;
        return next;
    }

    public abstract boolean check(User user);

    protected boolean checkNext(User user ){
        if (next == null) {
            return true;
        }
        return next.check(user);
    }


}
