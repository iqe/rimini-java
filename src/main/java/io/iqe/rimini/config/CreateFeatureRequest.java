package io.iqe.rimini.config;

import io.iqe.rimini.FeatureType;

public class CreateFeatureRequest extends AbstractConfigAction {
    private int featureId;
    private FeatureType featureType;
    private FeatureConfiguration featureConfig;

    public CreateFeatureRequest(int featureId, FeatureType featureType, FeatureConfiguration featureConfig) {
        super(ActionTypes.CONFIG_REQ_CREATE);
        this.featureId = featureId;
        this.featureType = featureType;
        this.featureConfig = featureConfig;
    }

    public int getFeatureId() {
        return featureId;
    }

    public FeatureType getFeatureType() {
        return featureType;
    }

    public FeatureConfiguration getFeatureConfig() {
        return featureConfig;
    }

    @Override
    public String toString() {
        return String.format("%s: Feature %d, type %s, config %s", super.toString(), featureId, featureType, featureConfig);
    }
}
