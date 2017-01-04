package Tools;

import console.Command;
import console.commands.exceptions.InvalidCommandException;
import console.display.Console;

public final class Help extends Command
{
	private final static String[] help = 
	{
		"No arguments; displays commands currently registered in this console."
	};
	
    /**
     * Creates the help command called from the console
     * 
     * @param parent The console calling for the help command
     */
    public Help(Console parent)
    {
        super("help", parent, null, help);
    }

    /**
     * This is called when the help function is executed; Returns command names
     * 
     * @param line The user input
     * @return Command names
     * @throws InvalidCommandException If an error occurs
     */
    @Override
    public synchronized void execute(String[] args) throws InvalidCommandException 
    {
    	for(String e: console.getCommandNames())
    		console.output(e);
    }
}