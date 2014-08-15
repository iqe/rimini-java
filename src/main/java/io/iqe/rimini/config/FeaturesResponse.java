package io.iqe.rimini.config;

import java.util.Set;

public class FeaturesResponse extends AbstractConfigAction {
    private Set<Integer> featureIds;

    public FeaturesResponse(Set<Integer> featureIds) {
        super(ActionTypes.CONFIG_RSP_FEATURES);
        this.featureIds = featureIds;
    }

    public Set<Integer> getFeatureIds() {
        return featureIds;
    }
}
