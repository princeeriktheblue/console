package console.commands.exceptions;

import console.exceptions.EException;

public class UnknownErrorException extends EException 
{
	private static final long serialVersionUID = 1L;

	public UnknownErrorException()
	{}
	
	public UnknownErrorException(String x)
	{
		super(x);
	}
}
