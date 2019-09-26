package com.restrictions;

abstract class Validation {
    private Validation next;

    public Validation linkWith(Validation next){
        this.next=next;
        return next;
    }

    public abstract boolean check();

}
