package commands;

import java.io.File;

import lib.Command;
import lib.ESystem;
import display.Console;
import commands.exceptions.InvalidArgumentException;
import commands.exceptions.InvalidCommandException;
import commands.exceptions.InvalidSyntaxException;

public class Find extends Command
{
	private final static String[] help = 
	{
			"Find command:",
			"Modifers",
			"		-e [extension] --> searches for file extension. EX: jpg, png, txt, etc. Do not include the dot",
			"[name] --> Starts searching the entire disk for the file name",
			"[name] [directory] --> searches for the "
	};
	
	public Find(Console console)
	{
		super("find", console, new char[]{'e'}, help);
	}

	@Override
	public synchronized void execute(String[] args) throws InvalidCommandException 
	{
		found = false;
		console.setInputDisabled(true);
		if(args.length > 1)
		{
			File c = new File("C:/");
			if(args[0].length() == 2 && args[0].charAt(0) == '-' && args[0].charAt(1) == modifiers[0])
			{
				if(args.length == 4)
				{
					File toStart = ESystem.resolveFile(console.getCurrentDirectory(), args[3], false);
					if(toStart == null)
						throw new InvalidArgumentException("The path to start doesn't exist");
					if(!toStart.isDirectory())
						throw new InvalidArgumentException("The path to start is not a directory");
					searchFor(args[2], args[1], toStart, false);
				}
				else if(args.length == 3)
					searchFor(args[2], args[1], c, false);
				else
					throw new InvalidSyntaxException("The syntax given is not a valid modifier for the find command",
							InvalidSyntaxException.SHOW_ALL);
			}
			else
			{
				File toStart = ESystem.resolveFile(console.getCurrentDirectory(), args[1], false);
				if(toStart == null)
					throw new InvalidArgumentException("The path to start doesn't exist");
				if(!toStart.isDirectory())
					throw new InvalidArgumentException("The path to start is not a directory");
				searchFor(args[2], null, toStart, false);
				
			}
		}
		else if(args.length == 1)
			searchFor(args[0], null, null, false);
		else
		{
			console.setInputDisabled(false);
			throw new InvalidSyntaxException("Arguments given do not match any syntax for the find command",
					InvalidSyntaxException.SHOW_ALL);
		}
	}

	private synchronized void searchFor(String name, String extension, File dir, boolean recursive)
	{
		if(console.isFlagged())
			return;
		if(dir == null)
			dir = console.getCurrentDirectory();
		if(extension == null)
		{
			File[] files = dir.listFiles();
			if(files != null)
			{
				for(int j = 0; j < files.length && !console.isFlagged(); j++)
				{
					File e = files[j];
					if(console.isFlagged())
						return;
					if(e.isDirectory())
						searchFor(name, extension, e, true);
					else
					{
						String fName = e.getName();
						if(fName.length() >= name.length())
						{
							boolean equal = true;
							for(int i = 0; i < name.length() && equal; i++)
								if(fName.charAt(i) != name.charAt(i))
									equal = false;
							if(equal)
								console.output(ESystem.getNaturalPath(e));
						}
					}
				}
			}
		}
		else
		{
			File[] files = dir.listFiles();
			if(files != null)
			{
				boolean found = false;
				for(int i = 0; !found && i < files.length && !console.isFlagged(); i++)
				{
					File e = files[i];
					if(e.isDirectory())
						searchFor(name, extension, e, true);
					else
					{
						String fName = e.getName();
						if(fName.length() == name.length() && extension.equals(getExtension(fName)))
						{
							boolean equal = true;
							for(int j = 0; j < name.length() && equal; j++)
								if(fName.charAt(j) != name.charAt(j))
									equal = false;
							if(equal)
							{
								console.output(ESystem.getNaturalPath(e));
								found = true;
							}
						}
					}
				}
			}
		}
		if(!found)
			console.output("No files with the given paramters were found.");
	}
	
	private synchronized String getExtension(String name)
	{
		int index = name.lastIndexOf('.');
		return index == -1 ? null : name.substring(index + 1);
	}
	
	private volatile boolean found;
}
