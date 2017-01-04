package engine.lang;

import engine.lang.exceptions.InvalidComparisonException;
import engine.lang.exceptions.MethodException;

public abstract class ObjectVariable extends Variable
{
	public ObjectVariable(String name, boolean isTable) 
	{
		super(name, false);
	}

	public abstract String toString();
	public abstract int compareTo(Variable v) throws InvalidComparisonException;
	public abstract String getClassName();
	public abstract String memberAccess(String memberName, String[] args) throws MethodException;
	
	public abstract ObjectVariable createNew();
}
