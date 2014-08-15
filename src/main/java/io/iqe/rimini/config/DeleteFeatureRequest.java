package io.iqe.rimini.config;

public class DeleteFeatureRequest extends AbstractConfigAction {
    private int featureId;

    public DeleteFeatureRequest(int featureId) {
        super(ActionTypes.CONFIG_REQ_DELETE);
        this.featureId = featureId;
    }

    public int getFeatureId() {
        return featureId;
    }
}
