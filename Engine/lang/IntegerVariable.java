package engine.lang;

import engine.Engine;
import engine.lang.exceptions.InvalidCastException;
import engine.lang.exceptions.InvalidComparisonException;

public class IntegerVariable extends PrimitiveVariable
{
	public IntegerVariable(String name) 
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
		if(obj instanceof Integer)
			data = (Integer)obj;
	}
	
	public String toString()
	{
		return data.toString();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if(obj instanceof IntegerVariable)
		{
			if(((IntegerVariable)obj).getData().equals(data))
				return true;
		}
		else if(obj instanceof DoubleVariable)
		{
			try
			{
				Double temp = Double.parseDouble(data.toString());
				if(((DoubleVariable)obj).getData().equals(temp))
					return true;
			}
			catch(NumberFormatException e)
			{
				return false;
			}
		}
		return false;
	}
	
	@Override
	public int compareTo(Variable v) throws InvalidComparisonException 
	{
		if(v instanceof IntegerVariable)
			return compareNumbers((Integer)((IntegerVariable)v).getData());
		else if(v instanceof DoubleVariable)
			return compareNumbers((Double)((DoubleVariable)v).getData());
		else if(v instanceof BoolVariable)
		{
			boolean b = (Boolean)((BoolVariable)v).getData();
			return compareNumbers(b ? 1 : 0);
		}
		else if(v instanceof StringVariable)
		{
			String str = (String)((StringVariable)v).getData();
			try
			{
				Integer in = Integer.parseInt(str);
				return compareNumbers(in);
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
	
	private Integer data;

	@Override
	public IntegerVariable cast(Variable v) throws InvalidCastException 
	{
		int temp = 0;
		if(v instanceof IntegerVariable)
			return (IntegerVariable)v;
		else if(v instanceof BoolVariable)
		{
			boolean b = (Boolean)((BoolVariable)v).getData();
			if(b)
				temp = 1;
		}
		else if(v instanceof DoubleVariable)
		{
			double data = (Double)((DoubleVariable)v).getData();
			temp = (int)data;
		}
		else if(v instanceof StringVariable)
		{
			String str = (String)((StringVariable)v).getData();
			try
			{
				temp = Integer.parseInt(str);
			}
			catch(NumberFormatException e)
			{
				throw new InvalidCastException();
			}
		}
		else
			throw new InvalidCastException();
		IntegerVariable in = new IntegerVariable(v.name);
		in.setData(temp);
		return in;
	}
}
