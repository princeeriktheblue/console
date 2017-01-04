package engine.lang;

import engine.lang.exceptions.InvalidComparisonException;

public abstract class Variable
{
	public static final int INVALID_VARIABLE = -1;
	public static final int BOOL_VARIABLE = 0;
	public static final int INT_VARIABLE = 1;
	public static final int DOUBLE_VARIABLE = 2;
	public static final int STRING_VARIABLE = 3;
	public static final int TABLE_VARIABLE = 4;
	public static final int OBJECT_VARIABLE = 5;
	
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
	
	public abstract String toString();
	
	public abstract int compareTo(Variable v) throws InvalidComparisonException;
	
	protected final String name;
	private boolean isTable;
}
