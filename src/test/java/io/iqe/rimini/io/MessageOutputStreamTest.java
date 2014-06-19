package io.iqe.rimini.io;

import static org.junit.Assert.*;
import static io.iqe.rimini.io.test.MessageStreamTestSupport.*;

import java.nio.BufferOverflowException;

import io.iqe.rimini.Address;
import io.iqe.rimini.FeatureRepository;
import io.iqe.rimini.Message;
import io.iqe.rimini.io.test.QueueOutputStream;
import io.iqe.rimini.io.test.SimpleTextFeature;
import io.iqe.wasp.WaspOutputStream;

import org.junit.Before;
import org.junit.Test;

public class MessageOutputStreamTest {
    private QueueOutputStream output;
    private FeatureRepository features;

    private MessageOutputStream stream;

    @Before
    public void setUp() throws Exception {
        output = new QueueOutputStream();
        features = new FeatureRepository();

        features.addFeature(42, new SimpleTextFeature());

        stream = new MessageOutputStream(5, new WaspOutputStream(output), features, 10);
    }

    @Test
    public void shouldUseFeatureToWriteMessage() throws Exception {
        // given
        Message message = createMessage(5, 42, "ABC");

        // when
        stream.writeMessage(message);

        // then
        String expectedOutput = formatBytes(message(0, 42, 3, 'A', 'B', 'C'));
        String actualOutput = formatBytes(output.readAll());

        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnInvalidStreamId() throws Exception {
        stream.writeMessage(createMessage(1, 42, "ABC"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnMissingFeature() throws Exception {
        stream.writeMessage(createMessage(5, 43, "ABC"));
    }

    @Test
    public void shouldAcceptMessageUpToMaxSize() throws Exception {
        // given
        Message message = createMessage(5, 42, "ABCDEFG");

        // when
        stream.writeMessage(message);

        // then
        String expectedOutput = formatBytes(message(0, 42, 7, 'A', 'B', 'C', 'D', 'E', 'F', 'G'));
        String actualOutput = formatBytes(output.readAll());

        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = BufferOverflowException.class)
    public void shouldThrowIfMessageIsTooLong() throws Exception {
        Message message = createMessage(5, 42, "ABCDEFGH");
        stream.writeMessage(message);
    }

    @Test
    public void shouldCloseUnderlyingStream() throws Exception {
        stream.close();
        assertTrue(output.isClosed());
    }

    private Message createMessage(int streamId, int featureId, String content) {
        return new Message(new Address(streamId, featureId), content);
    }
}
