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

    @Test
    public void shouldWriteVersionRequest() throws Exception {
        VersionRequest request = new VersionRequest();

        cfg.writeMessageContent(request, buf);

        assertWrittenBytes(ActionTypes.CONFIG_REQ_VERSION);
    }

    @Test(expected = UnknownConfigActionException.class)
    public void shouldThrownOnUnknownActionIdWhenWriting() throws Exception {
        AbstractConfigAction content = new AbstractConfigAction(255) {};
        cfg.writeMessageContent(content, buf);
    }

    @Test
    public void shouldReadFeaturesResponse() throws Exception {
        buffer(ActionTypes.CONFIG_RSP_FEATURES, 0, 2, 0, 1, 1, 1);

        FeaturesResponse response = readMessageContent(FeaturesResponse.class);

        assertEquals(set(1, 257), response.getFeatureIds());
    }

    @Test
    public void sholdReadVersionResponse() throws Exception {
        buffer(ActionTypes.CONFIG_RSP_VERSION, 1, 2, 3);

        VersionResponse response = readMessageContent(VersionResponse.class);

        assertEquals("1.2.3", response.getVersion());
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

    @SuppressWarnings("unchecked")
    private <T extends AbstractConfigAction> T readMessageContent(Class<T> expectedResponseClass) {
        AbstractConfigAction content = cfg.readMessageContent(buf);

        assertEquals(expectedResponseClass, content.getClass());

        return (T)content;
    }

    private void assertWrittenBytes(int... bytesAsInts) {
        byte[] expectedBytes = MessageStreamTestSupport.buildByteArray(bytesAsInts);
        byte[] actualBytes = new byte[expectedBytes.length];

        assertEquals(expectedBytes.length, buf.position());

        System.arraycopy(buf.array(), 0, actualBytes, 0, actualBytes.length);

        assertArrayEquals(expectedBytes, actualBytes);
    }
}
