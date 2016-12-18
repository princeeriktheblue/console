package lib.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import lib.datatypes.EList;

public class EFileReader extends EInputStream
{
	public EFileReader(File toRead)
	{
		target = toRead;
	}

	@Override
	public boolean ready() throws IOException 
	{
		checkOpen();
		return reader.ready();
	}

	@Override
	public int read() throws IOException 
	{
		checkOpen();
		return reader.read();
	}

	@Override
	public String readLine() throws IOException 
	{
		checkOpen();
		return reader.readLine();
	}
	
	public static EList<String> readAllLines(Path path) throws IOException
	{
		EList<String> str = new EList<>();
		File f = path.toFile();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		while(reader.ready())
			str.add(reader.readLine());
		reader.close();
		return str;
	}

	@Override
	public void open() throws IOException 
	{
		reader = new BufferedReader(new FileReader(target));
		open = true;
	}
	
	public void open(File file) throws IOException
	{
		reader = new BufferedReader(new FileReader(target));
		open = true;
	}

	@Override
	public void close() throws IOException 
	{
		checkOpen();
		reader.close();
		open = false;
	}
	
	private void checkOpen() throws UnopenedObjectException
	{
		if(!open)
			throw new UnopenedObjectException();
	}
	
	private boolean open;
	private File target;
	private BufferedReader reader;
}