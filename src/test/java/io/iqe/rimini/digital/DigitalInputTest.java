package io.iqe.rimini.digital;

import static org.junit.Assert.*;
import io.iqe.nio.MultiSignByteBuffer;
import io.iqe.rimini.io.MessageStream;

import org.junit.Before;
import org.junit.Test;

public class DigitalInputTest {
    private MultiSignByteBuffer buf;
    private DigitalInput input;

    @Before
    public void setUp() throws Exception {
        buf = MultiSignByteBuffer.allocate(MessageStream.DEFAULT_MAX_MESSAGE_SIZE);
        input = new DigitalInput();
    }

    @Test
    public void shouldReadCharacterAsBoolean() throws Exception {
        buf.clear();
        buf.putUnsigned('H');
        buf.rewind();
        assertTrue(input.readMessageContent(buf));

        buf.clear();
        buf.putUnsigned('L');
        buf.rewind();
        assertFalse(input.readMessageContent(buf));
    }
}
