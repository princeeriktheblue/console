package Tools;

import console.Command;
import console.commands.exceptions.InvalidCommandException;
import console.display.Console;

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