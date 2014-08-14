package io.iqe.rimini.io;

import static org.junit.Assert.*;
import io.iqe.rimini.Address;
import io.iqe.rimini.Message;
import io.iqe.rimini.MessageSourceListener;
import io.iqe.rimini.io.test.Arduino;
import io.iqe.rimini.io.test.SimpleTextFeature;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.util.concurrent.Service.State;

public class MessageSourceTest {
    private Arduino a5;
    private TestListener s5f42Listener;
    private TestListener s5f43Listener;
    private TestListener s6f42Listener;

    private MessageSource source;

    @Before
    public void setUp() throws Exception {
        a5 = new Arduino(5);
        a5.addFeature(42, new SimpleTextFeature());

        s5f42Listener = new TestListener();
        s5f43Listener = new TestListener();
        s6f42Listener = new TestListener();

        source = new MessageSource();

        source.addStream(a5.getStream());

        source.addListener(new Address(5, 42), s5f42Listener);
        source.addListener(new Address(5, 43), s5f43Listener);
        source.addListener(new Address(6, 42), s6f42Listener);

        source.start();
    }

    @After
    public void tearDown() throws Exception {
        if (source != null) {
            source.stop();
        }
    }

    @Test
    public void shouldForwardMessageToRegisteredListeners() throws Exception {
        // when
        a5.sendMessage(0, 42, 3, 'A', 'B', 'C');

        // then
        expectMessage(s5f42Listener, "ABC");
        expectNoMessage(s5f43Listener);
        expectNoMessage(s6f42Listener);
    }

    @Test
    public void shouldAllowAddingListenersWhileRunning() throws Exception {
        // given
        TestListener listener = new TestListener();
        source.addListener(new Address(5, 42), listener);

        // when
        a5.sendMessage(0, 42, 3, 'A', 'B', 'C');

        // then
        expectMessage(listener, "ABC");
    }

    @Test
    public void shouldAllowRemovingListenersWhileRunning() throws Exception {
        // given
        TestListener listener = new TestListener();

        // when, then
        source.addListener(new Address(5, 42), listener);
        a5.sendMessage(0, 42, 3, 'A', 'B', 'C');
        expectMessage(listener, "ABC");

        source.removeListener(new Address(5, 42), listener);
        a5.sendMessage(0, 42, 3, 'A', 'B', 'C');
        expectNoMessage(listener);
    }

    @Test
    public void shouldAllowAddingStreamsWhileRunning() throws Exception {
        // given
        Arduino a6 = new Arduino(6);
        a6.addFeature(13, new SimpleTextFeature());

        TestListener a6f13Listener = new TestListener();
        source.addListener(new Address(6, 13), a6f13Listener);

        // when
        source.addStream(a6.getStream());
        a6.sendMessage(0, 13, 3, 'A', 'B', 'C');

        // then
        expectMessage(a6f13Listener, "ABC");
    }

    @Test
    public void shouldAllowRemovingStreamsWhileRunning() throws Exception {
        // when
        source.removeStream(a5.getStream());
        a5.sendMessage(0, 42, 3, 'A', 'B', 'C');

        // then
        expectNoMessage(s5f42Listener);
    }

    private void expectMessage(TestListener listener, String content) throws InterruptedException {
        Message message = listener.messages.poll(1000, TimeUnit.MILLISECONDS);
        assertNotNull(String.format("Expected '%s', but did not receive any message", content), message);
        assertEquals(content, message.getContent());
    }

    private void expectNoMessage(TestListener listener) throws InterruptedException {
        Message message = listener.messages.poll(50, TimeUnit.MILLISECONDS);
        assertNull(String.format("Did not expect any message, but got %s", message), message);
    }

    private final class TestListener implements MessageSourceListener {
        private BlockingQueue<Message> messages = new LinkedBlockingQueue<>();

        @Override
        public void onMessage(Message message) {
            messages.add(message);
        }
    }
}
