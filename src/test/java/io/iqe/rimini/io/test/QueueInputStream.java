package io.iqe.rimini.io.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueInputStream extends InputStream {
	private boolean closed;
	private BlockingQueue<Integer> bytes;

	public QueueInputStream() {
		this(new LinkedBlockingQueue<Integer>());
	}

	public QueueInputStream(BlockingQueue<Integer> bytes) {
		this.bytes = bytes;
	}

	@Override
	public int read() throws IOException {
        try {
            return bytes.take();
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
	}

	@Override
	public void close() throws IOException {
	    super.close();
	    try {
            bytes.put(-1);
        } catch (InterruptedException e) {
            throw new IOException(e);
        } finally {
            closed = true;
        }
	}

	public void writeAll(byte[] b) throws IOException {
	    for (int i = 0; i < b.length; i++) {
            write(b[i]);
        }
	}

    public void write(int b) throws IOException {
        if ((byte)b != b) {
            throw new IOException(String.format("%d is not a valid byte value", b));
        }
        bytes.add(b);
    }

    public void clear() {
        bytes.clear();
    }

	public boolean isClosed() {
		return closed;
	}
}
