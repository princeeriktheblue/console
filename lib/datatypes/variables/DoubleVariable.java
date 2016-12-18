package lib.datatypes.variables;

public class DoubleVariable extends Variable
{
	public DoubleVariable(String name) 
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
		if(obj instanceof Double)
			data = (Double)obj;
	}
	
	public String toString()
	{
		return data.toString();
	}

	private Double data;
}
