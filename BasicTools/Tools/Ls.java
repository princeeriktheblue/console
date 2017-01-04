package Tools;

import java.io.File;

import console.Command;
import console.commands.exceptions.InvalidArgumentException;
import console.commands.exceptions.InvalidCommandException;
import console.commands.exceptions.InvalidSyntaxException;
import console.display.Console;
import lib.ESystem;

public class Ls extends Command
{
	private final static String[] help = 
	{
			"Usage: ",
            "ls --> Lists the files in the current directory",
            "ls [Path] --> Lists the files in the path"
	};
	
    public Ls(Console parent)
    {
        super("ls", parent, null, help);
    }

    @Override
    public synchronized void execute(String[] args) throws InvalidCommandException 
    {
        if(args.length == 0)
            showSubDirectories(console.getCurrentDirectory());
        else if(args.length == 1)
        {
        	File currentDir = ESystem.resolveFile(console.getCurrentDirectory(), args[0], false);
        	if(currentDir != null)
        	{
        		if(!currentDir.isDirectory())
        			throw new InvalidArgumentException("The given path is not a directory");
        		showSubDirectories(currentDir);
        	}
        	else
        		throw new InvalidArgumentException("The given path is invalid");
        }
        else
        	throw new InvalidSyntaxException("The number of arguments doesn't match the ls command", InvalidSyntaxException.SHOW_ALL);
    }
    
    private synchronized void showSubDirectories(File file)
    {
        File[] files = file.listFiles();
        if(files == null)
        	console.output("No files to show");
        else
            for(int i = 0; i < files.length; i++)
            	console.output(files[i].getName());
    }
}