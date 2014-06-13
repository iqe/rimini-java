package io.iqe.rimini.io.test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueOutputStream extends OutputStream {
	private boolean closed;
	private Queue<Byte> bytes;

	public QueueOutputStream() {
		this(new LinkedBlockingQueue<Byte>());
	}

	public QueueOutputStream(Queue<Byte> bytes) {
		this.bytes = bytes;
	}

	@Override
	public void write(int b) throws IOException {
		bytes.add((byte)b);
	}

	@Override
	public void close() throws IOException {
		super.close();
		closed = true;
	}

    public byte[] readAll() {
        byte[] b = new byte[bytes.size()];
        for (int i = 0; i < b.length; i++) {
            b[i] = bytes.poll();
        }
        return b;
    }

    public int read() {
        Byte b = bytes.poll();
        return b == null ? -1 : b & 0xFF;
    }

    public void clear() {
        bytes.clear();
    }

	public boolean isClosed() {
		return closed;
	}
}
