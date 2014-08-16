package io.iqe.rimini.digital;

import io.iqe.nio.MultiSignByteBuffer;
import io.iqe.rimini.AbstractFeature;

public class DigitalInput extends AbstractFeature<Boolean> {
    @Override
    public Boolean readMessageContent(MultiSignByteBuffer buf) {
        return buf.getUnsigned() == 'H';
    }

    @Override
    public Object readConfiguration(MultiSignByteBuffer buf) {
        buf.getUnsigned(); // skip pin count

        int pin = buf.getUnsigned();
        int flags = buf.getUnsigned();
        long debounceMillis = buf.getUnsignedInt();

        boolean usingInputPullup = (flags & 1) == 1;
        boolean reversed = (flags & 2) == 2;

        return new DigitalInputConfig(pin, usingInputPullup, reversed, debounceMillis);
    }

    @Override
    public void writeConfiguration(Object configuration, MultiSignByteBuffer buf) {
        if (!(configuration instanceof DigitalInputConfig)) {
            throw new IllegalArgumentException(); // FIXME
        }

        DigitalInputConfig config = (DigitalInputConfig) configuration;

        int flags = (config.isUsingInputPullup() ? 1 : 0) + (config.isReversed() ? 2 : 0);

        buf.putUnsigned(1);
        buf.putUnsigned(config.getPin());
        buf.putUnsigned(flags);
        buf.putUnsignedInt(config.getDebounceMillis());
    }
}
