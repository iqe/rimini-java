package io.iqe.rimini;

public class Address {
    private int streamId;
    private int featureId;

    public Address(int streamId, int featureId) {
        this.streamId = streamId;
        this.featureId = featureId;
    }

    public int getStreamId() {
        return streamId;
    }

    public int getFeatureId() {
        return featureId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + featureId;
        result = prime * result + streamId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Address other = (Address) obj;
        return featureId == other.featureId && streamId == other.streamId;
    }

    @Override
    public String toString() {
        return String.format("S%d.F%d", streamId, featureId);
    }
}
