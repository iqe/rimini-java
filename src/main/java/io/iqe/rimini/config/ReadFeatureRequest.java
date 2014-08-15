package io.iqe.rimini.config;

public class ReadFeatureRequest extends AbstractConfigAction {
    private int featureId;

    public ReadFeatureRequest(int featureId) {
        super(ActionTypes.CONFIG_REQ_READ);
        this.featureId = featureId;
    }

    public int getFeatureId() {
        return featureId;
    }
}
