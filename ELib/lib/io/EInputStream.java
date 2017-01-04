package lib.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public abstract class EInputStream extends InputStream implements Closeable
{
	public abstract boolean ready() throws IOException;
	
	@Override
	public abstract int read() throws IOException;
	public abstract String readLine() throws IOException;
	
	public abstract void open() throws IOException;
	public abstract void close() throws IOException;
}
