package io.iqe.rimini.io;

import io.iqe.rimini.Address;
import io.iqe.rimini.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

public class MessageSink {
    private static final Logger log = LoggerFactory.getLogger("Rimini");

    private static final Message BREAK = new Message(new Address(-1, -1), "");

    private MessageSinkService service;
    private BlockingQueue<Message> messageQueue;
    private CopyOnWriteArraySet<MessageStream> streams;

    public MessageSink() {
        service = new MessageSinkService();
        messageQueue = new LinkedBlockingQueue<>();
        streams = new CopyOnWriteArraySet<>();
    }

    public void start() {
        service.startAsync();
        service.awaitRunning();
    }

    public void stop() {
        service.stopAsync();
        service.awaitTerminated();
    }

    public void stop(long timeout, TimeUnit unit) throws TimeoutException {
        service.stopAsync();
        service.awaitTerminated(timeout, unit);
    }

    public void addStream(MessageStream stream) {
        streams.add(stream);
    }

    public void removeStream(MessageStream stream) {
        streams.remove(stream);
    }

    public void writeMessage(Message message) {
        messageQueue.add(message);
    }

    private class MessageSinkService extends AbstractExecutionThreadService {
        @Override
        protected void run() throws Exception {
            try {
                log.info("Started MessageSink");

                while (isRunning()) {
                    Message message = messageQueue.take();
                    if (message == BREAK) {
                        break;
                    }

                    MessageStream stream = getStream(message);
                    if (stream == null) {
                        log.warn("No stream found for message '{}'", message);
                        continue;
                    }

                    try {
                        stream.writeMessage(message);
                        log.debug("Wrote message {}", message);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            } finally {
                log.info("Stopped MessageSink");
            }
        }

        private MessageStream getStream(Message message) {
            for (MessageStream stream : streams) {
                if (stream.handlesMessage(message)) {
                    return stream;
                }
            }
            return null;
        }

        @Override
        protected void triggerShutdown() {
            try {
                messageQueue.put(BREAK);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
