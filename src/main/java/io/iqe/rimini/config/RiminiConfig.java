package io.iqe.rimini.config;

import io.iqe.nio.MultiSignByteBuffer;
import io.iqe.rimini.AbstractFeature;
import io.iqe.rimini.FeatureRepository;

public class RiminiConfig extends AbstractFeature<AbstractConfigAction> {
    private FeatureRepository features;

    public RiminiConfig(FeatureRepository features) {
        this.features = features;
    }

    @Override
    public void writeMessageContent(AbstractConfigAction content, MultiSignByteBuffer buf) {
        buf.putUnsigned(content.getActionId());

        switch (content.getActionId()) {
        case ActionTypes.CONFIG_REQ_VERSION:
            // nothing more to do
            break;
        default:
            throw new RuntimeException(); // TODO
        }
    }

    @Override
    public AbstractConfigAction readMessageContent(MultiSignByteBuffer buf) {
        int actionId = buf.getUnsigned();

        switch (actionId) {
        case ActionTypes.CONFIG_RSP_VERSION:
            int major = buf.getUnsigned();
            int minor = buf.getUnsigned();
            int patch = buf.getUnsigned();

            return new VersionResponse(major, minor, patch);
        default:
            throw new RuntimeException(); // TODO
        }
    }
}
