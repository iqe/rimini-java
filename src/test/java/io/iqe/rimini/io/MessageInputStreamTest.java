package io.iqe.rimini.io;

import static io.iqe.rimini.io.test.MessageStreamTestSupport.*;
import static org.junit.Assert.*;
import io.iqe.rimini.Address;
import io.iqe.rimini.FeatureRepository;
import io.iqe.rimini.Message;
import io.iqe.rimini.io.test.QueueInputStream;
import io.iqe.rimini.io.test.SimpleTextFeature;
import io.iqe.wasp.BufferSizeException;
import io.iqe.wasp.WaspInputStream;

import org.junit.Before;
import org.junit.Test;

public class MessageInputStreamTest {
    private QueueInputStream input;
    private FeatureRepository features;

    private MessageInputStream stream;

    @Before
    public void setUp() throws Exception {
        input = new QueueInputStream();
        features = new FeatureRepository();

        features.addFeature(42, new SimpleTextFeature());

        stream = new MessageInputStream(5, new WaspInputStream(input), features, 10);
    }

    @Test
    public void shouldUseFeatureToReadMessage() throws Exception {
        // given
        input.writeAll(message(0, 42, 3, 'A', 'B', 'C'));

        // when
        Message message = stream.readMessage();

        // then
        assertEquals(new Address(5, 42), message.getAddress());
        assertEquals("ABC", message.getContent());
    }

    @Test(expected=IllegalStateException.class)
    public void shouldThrowOnMissingFeature() throws Exception {
        input.writeAll(message(0, 43, 3, 'A', 'B', 'C'));
        stream.readMessage();
    }

    @Test
    public void shouldAcceptMessageUpToMaxSize() throws Exception {
        // given
        input.writeAll(message(0, 42, 7, 'A', 'B', 'C', 'D', 'E', 'F', 'G'));

        // when
        Message message = stream.readMessage();

        // then
        assertEquals("ABCDEFG", message.getContent());
    }

    @Test(expected=BufferSizeException.class)
    public void shouldThrowIfMessageIsTooLong() throws Exception {
        // given
        input.writeAll(message(0, 42, 8, 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H')); // 1 byte too long
        stream.readMessage();
    }

    @Test
    public void shouldCloseUnderlyingStream() throws Exception {
        stream.close();
        assertTrue(input.isClosed());
    }
}
