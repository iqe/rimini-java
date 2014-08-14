package io.iqe.rimini.digital;

import static org.junit.Assert.*;
import io.iqe.nio.MultiSignByteBuffer;
import io.iqe.rimini.io.MessageStream;

import org.junit.Before;
import org.junit.Test;

public class DigitalOutputTest {
    private MultiSignByteBuffer buf;
    private DigitalOutput output;

    @Before
    public void setUp() throws Exception {
        buf = MultiSignByteBuffer.allocate(MessageStream.DEFAULT_MAX_MESSAGE_SIZE);
        output = new DigitalOutput();
    }

    @Test
    public void shouldWriteBooleanAsCharacter() throws Exception {
        buf.clear();
        output.writeMessageContent(true, buf);
        buf.rewind();
        assertEquals('H', buf.getUnsigned());

        buf.clear();
        output.writeMessageContent(false, buf);
        buf.rewind();
        assertEquals('L', buf.getUnsigned());
    }
}
