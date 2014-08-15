package io.iqe.rimini.config;

import java.util.HashSet;
import java.util.Set;

import io.iqe.rimini.FeatureType;

public class FeatureConfiguration {
    private FeatureType featureType;
    private Set<Integer> usedPins;

    public FeatureConfiguration(FeatureType featureType) {
        this.featureType = featureType;

        usedPins = new HashSet<>();
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public Set<Integer> getUsedPins() {
        return usedPins;
    }

    protected final void setUsedPins(Set<Integer> usedPins) {
        this.usedPins = usedPins;
    }
}
