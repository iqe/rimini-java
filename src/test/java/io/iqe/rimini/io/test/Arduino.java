package io.iqe.rimini.io.test;

import static io.iqe.rimini.io.test.MessageStreamTestSupport.*;

import java.io.IOException;

import io.iqe.rimini.Feature;
import io.iqe.rimini.FeatureRepository;
import io.iqe.rimini.io.MessageStream;
import io.iqe.wasp.WaspInputStream;
import io.iqe.wasp.WaspOutputStream;

public class Arduino {
    private QueueInputStream input;
    private QueueOutputStream output;
    private FeatureRepository features;
    private MessageStream stream;

    public Arduino(int streamId) {
        input = new QueueInputStream();
        output = new QueueOutputStream();

        features = new FeatureRepository();
        stream = new MessageStream(streamId, new WaspInputStream(input), new WaspOutputStream(output), features);
    }

    public FeatureRepository getFeatures() {
        return features;
    }

    public MessageStream getStream() {
        return stream;
    }

    public void addFeature(int featureId, Feature<?> feature) {
        features.addFeature(featureId, feature);
    }

    public void sendMessage(int... bytes) throws IOException {
        input.writeAll(message(bytes));
    }
}
