package commands.exceptions;

public class CommandErrorException extends EException
{
	private static final long serialVersionUID = 1L;

	public CommandErrorException()
	{}
	
	public CommandErrorException(String x)
	{
		super(x);
	}

}
