package lib;

import commands.exceptions.EException;

public interface RunnableCommand 
{
	public void execute(String[] args) throws EException;
}
