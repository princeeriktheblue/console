package engine.lang;

import engine.Engine;
import engine.lang.exceptions.InvalidCastException;
import engine.lang.exceptions.InvalidComparisonException;

public class StringVariable extends PrimitiveVariable
{
	public StringVariable(String name) 
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
		if(obj instanceof String)
			data = (String)obj;
	}
	
	public String toString()
	{
		return name + '"' + data + '"';
	}
	
	@Override
	public int compareTo(Variable v) throws InvalidComparisonException 
	{
		if(v instanceof StringVariable)
		{
			String temp = (String)((StringVariable)v).getData();
			int score = data.compareTo(temp);
			if(score == 0)
				return Engine.EQUAL;
			else
				return score > 0 ? Engine.GREATERTHAN : Engine.LESSTHAN;
		}
		else
			throw new InvalidComparisonException();
	}
	
	@Override
	public PrimitiveVariable cast(Variable v) throws InvalidCastException {
		// TODO Auto-generated method stub
		return null;
	}

	private String data;
}
