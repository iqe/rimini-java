package io.iqe.rimini.io;

import static org.junit.Assert.*;
import static io.iqe.rimini.io.test.MessageStreamTestSupport.*;

import java.util.concurrent.TimeUnit;

import io.iqe.rimini.Address;
import io.iqe.rimini.Message;
import io.iqe.rimini.io.test.Arduino;
import io.iqe.rimini.io.test.SimpleTextFeature;

import org.junit.Before;
import org.junit.Test;

public class MessageSinkTest {
    private Arduino a5;
    private MessageSink sink;

    @Before
    public void setUp() throws Exception {
        a5 = new Arduino(5);
        a5.addFeature(42, new SimpleTextFeature());

        sink = new MessageSink();
        sink.addStream(a5.getStream());

        sink.start();
    }

    @Test
    public void shouldWriteMessageToOutputStream() throws Exception {
        // given
        sink.writeMessage(new Message(new Address(5, 42), "ABC"));
        byte[] expectedBytes = message(0, 42, 3, 'A', 'B', 'C');

        // when
        byte[] messageBytes = a5.receiveMessage(expectedBytes.length);

        // then
        assertEquals(formatBytes(expectedBytes), formatBytes(messageBytes));
    }

    @Test
    public void shouldIgnoreMessagesForUnknownFeatures() throws Exception {
        // when
        sink.writeMessage(new Message(new Address(5, 43), "ABC"));

        // then
        Thread.sleep(100);
        assertFalse(a5.hasReceivedBytes());
    }

    @Test
    public void shouldIgnoreMessagesForDifferentStream() throws Exception {
        // when
        sink.writeMessage(new Message(new Address(6, 42), "ABC"));

        // then
        Thread.sleep(100);
        assertFalse(a5.hasReceivedBytes());
    }

    @Test
    public void shouldBeStoppable() throws Exception {
        sink.stop(1000, TimeUnit.MILLISECONDS);
    }
}
