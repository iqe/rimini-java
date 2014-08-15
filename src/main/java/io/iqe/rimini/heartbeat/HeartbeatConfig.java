package io.iqe.rimini.heartbeat;

import java.util.Arrays;
import java.util.HashSet;

import io.iqe.rimini.FeatureType;
import io.iqe.rimini.config.FeatureConfiguration;

public class HeartbeatConfig extends FeatureConfiguration {
    private int[] streamIdPins;
    private long intervalMillis;

    public HeartbeatConfig(int streamIdPin0, int streamIdPin1, int streamIdPin2, long intervalMillis) {
        super(FeatureType.HEARTBEAT);
        this.streamIdPins = new int[] {streamIdPin0, streamIdPin1, streamIdPin2};
        this.intervalMillis = intervalMillis;

        setUsedPins(new HashSet<>(Arrays.asList(streamIdPin0, streamIdPin1, streamIdPin2)));
    }

    public int[] getStreamIdPins() {
        return streamIdPins;
    }

    public long getIntervalMillis() {
        return intervalMillis;
    }

    @Override
    public String toString() {
        return String.format("Pins: %s, Interval: %dms", Arrays.toString(streamIdPins), intervalMillis);
    }
}
