package com.validators;

public class ExceedLimits extends Exception {
    public ExceedLimits() {
        super("You exceed the limits!");
    }
}
