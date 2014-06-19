package io.iqe.rimini.io.test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class QueueOutputStream extends OutputStream {
	private boolean closed;
	private BlockingQueue<Integer> bytes;

	public QueueOutputStream() {
		this(new LinkedBlockingQueue<Integer>());
	}

	public QueueOutputStream(BlockingQueue<Integer> bytes) {
		this.bytes = bytes;
	}

	@Override
	public void write(int b) throws IOException {
	    if ((byte)b != b) {
            throw new IOException(String.format("%d is not a valid byte value", b));
        }

		try {
            bytes.put(b);
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
	}

	@Override
	public void close() throws IOException {
		super.close();
		closed = true;
	}

    public byte[] readAll() throws IOException {
        // Note: This breaks if multiple threads read at the same time!
        byte[] b = new byte[bytes.size()];
        for (int i = 0; i < b.length; i++) {
            b[i] = (byte)read();
        }
        return b;
    }

    public int read() throws IOException {
        return read(0);
    }

    public int read(long timeoutMillis) throws IOException {
        try {
            Integer b = bytes.poll(timeoutMillis, TimeUnit.MILLISECONDS);
            if (b == null) {
                throw new IOException(String.format("Timed out after %dms", timeoutMillis));
            }
            return b;
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    public void clear() {
        bytes.clear();
    }

	public boolean isClosed() {
		return closed;
	}
}
