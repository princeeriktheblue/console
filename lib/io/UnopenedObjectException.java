package lib.io;

import java.io.IOException;

public class UnopenedObjectException extends IOException 
{
	private static final long serialVersionUID = 1L;

	public UnopenedObjectException()
	{}
	
	public UnopenedObjectException(String x)
	{
		super(x);
	}
}
