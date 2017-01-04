package lib.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EFileWriter extends EOutputStream
{
	public EFileWriter(File target, boolean append)
	{
		this.target = target;
		this.append = append;
		open = false;
	}

	@Override
	public void write(int arg0) throws IOException 
	{
		checkOpen();
		writer.write(arg0);
	}

	@Override
	public void open() throws IOException 
	{
		writer = new FileWriter(target, append);
		open = true;
	}
	
	public void open(File file, boolean append) throws IOException
	{
		writer = new FileWriter(file, append);
		open = true;
	}

	@Override
	public void close() throws IOException 
	{
		writer.close();
		open = false;
	}

	@Override
	public void write(String ln) throws IOException 
	{
		checkOpen();
		writer.write(ln);
	}

	@Override
	public void write(char ch) throws IOException 
	{
		checkOpen();
		writer.write(ch);
	}

	@Override
	public void write(byte by) throws IOException 
	{
		checkOpen();
		writer.write(by);
	}

	@Override
	public void write(long l) throws IOException 
	{
		checkOpen();
		writer.write("" + l);
	}

	@Override
	public void write(double d) throws IOException 
	{
		checkOpen();
		writer.write("" + d);
	}

	@Override
	public void write(float f) throws IOException 
	{
		checkOpen();
		writer.write("" + f);
	}
	
	@Override
	public void append(int arg0, int start, int end) throws IOException 
	{
		checkOpen();
		writer.append("" + arg0, start, end);
	}

	@Override
	public void append(String ln, int start, int end) throws IOException 
	{
		checkOpen();
		writer.append(ln, start, end);
	}

	@Override
	public void append(char ch, int start, int end) throws IOException 
	{
		checkOpen();
		writer.append("" + ch, start, end);
	}

	@Override
	public void append(byte by, int start, int end) throws IOException 
	{
		checkOpen();
		writer.append("" + by, start, end);
	}

	@Override
	public void append(long l, int start, int end) throws IOException 
	{
		checkOpen();
		writer.append("" + l, start, end);
	}

	@Override
	public void append(double d, int start, int end) throws IOException 
	{
		checkOpen();
		writer.append("" + d, start, end);
	}

	@Override
	public void append(float f, int start, int end) throws IOException 
	{
		checkOpen();
		writer.append("" + f, start, end);
	}
	
	private void checkOpen() throws IOException
	{
		if(!open)
			throw new UnopenedObjectException();
	}
	
	private boolean open;
	private File target;
	private boolean append;
	private FileWriter writer;
}