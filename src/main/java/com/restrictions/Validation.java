package com.restrictions;

import com.models.User;

abstract class Validation {
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
