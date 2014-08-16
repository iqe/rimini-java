package io.iqe.rimini.heartbeat;

import io.iqe.rimini.FeatureType;
import io.iqe.rimini.config.FeatureConfiguration;

import java.util.Arrays;

public class HeartbeatConfig extends FeatureConfiguration {
    private long intervalMillis;

    public HeartbeatConfig(int streamIdPin0, int streamIdPin1, int streamIdPin2, long intervalMillis) {
        super(FeatureType.HEARTBEAT, Arrays.asList(streamIdPin0, streamIdPin1, streamIdPin2));
        this.intervalMillis = intervalMillis;
    }

    public long getIntervalMillis() {
        return intervalMillis;
    }

    @Override
    public String toString() {
        return String.format("Pins: %s, Interval: %dms", getPins(), intervalMillis);
    }
}
