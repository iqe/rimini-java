package io.iqe.rimini.io.test;

import io.iqe.nio.MultiSignByteBuffer;
import io.iqe.wasp.WaspOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MessageStreamTestSupport {
    public static byte[] message(int... bytes) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (WaspOutputStream waspOut = new WaspOutputStream(out)) {
                byte[] contentBytes = buildByteArray(bytes);
                waspOut.writeMessage(contentBytes);
                return out.toByteArray();
            }
        }
    }

    public static byte[] buildByteArray(int... bytesAsInts) {
        byte[] bytes = new byte[bytesAsInts.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) bytesAsInts[i];
        }
        return bytes;
    }

    public static int[] buildIntArray(byte... bytes) {
        int[] bytesAsInts = new int[bytes.length];
        for (int i = 0; i < bytesAsInts.length; i++) {
            bytesAsInts[i] = bytes[i] & 0xFF;
        }
        return bytesAsInts;
    }

    public static String formatBytes(byte[] bytes) {
        return formatBytes(bytes, bytes.length);
    }

    public static String formatBytes(byte[] bytes, int byteCount) {
        MultiSignByteBuffer bb = MultiSignByteBuffer.wrap(bytes);
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < byteCount; i++) {
            b.append(toHex(bb.getUnsigned())).append(" ");
        }
        return b.toString();
    }

    /**
     * Formats unsigned byte as hex value.
     */
    public static String toHex(int b) {
        return String.format("0x%02X", b);
    }

    /**
     * Returns high byte of unsigned short value.
     */
    public static int usHigh(int value) {
        MultiSignByteBuffer bb = MultiSignByteBuffer.allocate(2);
        bb.putUnsignedShort(value);
        return bb.getUnsigned(0);
    }

    /**
     * Returns low byte of unsigned short value.
     */
    public static int usLow(int value) {
        MultiSignByteBuffer bb = MultiSignByteBuffer.allocate(2);
        bb.putUnsignedShort(value);
        return bb.getUnsigned(1);
    }

    /**
     * Returns high byte of signed short value.
     */
    public static int ssHigh(int value) {
        MultiSignByteBuffer bb = MultiSignByteBuffer.allocate(2);
        bb.putShort((short) value);
        return bb.getUnsigned(0);
    }

    /**
     * Returns low byte of signed short value.
     */
    public static int ssLow(int value) {
        MultiSignByteBuffer bb = MultiSignByteBuffer.allocate(2);
        bb.putShort((short) value);
        return bb.getUnsigned(1);
    }
}
