package console.commands.exceptions;

public class InvalidVariableException extends InvalidCommandException 
{
	private static final long serialVersionUID = 1L;

	public InvalidVariableException()
	{}
	
	public InvalidVariableException(String x)
	{
		super(x);
	}
}
