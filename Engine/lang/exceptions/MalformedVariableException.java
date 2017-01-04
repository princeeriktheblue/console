package engine.lang.exceptions;

public class MalformedVariableException extends EngineException
{
	private static final long serialVersionUID = 1L;

	public MalformedVariableException()
	{}
	
	public MalformedVariableException(String x)
	{
		super(x);
	}
}
