package lib.datatypes.variables;

public class IntegerVariable extends Variable
{
	public IntegerVariable(String name) 
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
		if(obj instanceof Integer)
			data = (Integer)obj;
	}
	
	public String toString()
	{
		return data.toString();
	}

	private Integer data;
}
