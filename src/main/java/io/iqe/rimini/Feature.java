package io.iqe.rimini;

import io.iqe.nio.MultiSignByteBuffer;

public interface Feature<C> {
    /**
     * Writes the given {@code content} to {@code buffer}.
     */
    void writeMessageContent(C content, MultiSignByteBuffer buffer);

    /**
     * Parses the next bytes in {@code buffer} as message content.
     */
    C readMessageContent(MultiSignByteBuffer buffer);
}
