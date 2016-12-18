package commands;

import java.io.File;
import java.io.IOException;

import lib.Command;
import lib.ESystem;
import display.Console;
import commands.exceptions.CommandErrorException;
import commands.exceptions.EException;
import commands.exceptions.InvalidArgumentException;
import commands.exceptions.InvalidSyntaxException;
import commands.exceptions.UnknownErrorException;

public class Rename extends Command
{
	private final static String[] help = 
	{
			"Rename:",
			"rename [file] [name] --> Renames the file to the new name"
	};
	
	public Rename(Console parent) 
	{
		super("rename", parent, null, help);
	}

	@Override
	public synchronized void execute(String[] args) throws EException 
	{
		if(args.length == 2)
		{
			original = ESystem.resolveFile(console.getCurrentDirectory(), args[0], false);
			if(original == null)
				throw new InvalidArgumentException("The file given doesn't exist");
			File[] files = original.getParentFile().listFiles();
			int counter = 0;
			for(File e: files)
				if(e.getName().length() >= args[1].length() && e.getName().substring(0, args[1].length()).equals(args[1]))
					counter++;
			String path = original.getParentFile().getPath();
			if(path.charAt(path.length()-1) != '/')
				path += '/';
			overwrite = new File(path + args[1]);
			if(counter != 0)
			{
				if(overwrite.exists())
				{
					console.output("Would you like to overwrite the file that already exists?");
					String userResponse = console.input();
					if(userResponse.equalsIgnoreCase("yes") || userResponse.equalsIgnoreCase("y"))
			        {
			            if(overwrite.exists())
			                if(!overwrite.delete())
			                    throw new CommandErrorException("Error while renaming the file");
			            try
			            {
			            	overwrite.createNewFile();
			            }
			            catch(IOException e)
			            {
			                throw new UnknownErrorException("Error renaming file. Operation unsuccessful");
			            }
			            ESystem.copyToFile(original, overwrite);
			        }
			        else if(userResponse.equalsIgnoreCase("no") || userResponse.equalsIgnoreCase("n"))
			        {
			            try
			            {
			            	nonConflict.createNewFile();
			            }
			            catch(IOException e)
			            {
			                throw new UnknownErrorException("Error renaming file. Operation unsuccessful");
			            }
			            ESystem.copyToFile(original, nonConflict);
			        }
			        else if(userResponse.equalsIgnoreCase("cancel") || userResponse.equalsIgnoreCase("c"))
			            console.output("Rename cancelled");
			        else
			        	console.output("Invalid entry. Rename cancelled");
				}
			}
		}
		else throw new InvalidSyntaxException("The given syntax isn't correct for the command 'Rename'");
	}
	
	private File original;
	private File overwrite;
	private File nonConflict;
}
