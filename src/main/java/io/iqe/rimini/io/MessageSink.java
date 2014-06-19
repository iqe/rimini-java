package io.iqe.rimini.io;

import io.iqe.rimini.Address;
import io.iqe.rimini.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

public class MessageSink extends AbstractExecutionThreadService {
    private static final Logger log = LoggerFactory.getLogger("Rimini");

    private static final Message BREAK = new Message(new Address(-1, -1), "");

    private BlockingQueue<Message> messageQueue;
    private CopyOnWriteArraySet<MessageStream> streams;

    public MessageSink() {
        messageQueue = new LinkedBlockingQueue<>();
        streams = new CopyOnWriteArraySet<>();
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
                    log.debug("Wrote message '{}'", message);
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
