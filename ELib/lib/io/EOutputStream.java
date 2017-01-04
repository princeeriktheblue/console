package lib.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public abstract class EOutputStream extends OutputStream implements Closeable
{

	@Override
	public abstract void write(int arg0) throws IOException;
	public abstract void write(String ln) throws IOException;
	public abstract void write(char ch) throws IOException;
	public abstract void write(byte by) throws IOException;
	public abstract void write(long l) throws IOException;
	public abstract void write(double d) throws IOException;
	public abstract void write(float f) throws IOException;
	
	public abstract void append(int arg0, int start, int end) throws IOException;
	public abstract void append(String ln, int start, int end) throws IOException;
	public abstract void append(char ch, int start, int end) throws IOException;
	public abstract void append(byte by, int start, int end) throws IOException;
	public abstract void append(long l, int start, int end) throws IOException;
	public abstract void append(double d, int start, int end) throws IOException;
	public abstract void append(float f, int start, int end) throws IOException;
	
	public abstract void open() throws IOException;
	public abstract void close() throws IOException;
}
