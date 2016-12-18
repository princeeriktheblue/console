package commands;

import commands.exceptions.InvalidCommandException;
import display.Console;
import lib.Command;

public final class Start extends Command
{
	private final static String[] help = 
	{
		"No Parameters. Starts a new console instance"
	};
	
    /**
     * 
     * @param parent 
     */
    public Start(Console parent)
    {
        super("start", parent,null, help);
    }

    /**
     * 
     * @param line
     * @return
     * @throws InvalidCommandException When the command
     */
    @Override
    public synchronized void execute(String[] args) throws InvalidCommandException 
    {
        @SuppressWarnings("unused")
		Console con = new Console();
    }
}