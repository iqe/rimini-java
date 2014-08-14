package io.iqe.rimini.heartbeat;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import io.iqe.nio.MultiSignByteBuffer;
import io.iqe.rimini.io.MessageStream;

public class HeartbeatTest {
    private MultiSignByteBuffer buf;
    private Heartbeat heartbeat;

    @Before
    public void setUp() throws Exception {
        buf = MultiSignByteBuffer.allocate(MessageStream.DEFAULT_MAX_MESSAGE_SIZE);
        heartbeat = new Heartbeat();
    }

    @Test
    public void shouldReadStreamId() throws Exception {
        buf.putUnsigned(255);
        buf.rewind();
        assertEquals(255, (int)heartbeat.readMessageContent(buf));
    }
}
