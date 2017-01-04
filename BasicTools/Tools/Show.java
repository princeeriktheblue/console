package Tools;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import console.Command;
import console.KeyOverride;
import console.commands.exceptions.InvalidArgumentException;
import console.commands.exceptions.InvalidCommandException;
import console.commands.exceptions.InvalidSyntaxException;
import console.display.Console;
import lib.ESystem;
import lib.io.EFileReader;

public class Show extends Command implements KeyOverride
{
	private final static String[] help = 
	{
		"Show: ",
		"show [filename] -> the contents of a local file",
		"show [path] -> shows the contents of the file at path"
	};

	public Show(Console console) 
	{
		super("show", console, null, help);
	}

	@Override
	public synchronized void execute(String[] args) throws InvalidCommandException 
	{
		if(args.length == 1)
		{
			File file = ESystem.resolveFile(console.getCurrentDirectory(), args[0], false);
			if(file != null)
			{
				if(file.isDirectory())
					throw new InvalidArgumentException("The file specified must not be a directory");
				if(!file.canRead())
					console.output("The file cannot be read.");
				else
					readFile(file);
			}
			else
				console.output("No file found");
		}
		else
			throw new InvalidSyntaxException("There must be one argument", InvalidSyntaxException.SHOW_ALL);
	}
	
	private synchronized void readFile(File file)
	{
		EFileReader reader = new EFileReader(file);
		try 
		{
			reader.open();
			String toReturn = "";
			while(reader.ready())
				toReturn += reader.readLine() + ESystem.newline;
			console.clearScreen(false);
			console.output(toReturn);
			run = true;
			console.setInputDisabled(false);
			while(run)
			{}
			console.restoreScreen();
			
		} 
		catch (IOException e) 
		{
			console.output("The file is currently being used by another process.");
		}
		finally
		{
			try 
			{
				reader.close();
			} 
			catch (IOException e) 
			{}
		}
	}

	@Override
	public boolean keyTyped(KeyEvent evt) 
	{
		evt.consume();
		run = false;
		return false;
	}
	
	private volatile boolean run;
}