package commands;

import commands.exceptions.EException;
import display.Console;
import lib.Command;

public class ConsoleDisplay extends Command
{
	public ConsoleDisplay(Console console) 
	{
		super("console", console, new char[]{}, new String[]{});
	}

	@Override
	public void execute(String[] args) throws EException {
		// TODO Auto-generated method stub
		
	}

}
