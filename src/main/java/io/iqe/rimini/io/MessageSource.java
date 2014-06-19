package io.iqe.rimini.io;

import io.iqe.rimini.Address;
import io.iqe.rimini.Message;
import io.iqe.rimini.MessageSourceListener;

import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.util.concurrent.AbstractExecutionThreadService;

public class MessageSource extends AbstractExecutionThreadService {
    private static final Logger log = LoggerFactory.getLogger("Rimini");

    private static final Message BREAK = new Message(new Address(-1, -1), "");

    private LinkedBlockingQueue<Message> messageQueue;
    private CopyOnWriteArraySet<MessageReader> readers;
    private HashMultimap<Address, MessageSourceListener> listeners;

    public MessageSource() {
        messageQueue = new LinkedBlockingQueue<>();
        readers = new CopyOnWriteArraySet<>();
        listeners = HashMultimap.create();
    }

    public void addStream(MessageStream stream) {
        MessageReader reader = new MessageReader(stream);
        readers.add(reader);

        if (isRunning()) {
            reader.startAsync();
            reader.awaitRunning();
        }
    }

    public void removeStream(MessageStream stream) {
        for (MessageReader reader : readers) {
            if (reader.stream.equals(stream)) {
                readers.remove(reader);

                if (isRunning()) {
                    reader.stopAsync();
                    reader.awaitTerminated();
                }
                return;
            }
        }
    }

    public void addListener(Address address, MessageSourceListener listener) {
        listeners.put(address, listener);
    }

    public void removeListener(Address address, MessageSourceListener listener) {
        listeners.remove(address, listener);
    }

    @Override
    protected void triggerShutdown() {
        try {
            messageQueue.put(BREAK);
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    protected void startUp() throws Exception {
        log.info("Starting MessageSource");

        for (MessageReader reader : readers) {
            reader.startAsync();
            reader.awaitRunning();
        }
    }

    @Override
    protected void run() throws Exception {
        while (isRunning()) {
            Message message = messageQueue.take();
            if (message == BREAK) {
                break;
            }

            for (MessageSourceListener listener : listeners.get(message.getAddress())) {
                try {
                    listener.onMessage(message);
                } catch (Exception e) {
                    log.warn("Failure during onMessage() of listener '{}'", listener, e);
                }
            }
        }
    }

    @Override
    protected void shutDown() throws Exception {
        for (MessageReader reader : readers) {
            reader.stopAsync();
            reader.awaitTerminated();
        }

        log.info("Stopped MessageSource");
    }

    private class MessageReader extends AbstractExecutionThreadService {
        private MessageStream stream;

        public MessageReader(MessageStream stream) {
            this.stream = stream;
        }

        @Override
        protected void triggerShutdown() {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    log.warn("{} while closing stream '{}': {}", e.getClass().getSimpleName(), stream.getStreamId(),
                            e.getMessage());
                }
            }
        }

        @Override
        protected void run() throws Exception {
            try {
                log.info("Started MessageReader for stream '{}'", stream.getStreamId());

                while (true) {
                    try {
                        Message message = stream.readMessage();
                        log.debug("Received message {}", message);
                        if (message.getAddress().getFeatureId() == 1) { // FIXME magic constant!
                            int currentStreamId = stream.getStreamId();
                            int newStreamId = (Integer) message.getContent();

                            if (currentStreamId != newStreamId) {
                                stream.setStreamId(newStreamId);
                                log.info("Renamed stream '{}' to '{}'", currentStreamId, newStreamId);
                            }
                        }
                        messageQueue.add(message);
                    } catch (EOFException e) {
                        log.debug("Reached end of stream '{}'", stream.getStreamId());
                        break;
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            } finally {
                log.info("Stopped MessageReader for stream '{}'", stream.getStreamId());
            }
        }
    }
}
