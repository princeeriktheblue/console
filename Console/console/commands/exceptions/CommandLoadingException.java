package console.commands.exceptions;

public class CommandLoadingException extends CommandErrorException 
{
	private static final long serialVersionUID = 1L;

	public CommandLoadingException()
	{}
	
	public CommandLoadingException(String x)
	{
		super(x);
	}
}
