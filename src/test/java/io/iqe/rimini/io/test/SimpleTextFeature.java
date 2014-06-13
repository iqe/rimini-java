package io.iqe.rimini.io.test;

import io.iqe.nio.MultiSignByteBuffer;
import io.iqe.rimini.AbstractFeature;

public class SimpleTextFeature extends AbstractFeature<String> {
    @Override
    public String readMessageContent(MultiSignByteBuffer buf) {
        int length = buf.getUnsigned();
        byte[] content = new byte[length];
        buf.get(content);
        return new String(content);
    }

    @Override
    public void writeMessageContent(String content, MultiSignByteBuffer buf) {
        buf.putUnsigned(content.length()); // Only works for length <= 256!
        buf.put(content.getBytes());
    }
}