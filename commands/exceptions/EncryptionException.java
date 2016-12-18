package commands.exceptions;

public class EncryptionException extends EException 
{
	private static final long serialVersionUID = 1L;

	public EncryptionException()
	{}
	
	public EncryptionException(String x)
	{
		super(x);
	}
}
