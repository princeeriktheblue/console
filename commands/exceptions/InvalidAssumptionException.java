package commands.exceptions;

public class InvalidAssumptionException extends InvalidCommandException 
{
	private static final long serialVersionUID = 1L;

	public InvalidAssumptionException()
	{}
	
	public InvalidAssumptionException(String x)
	{
		super(x);
	}
}
