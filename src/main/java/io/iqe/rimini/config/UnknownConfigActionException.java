package io.iqe.rimini.config;

public class UnknownConfigActionException extends RuntimeException {
    public UnknownConfigActionException(int actionId) {
        super(String.format("Unknown Rimini config action ID: %d", actionId));
    }
}
