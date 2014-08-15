package io.iqe.rimini.config;

import static org.junit.Assert.*;
import io.iqe.nio.MultiSignByteBuffer;
import io.iqe.rimini.io.MessageStream;
import io.iqe.rimini.io.test.MessageStreamTestSupport;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class RiminiConfigTest {
    private MultiSignByteBuffer buf;
    private RiminiConfig cfg;

    @Before
    public void setUp() throws Exception {
        buf = MultiSignByteBuffer.allocate(MessageStream.DEFAULT_MAX_MESSAGE_SIZE);
        cfg = new RiminiConfig();
    }

    @Test
    public void shouldWriteFeaturesRequest() throws Exception {
        FeaturesRequest request = new FeaturesRequest();

        cfg.writeMessageContent(request, buf);

        assertWrittenBytes(ActionTypes.CONFIG_REQ_FEATURES);
    }

    @Test(expected = UnknownConfigActionException.class)
    public void shouldThrownOnUnknownActionIdWhenWriting() throws Exception {
        AbstractConfigAction content = new AbstractConfigAction(255) {};
        cfg.writeMessageContent(content, buf);
    }

    @Test
    public void shouldReadFeaturesResponse() throws Exception {
        buffer(ActionTypes.CONFIG_RSP_FEATURES, 2, 0, 1, 1, 1);

        AbstractConfigAction content = cfg.readMessageContent(buf);

        assertEquals(FeaturesResponse.class, content.getClass());
        assertEquals(set(1, 257), ((FeaturesResponse) content).getFeatureIds());
    }

    @Test(expected = UnknownConfigActionException.class)
    public void shouldThrownOnUnknownActionIdWhenReading() throws Exception {
        buffer(255);
        cfg.readMessageContent(buf);
    }

    private void buffer(int... bytesAsInts) {
        buf.clear();
        byte[] bytes = MessageStreamTestSupport.buildByteArray(bytesAsInts);
        for (byte b : bytes) {
            buf.put(b);
        }
        buf.rewind();
    }

    @SuppressWarnings("unchecked")
    private <T> Set<T> set(T... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }

    private void assertWrittenBytes(int... bytesAsInts) {
        byte[] expectedBytes = MessageStreamTestSupport.buildByteArray(bytesAsInts);
        byte[] actualBytes = new byte[expectedBytes.length];

        assertEquals(expectedBytes.length, buf.position());

        System.arraycopy(buf.array(), 0, actualBytes, 0, actualBytes.length);

        assertArrayEquals(expectedBytes, actualBytes);
    }
}
