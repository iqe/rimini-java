package io.iqe.rimini.io;

import io.iqe.rimini.FeatureRepository;
import io.iqe.rimini.Message;
import io.iqe.wasp.WaspInputStream;
import io.iqe.wasp.WaspOutputStream;

import java.io.Closeable;
import java.io.IOException;

/**
 * Convenience wrapper around {@link WaspInputStream} and
 * {@link WaspOutputStream}.
 *
 * This allows to manage a {@link MessageInputStream} and a
 * {@link MessageOutputStream} with the same stream ID.
 */
public class MessageStream implements Closeable {
    public static final int DEFAULT_MAX_MESSAGE_SIZE = 128;

    private MessageInputStream input;
    private MessageOutputStream output;

    public MessageStream(int streamId, WaspInputStream input, WaspOutputStream output, FeatureRepository features) {
        this(streamId, input, output, features, DEFAULT_MAX_MESSAGE_SIZE);
    }

    public MessageStream(int streamId, WaspInputStream input, WaspOutputStream output, FeatureRepository features,
            int maxMessageSize) {
        this.input = new MessageInputStream(streamId, input, features, maxMessageSize);
        this.output = new MessageOutputStream(streamId, output, features, maxMessageSize);
    }

    public int getStreamId() {
        return input.getStreamId();
    }

    public void setStreamId(int streamId) {
        input.setStreamId(streamId);
        output.setStreamId(streamId);
    }

    public Message readMessage() throws IOException {
        return input.readMessage();
    }

    public void writeMessage(Message message) throws IOException {
        output.writeMessage(message);
    }

    public boolean handlesMessage(Message message) {
        return message.getAddress().getStreamId() == input.getStreamId();
    }

    @Override
    public void close() throws IOException {
        input.close();
        output.close();
    }
}
