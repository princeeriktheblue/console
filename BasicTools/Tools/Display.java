package Tools;

import console.Command;
import console.commands.exceptions.InvalidCommandException;
import console.commands.exceptions.InvalidSyntaxException;
import console.display.Console;

public class Display extends Command
{
	private final static String[] help = 
	{
		"Display [arg1] [arg2] ... [argn] --> displays each argument on a separate line"
	};
	
   	public Display(Console parent) 
   	{
   		super("display", parent, null, help);
   	}

   	@Override
   	public synchronized void execute(String[] args) throws InvalidCommandException 
   	{
   		if(args.length == 0)
   			throw new InvalidSyntaxException("No valid arguments were passed in", InvalidSyntaxException.SHOW_ALL);
   		for(int i = 0; i < args.length; i++)
   			console.output(args[i]);
   	}
}