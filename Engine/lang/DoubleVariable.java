package engine.lang;

import engine.Engine;
import engine.lang.exceptions.InvalidCastException;
import engine.lang.exceptions.InvalidComparisonException;

public class DoubleVariable extends PrimitiveVariable
{
	public DoubleVariable(String name) 
	{
		super(name);
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
	
	@Override
	public int compareTo(Variable v) throws InvalidComparisonException
	{
		if(v instanceof DoubleVariable)
			return compareNumbers((Double)((DoubleVariable)v).getData());
		else if(v instanceof IntegerVariable)
			return compareNumbers((Integer)((IntegerVariable)v).getData());
		else if(v instanceof BoolVariable)
		{
			boolean b = (Boolean)((BoolVariable)v).getData();
			return compareNumbers(b ? 1 : 0);
		}
		else if(v instanceof StringVariable)
		{
			try
			{
				Double temp = Double.parseDouble((String)((StringVariable)v).getData());
				if(data == temp)
					return Engine.EQUAL;
				else
					return data > temp ? Engine.GREATERTHAN : Engine.LESSTHAN;
			}
			catch(NumberFormatException e)
			{
				throw new InvalidComparisonException();
			}
		}
		else
			throw new InvalidComparisonException();
	}
	
	private final int compareNumbers(double d)
	{
		if(data == d)
			return Engine.EQUAL;
		else
			return data > d ? Engine.GREATERTHAN : Engine.LESSTHAN;
	}
	
	public DoubleVariable cast(Variable v) throws InvalidCastException 
	{
		double temp = 0;
		if(v instanceof DoubleVariable)
			return (DoubleVariable)v;
		else if(v instanceof BoolVariable)
		{			
			Boolean b = (Boolean)((BoolVariable)v).getData();
			if(b)
				temp = 1;
		}
		else if(v instanceof IntegerVariable)
		{
			Integer in = (Integer)((IntegerVariable)v).getData();
			temp = in;
		}
		else if(v instanceof StringVariable)
		{
			String str = (String)((StringVariable)v).getData();
			try
			{
				temp = Double.parseDouble(str);
			}
			catch(NumberFormatException e)
			{
				throw new InvalidCastException();
			}
		}
		DoubleVariable d = new DoubleVariable(v.name);
		d.setData(temp);
		return d;
	}
	
	private Double data;
}
