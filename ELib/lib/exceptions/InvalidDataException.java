package lib.exceptions;

public class InvalidDataException extends LibException 
{
	private static final long serialVersionUID = 1L;

	public InvalidDataException()
	{}
	
	public InvalidDataException(String x)
	{
		super(x);
	}

}
