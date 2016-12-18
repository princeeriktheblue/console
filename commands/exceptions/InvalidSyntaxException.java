package commands.exceptions;

public class InvalidSyntaxException extends InvalidCommandException 
{
	private static final long serialVersionUID = 1L;
	
	public final static int SHOW_ALL = -2;
	
	public InvalidSyntaxException()
	{
		index = -1;
	}
	
	public InvalidSyntaxException(String x)
	{
		super(x);
		index = -1;
	}
	
	public InvalidSyntaxException(String x, int index)
	{
		super(x);
		this.index = index;
	}
	
	public final int getIndex()
	{
		return index;
	}
	
	private final int index;
}
