package io.iqe.rimini.heartbeat;

import io.iqe.nio.MultiSignByteBuffer;
import io.iqe.rimini.AbstractFeature;

public class Heartbeat extends AbstractFeature<Integer> {
    @Override
    public Integer readMessageContent(MultiSignByteBuffer buffer) {
        return (int)buffer.getUnsigned(); // streamId
    }

    @Override
    public Object readConfiguration(MultiSignByteBuffer buffer) {
        int streamIdPin0 = buffer.getUnsigned();
        int streamIdPin1 = buffer.getUnsigned();
        int streamIdPin2 = buffer.getUnsigned();
        long intervalMillis = buffer.getUnsignedInt();

        return new HeartbeatConfig(streamIdPin0, streamIdPin1, streamIdPin2, intervalMillis);
    }
}
