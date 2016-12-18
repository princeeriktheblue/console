package lib.datatypes.variables;

import commands.exceptions.InvalidDataException;
import commands.exceptions.VariableFormatException;
import lib.datatypes.EList;

public abstract class Variable
{
	public Variable(String name, boolean isTable)
	{
		this.name = name;
		this.isTable = isTable;
	}

	public final String getName() 
	{
		return name;
	}
	
	public final boolean isTable() 
	{
		return isTable;
	}
	
	public static synchronized Variable makeVariable(String name, String str) throws InvalidDataException, VariableFormatException
	{
		byte type = detectDataType(str);
		Variable var = null;
		if(type == -1) 
			throw new VariableFormatException("Unknown data type found");
		switch(type)
		{
			case 0:
			{
				try
				{
					Integer in = Integer.parseInt(str);
					var = new IntegerVariable(name);
					var.setData(in);
				}
				catch(NumberFormatException e)
				{
					throw new VariableFormatException("Error in data type detection for Integer");
				}
				break;
			}
			case 1:
			{
				try
				{
					Double in = Double.parseDouble(str);
					var = new DoubleVariable(name);
					var.setData(in);
				}
				catch(NumberFormatException e)
				{
					throw new VariableFormatException("Error in data type detection for double");
				}
				break;
			}
			case 2:
			{
				var = new StringVariable(name);
				var.setData(str.substring(1, str.length()-1));
				break;
			}
			case 3:
			{
				EList<Variable> list = deriveElements(str.substring(1, str.length()-1));
				var = new TableVariable(name);
				var.setData(list);
				break;
			}
			default:
			{
				throw new VariableFormatException("No known case for data type inputted");
			}
		}
		return var;
	}
	
	private static synchronized EList<Variable> deriveElements(String str) throws InvalidDataException
	{
		EList<String> tmp = EList.makeListFromString(str);
		EList<Variable> toReturn = new EList<>();
		for(String e: tmp)
		{
			try 
			{
				toReturn.add(Variable.makeVariable(null, e));
			} 
			catch (VariableFormatException e1) 
			{
				throw new InvalidDataException(e1.getMessage());
			}
		}
		return toReturn;
	}
	
	public static synchronized byte detectDataType(String str)
	{
		int startTable = str.indexOf('{');
		int endTable = str.lastIndexOf('}');
		int dot = str.indexOf('.');
		if(startTable != -1 && !isInQuotes(str, startTable) && !isInQuotes(str, endTable))
		{
			if(endTable == -1)
				return -1;
			return 3;
		}
		else if(str.indexOf('"') != -1)
		{
			if(str.lastIndexOf('"') == -1)
				return -1;
			return 2;
		}
		try
		{
			if(dot != -1)
			{
				if(str.lastIndexOf('.') != dot)
					return -1;
				Double.parseDouble(str);
				return 1;
			}
			else
			{
				Integer.parseInt(str);
				return 0;
			}
		}
		catch(NumberFormatException e)
		{
			return -1;
		}
	}
	
	public static synchronized boolean isInQuotes(String str, int index)
	{
		boolean inQuotes = false;
		for(int i = 0; i < index; i++)
			if(str.charAt(i) == 92)
				inQuotes = !inQuotes;
		return inQuotes;
	}
	
	public abstract String toString();
	
	public abstract Object getData();
	
	public abstract void setData(Object obj);
	
	protected final String name;
	private boolean isTable;
}
