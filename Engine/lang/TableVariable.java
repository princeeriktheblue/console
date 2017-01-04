package engine.lang;

import engine.Engine;
import engine.lang.exceptions.InvalidComparisonException;
import engine.lang.exceptions.VariableFormatException;
import lib.datatypes.EList;
import lib.exceptions.InvalidDataException;

public class TableVariable<E> extends Variable
{
	public TableVariable(String name) 
	{
		super(name, true);
	}

	public String toString()
	{
		String toReturn = "{";
		for(E e: data)
			toReturn = toReturn + e.toString() + ',';
		toReturn = toReturn.substring(0, toReturn.length()-1) + '}';
		return toReturn;
	}
	
	@Override
	public int compareTo(Variable v) throws InvalidComparisonException 
	{
		if(v instanceof TableVariable)
		{
			@SuppressWarnings("unchecked")
			EList<?> temp = (EList<?>)((TableVariable<E>)v).data;
			int size = temp.size();
			if(data.size() == size)
				return Engine.EQUAL;
			else
				return data.size() < size ? Engine.GREATERTHAN : Engine.LESSTHAN;
		}
		else
			throw new InvalidComparisonException();
	}
	
	public static synchronized EList<Variable> deriveElements(String str) throws InvalidDataException
	{
		EList<String> tmp = EList.makeListFromString(str);
		EList<Variable> toReturn = new EList<>();
		for(String e: tmp)
		{
			try 
			{
				toReturn.add(PrimitiveVariable.makeVariable(null, e));
			} 
			catch (VariableFormatException e1) 
			{
				throw new InvalidDataException(e1.getMessage());
			}
		}
		return toReturn;
	}
	
	public synchronized void setData(EList<E> newList)
	{
		data.clear();
		for(E e : newList)
			data.add(e);
	}
	
	private EList<E> data;
}
