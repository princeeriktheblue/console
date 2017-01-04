package console.commands.exceptions;

import console.exceptions.EException;

public class InvalidDataException extends EException 
{
	private static final long serialVersionUID = 1L;

	public InvalidDataException()
	{}
	
	public InvalidDataException(String x)
	{
		super(x);
	}
}
