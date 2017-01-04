package engine;

import engine.lang.exceptions.EngineException;

public class MalformedFunctionException extends EngineException 
{
	private static final long serialVersionUID = 1L;

	public MalformedFunctionException()
	{}
	
	public MalformedFunctionException(String x)
	{
		super(x);
	}
}
