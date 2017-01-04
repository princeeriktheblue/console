package engine.lang.exceptions;

public class InvalidComparisonException extends EngineException
{
	private static final long serialVersionUID = 1L;

	public InvalidComparisonException()
	{}
	
	public InvalidComparisonException(String x)
	{
		super(x);
	}
}
