package commands.exceptions;

public class InvalidCommandException extends EException 
{
	private static final long serialVersionUID = 1L;

	public InvalidCommandException()
	{}
	
	public InvalidCommandException(String x)
	{
		super(x);
	}
}
