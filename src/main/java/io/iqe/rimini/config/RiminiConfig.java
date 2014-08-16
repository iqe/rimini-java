package io.iqe.rimini.config;

import io.iqe.nio.MultiSignByteBuffer;
import io.iqe.rimini.AbstractFeature;
import io.iqe.rimini.Feature;
import io.iqe.rimini.FeatureRepository;

import java.util.HashSet;
import java.util.Set;

public class RiminiConfig extends AbstractFeature<AbstractConfigAction> {
    private FeatureRepository features;

    public RiminiConfig(FeatureRepository features) {
        this.features = features;
    }

    @Override
    public void writeMessageContent(AbstractConfigAction content, MultiSignByteBuffer buf) {
        int actionId = content.getActionId();

        buf.putUnsigned(actionId);

        switch (actionId) {
        case ActionTypes.CONFIG_REQ_VERSION:
        case ActionTypes.CONFIG_REQ_FEATURES:
            break; // Nothing more to do
        case ActionTypes.CONFIG_REQ_READ:
            buf.putShort((short)((ReadFeatureRequest)content).getFeatureId());
            break;
        case ActionTypes.CONFIG_REQ_DELETE:
            buf.putShort((short)((DeleteFeatureRequest)content).getFeatureId());
            break;
        default:
            throw new UnknownConfigActionException(actionId);
        }
    }

    @Override
    public AbstractConfigAction readMessageContent(MultiSignByteBuffer buf) {
        int actionId = buf.getUnsigned();

        switch (actionId) {
        case ActionTypes.CONFIG_RSP_VERSION:
            return createVersionResponse(buf);
        case ActionTypes.CONFIG_RSP_FEATURES:
            return createFeaturesResponse(buf);
        case ActionTypes.CONFIG_RSP_READ:
            return createReadFeatureResponse(buf);
        case ActionTypes.CONFIG_RSP_DELETE:
            return createDeleteFeatureResponse(buf);
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

    private AbstractConfigAction createReadFeatureResponse(MultiSignByteBuffer buf) {
        int featureId = buf.getShort();
        int errorCode = buf.getShort();

        if (errorCode != 0) {
            throw new IllegalStateException(
                    String.format("Failed to read feature %d - error %d", featureId, errorCode));
        }

        Feature<?> feature = features.getFeature(featureId);
        if (feature == null) {
            throw new IllegalStateException(); // FIXME throw UnknownFeatureEx or similar
        }
        Object configuration = feature.readConfiguration(buf);

        return new ReadFeatureResponse(featureId, configuration);
    }

    private AbstractConfigAction createDeleteFeatureResponse(MultiSignByteBuffer buf) {
        int errorCode = buf.getShort();

        return new DeleteFeatureResponse(errorCode);
    }
}
