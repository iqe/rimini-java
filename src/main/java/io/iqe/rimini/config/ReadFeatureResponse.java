package io.iqe.rimini.config;

import java.util.Set;

public class ReadFeatureResponse extends AbstractConfigAction {
    private int featureId;
    private Object configuration;

    public ReadFeatureResponse(int featureId, Object configuration) {
        super(ActionTypes.CONFIG_RSP_READ);
        this.featureId = featureId;
        this.configuration = configuration;
    }

    public int getFeatureId() {
        return featureId;
    }
    public Object getConfiguration() {
        return configuration;
    }

    @Override
    public String toString() {
        return String.format("%s: Feature %d, config [%s]",
                super.toString(), featureId, configuration);
    }
}
