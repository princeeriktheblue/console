package engine.lang;

import engine.lang.exceptions.InvalidCastException;
import engine.lang.exceptions.VariableFormatException;

public abstract class PrimitiveVariable extends Variable
{
	public PrimitiveVariable(String name) 
	{
		super(name, false);
	}
	
	public static synchronized PrimitiveVariable makeVariable(String name, String str) throws VariableFormatException
	{
		byte type = detectDataType(str);
		PrimitiveVariable var = null;
		if(type == -1) 
			throw new VariableFormatException("Unknown data type found");
		switch(type)
		{
			case BOOL_VARIABLE:
			{
				try
				{
					Boolean bool = Boolean.parseBoolean(str);
					var = new BoolVariable(name);
					var.setData(bool);
				}
				catch(NumberFormatException e)
				{
					throw new VariableFormatException("Error in data type detection for boolean");
				}
				break;
			}
			case INT_VARIABLE:
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
			case DOUBLE_VARIABLE:
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
			case STRING_VARIABLE:
			{
				var = new StringVariable(name);
				var.setData(str.substring(1, str.length()-1));
				break;
			}
			default:
			{
				throw new VariableFormatException("No known case for data type inputted");
			}
		}
		return var;
	}
	
	public static synchronized byte detectDataType(String str)
	{
		int startTable = str.indexOf('{');
		int endTable = str.lastIndexOf('}');
		int dot = str.indexOf('.');
		if(startTable != -1 && !isInQuotes(str, startTable) && !isInQuotes(str, endTable))
		{
			if(endTable == -1)
				return INVALID_VARIABLE;
			return TABLE_VARIABLE;
		}
		else if(str.indexOf('"') != -1)
		{
			if(str.lastIndexOf('"') == -1)
				return INVALID_VARIABLE;
			return STRING_VARIABLE;
		}
		else if(str.equals("true") || str.equals("false"))
			return BOOL_VARIABLE;
		try
		{
			if(dot != -1)
			{
				if(str.lastIndexOf('.') != dot)
					return INVALID_VARIABLE;
				Double.parseDouble(str);
				return DOUBLE_VARIABLE;
			}
			else
			{
				Integer.parseInt(str);
				return INT_VARIABLE;
			}
		}
		catch(NumberFormatException e)
		{
			return INVALID_VARIABLE;
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

	public abstract Object getData();
	
	public abstract void setData(Object obj);
	
	public abstract PrimitiveVariable cast(Variable v) throws InvalidCastException;
}
