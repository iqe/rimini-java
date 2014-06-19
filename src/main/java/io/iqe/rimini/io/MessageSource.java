package io.iqe.rimini.io;

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

    private static final Message BREAK = new Message(-1, -1, "");

    private LinkedBlockingQueue<Message> messageQueue;
    private CopyOnWriteArraySet<MessageReader> readers;
    private HashMultimap<Integer, MessageSourceListener> listeners;

    public MessageSource() {
        messageQueue = new LinkedBlockingQueue<>();
        readers = new CopyOnWriteArraySet<>();
        listeners = HashMultimap.create();
    }

    public void addStream(MessageStream stream) {
        MessageReader reader = new MessageReader(stream);
        readers.add(reader);

        reader.startAsync();
        reader.awaitRunning();
    }

    public void removeStream(MessageStream stream) {
        for (MessageReader reader : readers) {
            if (reader.stream.equals(stream)) {
                readers.remove(reader);

                reader.stopAsync();
                reader.awaitTerminated();
                return;
            }
        }
    }

    public void addListener(int streamId, MessageSourceListener listener) {
        listeners.put(streamId, listener);
    }

    public void removeListener(int streamId, MessageSourceListener listener) {
        listeners.remove(streamId, listener);
    }

    @Override
    protected void triggerShutdown() {
        try {
            messageQueue.put(BREAK);
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    protected void run() throws Exception {
        try {
            log.info("Started MessageSource");

            for (MessageReader reader : readers) {
                reader.startAsync();
                reader.awaitRunning();
            }

            while (isRunning()) {
                Message message = messageQueue.take();
                if (message == BREAK) {
                    break;
                }

                for (MessageSourceListener listener : listeners.get(message.getStreamId())) {
                    try {
                        listener.onMessage(message);
                    } catch (Exception e) {
                        log.warn("Failure during onMessage() of listener '{}'", listener, e);
                    }
                }
            }
        } finally {
            for (MessageReader reader : readers) {
                reader.stopAsync();
                reader.awaitTerminated();
            }
            log.info("Stopped MessageSource");
        }
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
                        log.debug("Received message '{}'", message);
                        if (message.getFeatureId() == 1) { // FIXME magic
                                                           // constant!
                            int currentStreamId = stream.getStreamId();
                            int newStreamId = (Integer) message.getContent();

                            if (currentStreamId != newStreamId) {
                                stream.setStreamId(newStreamId);
                                log.info("Renamed stream '{}' to '{}'", currentStreamId, newStreamId);
                            }
                        }
                        messageQueue.add(message);
                    } catch (EOFException e) {
                        log.warn(e.getMessage());
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