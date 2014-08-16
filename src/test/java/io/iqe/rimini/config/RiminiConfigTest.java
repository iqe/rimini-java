package io.iqe.rimini.config;

import static org.junit.Assert.*;
import io.iqe.rimini.FeatureRepository;
import io.iqe.rimini.io.test.SimpleTextFeature;
import io.iqe.rimini.test.AbstractFeatureTest;

import org.junit.Before;
import org.junit.Test;

public class RiminiConfigTest extends AbstractFeatureTest {
    private FeatureRepository features;
    private RiminiConfig cfg;

    @Before
    public void setUp() throws Exception {
        features = new FeatureRepository();
        cfg = new RiminiConfig(features);

        features.addFeature(258, new SimpleTextFeature());
    }

    @Test
    public void shouldWriteFeaturesRequest() throws Exception {
        FeaturesRequest request = new FeaturesRequest();

        cfg.writeMessageContent(request, buf);

        assertWrittenBytes(ActionTypes.CONFIG_REQ_FEATURES);
    }

    @Test
    public void shouldWriteVersionRequest() throws Exception {
        VersionRequest request = new VersionRequest();

        cfg.writeMessageContent(request, buf);

        assertWrittenBytes(ActionTypes.CONFIG_REQ_VERSION);
    }

    @Test
    public void shouldWriteReadFeatureRequest() throws Exception {
        ReadFeatureRequest request = new ReadFeatureRequest(258);

        cfg.writeMessageContent(request, buf);

        assertWrittenBytes(ActionTypes.CONFIG_REQ_READ, 1, 2);
    }

    @Test
    public void shouldWriteDeleteFeatureRequest() throws Exception {
        DeleteFeatureRequest request = new DeleteFeatureRequest(5);

        cfg.writeMessageContent(request, buf);

        assertWrittenBytes(ActionTypes.CONFIG_REQ_DELETE, 0, 5);
    }

    @Test(expected = UnknownConfigActionException.class)
    public void shouldThrownOnUnknownActionIdWhenWriting() throws Exception {
        AbstractConfigAction content = new AbstractConfigAction(255) {};
        cfg.writeMessageContent(content, buf);
    }

    @Test
    public void shouldReadFeaturesResponse() throws Exception {
        buffer(ActionTypes.CONFIG_RSP_FEATURES, 0, 2, 0, 1, 1, 1);

        FeaturesResponse response = readMessageContent(FeaturesResponse.class);

        assertEquals(set(1, 257), response.getFeatureIds());
    }

    @Test
    public void sholdReadVersionResponse() throws Exception {
        buffer(ActionTypes.CONFIG_RSP_VERSION, 1, 2, 3);

        VersionResponse response = readMessageContent(VersionResponse.class);

        assertEquals("1.2.3", response.getVersion());
    }

    @Test
    public void shouldReadReadFeatureResponse() throws Exception {
        buffer(ActionTypes.CONFIG_RSP_READ, 1, 2, 0, 0, 0, 5, 'H', 'e', 'l', 'l', 'o');

        ReadFeatureResponse response = readMessageContent(ReadFeatureResponse.class);

        assertEquals(258, response.getFeatureId());
        assertEquals(258, response.getFeatureId());
        assertEquals("Hello", response.getConfiguration());
    }

    @Test
    public void shouldThrowOnErrorCodeInReadFeatureResponse() throws Exception {
        buffer(ActionTypes.CONFIG_RSP_READ, 1, 2, 0xFF, 0xF6);

        try {
            readMessageContent(ReadFeatureResponse.class);

            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("-10"));
        }
    }

    @Test
    public void shouldReadDeleteFeatureResponse() throws Exception {
        buffer(ActionTypes.CONFIG_RSP_DELETE, 0, 42);

        DeleteFeatureResponse response = readMessageContent(DeleteFeatureResponse.class);

        assertEquals(42, response.getErrorCode());
    }

    @Test(expected = UnknownConfigActionException.class)
    public void shouldThrownOnUnknownActionIdWhenReading() throws Exception {
        buffer(255);
        cfg.readMessageContent(buf);
    }

    @SuppressWarnings("unchecked")
    private <T extends AbstractConfigAction> T readMessageContent(Class<T> expectedResponseClass) {
        AbstractConfigAction content = cfg.readMessageContent(buf);

        assertEquals(expectedResponseClass, content.getClass());

        return (T)content;
    }
}
