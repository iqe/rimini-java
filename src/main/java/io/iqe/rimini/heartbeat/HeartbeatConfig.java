package io.iqe.rimini.heartbeat;

import io.iqe.rimini.FeatureType;
import io.iqe.rimini.config.FeatureConfiguration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HeartbeatConfig extends FeatureConfiguration {
    private List<Integer> streamIdPins;
    private long intervalMillis;

    public HeartbeatConfig(int streamIdPin0, int streamIdPin1, int streamIdPin2, long intervalMillis) {
        super(FeatureType.HEARTBEAT);
        this.streamIdPins = Arrays.asList(streamIdPin0, streamIdPin1, streamIdPin2);
        this.intervalMillis = intervalMillis;

        setUsedPins(new HashSet<>(Arrays.asList(streamIdPin0, streamIdPin1, streamIdPin2)));
    }

    @Override
    public Set<Integer> getUsedPins() {
        return new HashSet<>(streamIdPins);
    }

    public long getIntervalMillis() {
        return intervalMillis;
    }

    @Override
    public String toString() {
        return String.format("Pins: %s, Interval: %dms", streamIdPins, intervalMillis);
    }
}
