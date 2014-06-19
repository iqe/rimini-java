package io.iqe.rimini.io;

import static io.iqe.rimini.io.test.MessageStreamTestSupport.*;
import static org.junit.Assert.*;
import io.iqe.rimini.Address;
import io.iqe.rimini.FeatureRepository;
import io.iqe.rimini.Message;
import io.iqe.rimini.MessageSourceListener;
import io.iqe.rimini.io.test.QueueInputStream;
import io.iqe.rimini.io.test.QueueOutputStream;
import io.iqe.rimini.io.test.SimpleTextFeature;
import io.iqe.wasp.WaspInputStream;
import io.iqe.wasp.WaspOutputStream;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

public class MessageSourceTest {
    private QueueInputStream input;
    private QueueOutputStream output;
    private FeatureRepository features;
    private MessageStream stream;
    private TestListener s5f42Listener;
    private TestListener s5f43Listener;
    private TestListener s6f42Listener;

    private MessageSource source;

    @Before
    public void setUp() throws Exception {
        input = new QueueInputStream();
        output = new QueueOutputStream();

        features = new FeatureRepository();
        stream = new MessageStream(5, new WaspInputStream(input), new WaspOutputStream(output), features);
        s5f42Listener = new TestListener();
        s5f43Listener = new TestListener();
        s6f42Listener = new TestListener();

        source = new MessageSource();

        source.addListener(new Address(5, 42), s5f42Listener);
        source.addListener(new Address(5, 43), s5f43Listener);
        source.addListener(new Address(6, 42), s6f42Listener);
        source.addStream(stream);
    }

    @Test
    public void shouldForwardMessageToRegisteredListeners() throws Exception {
        try {
            // given
            source.startAsync();
            source.awaitRunning();

            features.addFeature(42, new SimpleTextFeature());

            // when
            input.writeAll(message(0, 42, 3, 'A', 'B', 'C'));

            // then
            expectMessage(s5f42Listener, "ABC");
            expectNoMessage(s5f43Listener);
            expectNoMessage(s6f42Listener);
        } finally {
            source.stopAsync();
            source.awaitTerminated();
        }
    }

    @Test
    public void shouldAllowAddingListenersWhileRunning() throws Exception {
        try {
            // given
            source.startAsync();
            source.awaitRunning();

            features.addFeature(42, new SimpleTextFeature());

            TestListener listener = new TestListener();
            source.addListener(new Address(5, 42), listener);

            // when
            input.writeAll(message(0, 42, 3, 'A', 'B', 'C'));

            // then
            expectMessage(listener, "ABC");
        } finally {
            source.stopAsync();
            source.awaitTerminated();
        }
    }

    @Test
    public void shouldAllowRemovingListenersWhileRunning() throws Exception {
        try {
            // given
            source.startAsync();
            source.awaitRunning();

            features.addFeature(42, new SimpleTextFeature());

            TestListener listener = new TestListener();

            // when, then
            source.addListener(new Address(5, 42), listener);
            input.writeAll(message(0, 42, 3, 'A', 'B', 'C'));
            expectMessage(listener, "ABC");

            source.removeListener(new Address(5, 42), listener);
            input.writeAll(message(0, 42, 3, 'A', 'B', 'C'));
            expectNoMessage(listener);
        } finally {
            source.stopAsync();
            source.awaitTerminated();
        }
    }

    private void expectMessage(TestListener listener, String content) throws InterruptedException {
        Message message = listener.messages.poll(100, TimeUnit.MILLISECONDS);
        assertNotNull(message);
        assertEquals(content, message.getContent());
    }

    private void expectNoMessage(TestListener listener) throws InterruptedException {
        Message message = listener.messages.poll(50, TimeUnit.MILLISECONDS);
        assertNull(message);
    }

    private final class TestListener implements MessageSourceListener {
        private BlockingQueue<Message> messages = new LinkedBlockingQueue<>();

        @Override
        public void onMessage(Message message) {
            messages.add(message);
        }
    }
}
