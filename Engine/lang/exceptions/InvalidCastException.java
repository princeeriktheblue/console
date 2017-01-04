package engine.lang.exceptions;

public class InvalidCastException extends EngineException
{
	private static final long serialVersionUID = 1L;

	public InvalidCastException()
	{}
	
	public InvalidCastException(String x)
	{
		super(x);
	}
}
