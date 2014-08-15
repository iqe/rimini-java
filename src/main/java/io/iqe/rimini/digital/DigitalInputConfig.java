package io.iqe.rimini.digital;

public class DigitalInputConfig {
    private boolean usingInputPullup;
    private boolean reversed;
    private long debounceMillis;

    public DigitalInputConfig(boolean usingInputPullup, boolean reversed, long debounceMillis) {
        this.usingInputPullup = usingInputPullup;
        this.reversed = reversed;
        this.debounceMillis = debounceMillis;
    }

    public boolean isUsingInputPullup() {
        return usingInputPullup;
    }

    public boolean isReversed() {
        return reversed;
    }

    public long getDebounceMillis() {
        return debounceMillis;
    }
}
