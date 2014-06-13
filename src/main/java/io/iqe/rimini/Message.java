package io.iqe.rimini;

public class Message {
    private int streamId;
    private int featureId;
    private Object content;

    public Message(int streamId, int featureId, Object content) {
        this.streamId = streamId;
        this.featureId = featureId;
        this.content = content;
    }

    public int getStreamId() {
        return streamId;
    }

    public int getFeatureId() {
        return featureId;
    }

    public Object getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.format("S%d.F%d: '%s'", streamId, featureId, content);
    }
}
