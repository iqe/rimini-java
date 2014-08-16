package io.iqe.rimini.heartbeat;

import static org.junit.Assert.*;
import io.iqe.rimini.test.AbstractFeatureTest;

import org.junit.Before;
import org.junit.Test;

public class HeartbeatTest extends AbstractFeatureTest {
    private Heartbeat heartbeat;

    @Before
    public void setUp() throws Exception {
        heartbeat = new Heartbeat();
    }

    @Test
    public void shouldReadStreamId() throws Exception {
        buffer(255);

        assertEquals(255, (int)heartbeat.readMessageContent(buf));
    }

    @Test
    public void shouldReadConfig() throws Exception {
        buffer(3, 6, 7, 8, 0, 0, 0x12, 0x67);

        HeartbeatConfig config = (HeartbeatConfig) heartbeat.readConfiguration(buf);

        assertEquals(set(6, 7, 8), config.getUsedPins());
        assertEquals(4711, config.getIntervalMillis());
    }
}
