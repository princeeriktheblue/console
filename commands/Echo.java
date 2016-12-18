package commands;

import commands.exceptions.InvalidArgumentException;
import commands.exceptions.InvalidCommandException;
import commands.exceptions.InvalidSyntaxException;
import display.Console;
import lib.Command;

public class Echo extends Command
{
	private final static String[] help = 
	{
			"Usage:",
		    "Echo on --> Echos out responses",
		    "Echo off --> Echos out nothing"
	};
	
	public Echo(Console con)
    {
       super("echo", con, new char[]{'r'}, help);
    }

   @Override
   public void execute(String[] args) throws InvalidCommandException 
   {
	   if(args.length > 2 || args.length < 1)
		   throw new InvalidSyntaxException("The given number of argument's "
		   		+ "doesn't match syntax for the command Echo", InvalidSyntaxException.SHOW_ALL);
       if(args.length == 1)
       {
    	   if(args[0].equalsIgnoreCase("on"))
    		   console.setEcho(true);
    	   else if(args[0].equalsIgnoreCase("off"))
    		   console.setEcho(false);
    	   else
    		   throw new InvalidArgumentException("The argument given is invalid for the command echo");
       }
   }
}