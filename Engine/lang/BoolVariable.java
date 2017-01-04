package engine.lang;

import engine.Engine;
import engine.lang.exceptions.InvalidCastException;
import engine.lang.exceptions.InvalidComparisonException;

public class BoolVariable extends PrimitiveVariable
{
	public BoolVariable(String name) 
	{
		super(name);
	}

	@Override
	public String toString()
	{
		return data.toString();
	}

	@Override
	public Object getData() 
	{
		return data;
	}

	@Override
	public void setData(Object obj) 
	{
		if(obj instanceof Boolean)
			data = (Boolean)obj;
	}
	
	@Override
	public int compareTo(Variable v) throws InvalidComparisonException 
	{
		if(v instanceof BoolVariable)
			return ((BoolVariable)v).getData().equals(data) ? Engine.EQUAL : Engine.NOTEQUAL;
		else if(v instanceof IntegerVariable)
		{
			boolean temp = false;
			Integer in = (Integer)((IntegerVariable)v).getData();
			if(in == 1)
				temp = true;
			else if(in == 0)
				temp = false;
			else
				throw new InvalidComparisonException();
			return temp == data ? Engine.EQUAL : Engine.NOTEQUAL;
		}
		else if(v instanceof DoubleVariable)
		{
			boolean temp = false;
			Double d = (Double)((DoubleVariable)v).getData();
			if(d == 1)
				temp = true;
			else if(d == 0)
				temp = false;
			else
				throw new InvalidComparisonException();
			return temp == data ? Engine.EQUAL : Engine.NOTEQUAL;
		}
		else if(v instanceof StringVariable)
		{
			String str = (String)((StringVariable)v).getData();
			if((str.equals("true") && data)  || str.equals("false") && !data)
				return Engine.EQUAL;
			else if((str.equals("false") && data) || str.equals("true") && !data)
				return Engine.NOTEQUAL;
			else
				throw new InvalidComparisonException();
		}
		else
			throw new InvalidComparisonException();
	}
	
	public BoolVariable cast(Variable v) throws InvalidCastException 
	{
		boolean temp = false;
		if(v instanceof BoolVariable)
			return (BoolVariable)v;
		else if(v instanceof IntegerVariable)
		{
			Integer in = (Integer)((IntegerVariable)v).getData();
			if(in == 1)
				temp = true;
			else if(in != 0)
				throw new InvalidCastException();
		}
		else if(v instanceof DoubleVariable)
		{
			Double d = (Double)((DoubleVariable)v).getData();
			if(d == 1)
				temp = true;
			else if(d != 0)
				throw new InvalidCastException();
			
		}
		else if(v instanceof StringVariable)
		{
			String str = (String)((StringVariable)v).getData();
			if(str.equals("true"))
				temp = true;
			else if(!str.equals("false"))
				throw new InvalidCastException();
		}
		else
			throw new InvalidCastException();
		BoolVariable var = new BoolVariable(v.name);
		var.setData(temp);
		return var;
	}
	
	private Boolean data;
}