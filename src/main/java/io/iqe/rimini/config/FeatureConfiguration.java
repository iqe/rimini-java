package io.iqe.rimini.config;

import io.iqe.rimini.FeatureType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FeatureConfiguration {
    private FeatureType featureType;
    private List<Integer> pins;

    protected FeatureConfiguration(FeatureType featureType, Collection<Integer> pins) {
        this.featureType = featureType;
        this.pins = new ArrayList<>(pins);
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public List<Integer> getPins() {
        return pins;
    }
}
