package io.iqe.rimini.config;

import java.util.Set;

public class ReadFeatureResponse extends AbstractConfigAction {
    private int featureId;
    private Set<Integer> usedPins;
    private Object configuration;

    public ReadFeatureResponse(int featureId, Set<Integer> usedPins, Object configuration) {
        super(ActionTypes.CONFIG_RSP_READ);
        this.featureId = featureId;
        this.usedPins = usedPins;
        this.configuration = configuration;
    }

    public int getFeatureId() {
        return featureId;
    }

    public Set<Integer> getUsedPins() {
        return usedPins;
    }

    public Object getConfiguration() {
        return configuration;
    }

    @Override
    public String toString() {
        return String.format("%s: Feature %d, pins %s, config [%s]",
                super.toString(), featureId, usedPins, configuration);
    }
}
