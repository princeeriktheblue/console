package Tools;

import console.Command;
import console.commands.exceptions.InvalidCommandException;
import console.display.Console;

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