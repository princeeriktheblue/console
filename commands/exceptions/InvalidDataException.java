package commands.exceptions;

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
