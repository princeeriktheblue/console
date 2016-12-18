package lib.datatypes.variables;

public class StringVariable extends Variable
{
	public StringVariable(String name) 
	{
		super(name, false);
	}

	@Override
	public Object getData() 
	{
		return data;
	}

	@Override
	public void setData(Object obj) 
	{
		if(obj instanceof String)
			data = (String)obj;
	}
	
	public String toString()
	{
		return name + '"' + data + '"';
	}

	private String data;
}
