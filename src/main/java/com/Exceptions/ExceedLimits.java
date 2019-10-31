package com.Exceptions;

public class ExceedLimits extends IllegalArgumentException {
    public ExceedLimits() {
        super("You exceed the limits!");
    }
}
