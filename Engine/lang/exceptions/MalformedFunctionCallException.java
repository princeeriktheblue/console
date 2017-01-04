package engine.lang.exceptions;

public class MalformedFunctionCallException extends EngineException
{
	private static final long serialVersionUID = 1L;

	public MalformedFunctionCallException()
	{}
	
	public MalformedFunctionCallException(String x)
	{
		super(x);
	}

}
