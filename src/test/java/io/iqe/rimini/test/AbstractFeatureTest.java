package io.iqe.rimini.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;

import io.iqe.nio.MultiSignByteBuffer;
import io.iqe.rimini.io.MessageStream;
import io.iqe.rimini.io.test.MessageStreamTestSupport;

public class AbstractFeatureTest {
    protected MultiSignByteBuffer buf;

    @Before
    public void createBuffer() {
        buf = MultiSignByteBuffer.allocate(MessageStream.DEFAULT_MAX_MESSAGE_SIZE);
    }

    protected void buffer(int... bytesAsInts) {
        buf.clear();
        byte[] bytes = MessageStreamTestSupport.buildByteArray(bytesAsInts);
        for (byte b : bytes) {
            buf.put(b);
        }
        buf.rewind();
    }

    @SuppressWarnings("unchecked")
    protected static <T> Set<T> set(T... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }

    protected void assertWrittenBytes(int... bytesAsInts) {
        byte[] expectedBytes = MessageStreamTestSupport.buildByteArray(bytesAsInts);
        byte[] actualBytes = new byte[expectedBytes.length];

        assertEquals(expectedBytes.length, buf.position());

        System.arraycopy(buf.array(), 0, actualBytes, 0, actualBytes.length);

        assertArrayEquals(expectedBytes, actualBytes);
    }
}
