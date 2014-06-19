package io.iqe.rimini.io;

import io.iqe.nio.MultiSignByteBuffer;
import io.iqe.rimini.Address;
import io.iqe.rimini.Feature;
import io.iqe.rimini.FeatureRepository;
import io.iqe.rimini.Message;
import io.iqe.wasp.WaspInputStream;
import io.iqe.wasp.WaspStream;

import java.io.Closeable;
import java.io.IOException;

public class MessageInputStream implements Closeable {
    private int streamId;
    private WaspInputStream input;
    private FeatureRepository features;

    private MultiSignByteBuffer buffer;

    public MessageInputStream(int streamId, WaspInputStream input, FeatureRepository features, int maxMessageSize) {
        this.streamId = streamId;
        this.input = input;
        this.features = features;
        this.buffer = MultiSignByteBuffer.allocate(maxMessageSize + WaspStream.CRC_SIZE);
    }

    public int getStreamId() {
        return streamId;
    }

    public void setStreamId(int streamId) {
        this.streamId = streamId;
    }

    /**
     * Reads the next message from the stream.
     * <p>
     * This method blocks until a message is available.
     */
    public Message readMessage() throws IOException {
        input.readMessageIntoBuffer(buffer.array());

        buffer.clear();
        int featureId = buffer.getUnsignedShort();

        Feature<?> feature = features.getFeature(featureId);
        if (feature == null) {
            throw new IllegalStateException(
                    String.format("No feature configured in stream '%s' for feature ID '%s'", streamId, featureId));
        }

        Object content = feature.readMessageContent(buffer);

        return new Message(new Address(streamId, featureId), content);
    }

    /**
     * Closes the underlying {@link WaspInputStream}.
     */
    @Override
    public void close() throws IOException {
        input.close();
    }
}
