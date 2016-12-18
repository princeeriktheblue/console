package lib.datatypes.variables;

import lib.datatypes.EList;

public class TableVariable extends Variable
{
	public TableVariable(String name) 
	{
		super(name, true);
	}

	@Override
	public Object getData() 
	{
		return data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setData(Object obj) 
	{
		if(obj instanceof EList)
			data = (EList<Variable>)obj;
	}

	public String toString()
	{
		String toReturn = "{";
		for(Variable e: data)
			toReturn = toReturn + e.toString() + ',';
		toReturn = toReturn.substring(0, toReturn.length()-1) + '}';
		return toReturn;
	}
	
	private EList<Variable> data;
}
