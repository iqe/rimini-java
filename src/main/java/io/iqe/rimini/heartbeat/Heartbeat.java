package io.iqe.rimini.heartbeat;

import io.iqe.nio.MultiSignByteBuffer;
import io.iqe.rimini.AbstractFeature;

public class Heartbeat extends AbstractFeature<Integer> {
    @Override
    public Integer readMessageContent(MultiSignByteBuffer buffer) {
        return (int)buffer.getUnsigned(); // streamId
    }
}
