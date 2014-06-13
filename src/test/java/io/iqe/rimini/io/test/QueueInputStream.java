package io.iqe.rimini.io.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueInputStream extends InputStream {
	private boolean closed;
	private Queue<Byte> bytes;

	public QueueInputStream() {
		this(new LinkedBlockingQueue<Byte>());
	}

	public QueueInputStream(Queue<Byte> bytes) {
		this.bytes = bytes;
	}

	@Override
	public int read() throws IOException {
		Byte b = bytes.poll();
		return b == null ? -1 : b & 0xFF;
	}

	@Override
	public void close() throws IOException {
		super.close();
		closed = true;
	}

	public void writeAll(byte[] b) {
	    for (int i = 0; i < b.length; i++) {
            write(b[i]);
        }
	}

    public void write(int b) {
        bytes.add((byte)b);
    }

    public void clear() {
        bytes.clear();
    }

	public boolean isClosed() {
		return closed;
	}
}
