package io.iqe.rimini.digital;

import io.iqe.nio.MultiSignByteBuffer;
import io.iqe.rimini.AbstractFeature;

public class DigitalInput extends AbstractFeature<Boolean> {
    @Override
    public Boolean readMessageContent(MultiSignByteBuffer buf) {
        return buf.getUnsigned() == 'H';
    }
}
