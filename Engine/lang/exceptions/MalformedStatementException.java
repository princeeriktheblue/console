package engine.lang.exceptions;

public class MalformedStatementException extends EngineException
{
	private static final long serialVersionUID = 1L;

	public MalformedStatementException()
	{}
	
	public MalformedStatementException(String x)
	{
		super(x);
	}
}
