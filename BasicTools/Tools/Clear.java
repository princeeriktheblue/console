package Tools;

import console.Command;
import console.commands.exceptions.InvalidCommandException;
import console.display.Console;

public final class Clear extends Command
{
	private final static String[] help = 
		{
			"No parameters: Clears the console window to the default format"	
		};
	
    /**
     * Creates the command with the given console calling it. 
     * 
     * @param console The console accessing this command
     */
    public Clear(Console console)
    {
        super("clear", console, null, help);
    }
    
    /**
     * This method is called when the user inputs the command.
     * 
     * @param line The data the user input
     * @return The data which is to be shown on the console
     * @throws InvalidCommandException When the command has problems
     */
    @Override
    public synchronized void execute(String[] args) throws InvalidCommandException
    {
        console.clear();
    }
}