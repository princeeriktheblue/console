package Tools;

import java.io.File;

import console.Command;
import console.commands.exceptions.InvalidArgumentException;
import console.commands.exceptions.InvalidCommandException;
import console.commands.exceptions.InvalidSyntaxException;
import console.display.Console;
import lib.ESystem;

public final class Cd extends Command
{
	private final static String[] help = 
		{
			"Usage: ",
			"cd [Directory] --> Transfers the abstract path to the local directory",
		};
	
    /**
     * Creates the command with the given console calling it
     * 
     * @param console The console console
     */
    public Cd(Console console)
    {
        super("cd", console, null, help);
    }

    /**
     * This method is called when the command is to be executed, with line being the
     * data which the user inputed into the console.
     * 
     * @param line Data the user inputed
     * @return What to display on the screen
     * @throws InvalidCommandException When the command isn't executable due to user or system error
     */
    @Override
    public synchronized void execute(String[] args) throws InvalidCommandException
    {
 	   if(args.length == 0)
 		   throw new InvalidSyntaxException("No directory argument listed.", InvalidSyntaxException.SHOW_ALL);
 	   else if(args.length > 1)
 		   throw new InvalidSyntaxException("Invalid syntax for the cd command", InvalidSyntaxException.SHOW_ALL);
 	   else if(args[0].equals(".."))
        {
            if(console.getPath().equals("C:/"))
            	console.output("No console path");
            else
            	console.setPath(console.getCurrentDirectory().getParentFile());
        }
        else if(args[0].equals("~"))
        	console.setPath(console.getHomeDirectory());
        else
        {
	           File file = ESystem.resolveFile(console.getCurrentDirectory(), args[0], false);
	           if(file == null)
	        	   throw new InvalidArgumentException("The given path isn't valid or isn't a directory");
	           if(!file.isDirectory())
	        	   throw new InvalidArgumentException("The given path results in a file.");
	           console.setPath(file);
        }
    }
}