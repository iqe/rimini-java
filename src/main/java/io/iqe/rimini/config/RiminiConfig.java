package io.iqe.rimini.config;

import io.iqe.nio.MultiSignByteBuffer;
import io.iqe.rimini.AbstractFeature;

import java.util.HashSet;
import java.util.Set;

public class RiminiConfig extends AbstractFeature<AbstractConfigAction> {
    @Override
    public void writeMessageContent(AbstractConfigAction content, MultiSignByteBuffer buf) {
        int actionId = content.getActionId();

        buf.putUnsigned(actionId);

        switch (actionId) {
        case ActionTypes.CONFIG_REQ_FEATURES:
        case ActionTypes.CONFIG_REQ_VERSION:
            break; // Nothing more to do
        default:
            throw new UnknownConfigActionException(actionId);
        }
    }

    @Override
    public AbstractConfigAction readMessageContent(MultiSignByteBuffer buf) {
        int actionId = buf.getUnsigned();

        switch (actionId) {
        case ActionTypes.CONFIG_RSP_FEATURES:
            return createFeaturesResponse(buf);
        case ActionTypes.CONFIG_RSP_VERSION:
            return createVersionResponse(buf);
        default:
            throw new UnknownConfigActionException(actionId);
        }
    }

    private AbstractConfigAction createFeaturesResponse(MultiSignByteBuffer buf) {
        int featureCount = buf.getShort();

        Set<Integer> featureIds = new HashSet<>();
        for (int i = 0; i < featureCount; i++) {
            int featureId = buf.getShort();
            featureIds.add(featureId);
        }
        return new FeaturesResponse(featureIds);
    }

    private AbstractConfigAction createVersionResponse(MultiSignByteBuffer buf) {
        int major = buf.getUnsigned();
        int minor = buf.getUnsigned();
        int patch = buf.getUnsigned();

        return new VersionResponse(major, minor, patch);
    }
}
