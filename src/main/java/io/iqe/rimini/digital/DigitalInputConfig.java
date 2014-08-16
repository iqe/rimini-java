package io.iqe.rimini.digital;

import java.util.Arrays;

import io.iqe.rimini.FeatureType;
import io.iqe.rimini.config.FeatureConfiguration;

public class DigitalInputConfig extends FeatureConfiguration {
    private boolean usingInputPullup;
    private boolean reversed;
    private long debounceMillis;

    public DigitalInputConfig(int pin, boolean usingInputPullup, boolean reversed, long debounceMillis) {
        super(FeatureType.DIGITAL_INPUT, Arrays.asList(pin));

        this.usingInputPullup = usingInputPullup;
        this.reversed = reversed;
        this.debounceMillis = debounceMillis;
    }

    public int getPin() {
        return getPins().get(0);
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

    @Override
    public String toString() {
        return String.format("%s: Pin %d, Pullup %s, Reversed %s, Debounce %d", super.toString(),
                getPin(), usingInputPullup, reversed, debounceMillis);
    }
}
