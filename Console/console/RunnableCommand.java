package console;

import console.exceptions.EException;

public interface RunnableCommand 
{
	public void execute(String[] args) throws EException;
}
