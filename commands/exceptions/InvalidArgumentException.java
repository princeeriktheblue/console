package commands.exceptions;

public class InvalidArgumentException extends InvalidCommandException
{
	private static final long serialVersionUID = 1L;

	public InvalidArgumentException()
	{}
	
	public InvalidArgumentException(String x)
	{
		super(x);
	}
}
