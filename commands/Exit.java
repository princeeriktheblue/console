package commands;

import commands.exceptions.InvalidCommandException;
import display.Console;
import lib.Command;

public final class Exit extends Command
{
	private final static String[] help = 
	{
		"No parameters: Kills the console instance"	
	};
	
	public Exit(Console console) 
	{
		super("exit", console, null, help);
	}
	
    @Override
    public synchronized void execute(String[] args) throws InvalidCommandException 
    {}
}