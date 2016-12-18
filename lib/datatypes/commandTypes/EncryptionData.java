package lib.datatypes.commandTypes;

public final class EncryptionData
{
	public EncryptionData(String password, byte[] data, byte[] salt, byte[] iv)
	{
		this.password = password;
		this.data = data;
		this.salt = salt;
		this.iv = iv;
	}
	
	public String getPass()
	{
		return password;
	}
	
	public byte[] getData()
	{
		return data;
	}
	
	public byte[] getSalt()
	{
		return salt;
	}
	
	public byte[] getIV()
	{
		return iv;
	}
	
	private final String password;
	private final byte[] data;
	private final byte[] salt;
	private final byte[] iv;
}