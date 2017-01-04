package engine.lang.exceptions;

public class InvalidAccessException extends EngineException 
{
	private static final long serialVersionUID = 1L;

	public InvalidAccessException()
	{}
	
	public InvalidAccessException(String x)
	{
		super(x);
	}
}
