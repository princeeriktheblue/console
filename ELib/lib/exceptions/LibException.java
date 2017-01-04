package lib.exceptions;

public class LibException extends Exception 
{
	private static final long serialVersionUID = 1L;

	public LibException()
	{}
	
	public LibException(String x)
	{
		super(x);
	}
}
