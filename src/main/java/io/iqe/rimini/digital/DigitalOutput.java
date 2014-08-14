package io.iqe.rimini.digital;

import io.iqe.nio.MultiSignByteBuffer;
import io.iqe.rimini.AbstractFeature;

public class DigitalOutput extends AbstractFeature<Boolean> {
    @Override
    public void writeMessageContent(Boolean content, MultiSignByteBuffer buf) {
        buf.putUnsigned(content ? 'H' : 'L');
    }
}
