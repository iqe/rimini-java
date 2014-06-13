package io.iqe.rimini;

import java.util.HashMap;
import java.util.Map;

public class FeatureRepository {
    private Map<Integer, Feature<?>> features;

    public FeatureRepository() {
        features = new HashMap<>();
    }

    // FIXME Should be thread-safe (?)
    public void addFeature(int featureId, Feature<?> feature) {
        features.put(featureId, feature);
    }

    public Feature<?> getFeature(int featureId) {
        return features.get(featureId);
    }
}
