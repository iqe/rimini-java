package io.iqe.rimini;

import io.iqe.nio.MultiSignByteBuffer;

public abstract class AbstractFeature<C> implements Feature<C> {
    @Override
    public void writeMessageContent(C content, MultiSignByteBuffer buf) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support writing messages");
    }

    @Override
    public C readMessageContent(MultiSignByteBuffer buf) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support reading messages");
    }

    @Override
    public Object readConfiguration(MultiSignByteBuffer buffer) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support reading configuration");

    }

    @Override
    public void writeConfiguration(Object configuration, MultiSignByteBuffer buffer) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support writing configuration");
    }
}
