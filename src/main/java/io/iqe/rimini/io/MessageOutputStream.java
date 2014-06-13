package io.iqe.rimini.io;

import io.iqe.nio.MultiSignByteBuffer;
import io.iqe.rimini.Feature;
import io.iqe.rimini.FeatureRepository;
import io.iqe.rimini.Message;
import io.iqe.wasp.WaspOutputStream;

import java.io.Closeable;
import java.io.IOException;

public class MessageOutputStream implements Closeable {
    private int streamId;
    private WaspOutputStream output;
    private FeatureRepository features;

    private MultiSignByteBuffer buffer;

    public MessageOutputStream(int streamId, WaspOutputStream output, FeatureRepository features, int maxMessageSize) {
        this.streamId = streamId;
        this.output = output;
        this.features = features;

        this.buffer = MultiSignByteBuffer.allocate(maxMessageSize);
    }

    public int getStreamId() {
        return streamId;
    }

    public void setStreamId(int streamId) {
        this.streamId = streamId;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void writeMessage(Message message) throws IOException {
        if (streamId != message.getStreamId()) {
            throw new IllegalArgumentException(
                    String.format("Stream '%s' can't handle message %s", streamId, message));
        }

        Feature feature = features.getFeature(message.getFeatureId());
        if (feature == null) {
            throw new IllegalArgumentException(
                    String.format("No feature configured in stream '%s' for message %s", streamId, message));
        }

        buffer.rewind();
        buffer.putUnsignedShort(message.getFeatureId());
        feature.writeMessageContent(message.getContent(), buffer);

        output.writeMessage(buffer.array(), 0, buffer.position());
    }

    @Override
    public void close() throws IOException {
        output.close();
    }
}
