package Tools;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import console.Command;
import console.KeyOverride;
import console.commands.exceptions.InvalidArgumentException;
import console.commands.exceptions.InvalidSyntaxException;
import console.commands.exceptions.UnknownErrorException;
import console.display.Console;
import console.exceptions.EException;
import lib.ESystem;
import lib.io.EFileReader;
import lib.io.EFileWriter;

public class Edit extends Command implements KeyOverride
{
	private static final String[] helpArgs = {};

	public Edit(Console console) 
	{
		super("edit", console, new char[]{}, helpArgs);
	}

	public synchronized void execute(String[] args) throws EException 
	{
		if(args.length == 1)
		{
			File file = ESystem.resolveFile(console.getCurrentDirectory(), args[0], true);
			if(file.exists())
			{
				if(file.isDirectory())
					throw new InvalidArgumentException("The file specified must not be a directory");
				if(!file.canRead())
					console.output("The file cannot be read.");
				else
					readFile(file);
			}
			else
			{
				try 
				{
					boolean made = file.createNewFile();
					if(!made)
						throw new UnknownErrorException("Cannot create new file");
					console.clearScreen(false);
					run = true;
					while(run)
					{}
					console.restoreScreen();
				} 
				catch (IOException e) 
				{
					console.output("Cannot create file.");
				}
			}
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
	
	private final void save()
	{
		EFileWriter writer = new EFileWriter(target, false);
		try
		{
			writer.open();
			writer.write(console.getCurrentText());
			writer.close();
		}
		catch(IOException e)
		{
			try 
			{
				writer.close();
			} 
			catch (IOException e1) 
			{}
		}
	}

	@Override
	public boolean keyTyped(KeyEvent evt) 
	{
		int code = evt.getKeyCode();
		char ch = evt.getKeyChar();
		if(evt.isAltDown())
		{
			if(code == KeyEvent.VK_C)
			{
				if(!edited)
					run = false;
				else
				{
					int answer = console.prompt("Do you want to exit without saving", true, false, 
							ESystem.YES_NO_CANCEL_ANSWER).getResponse();
					if(answer == ESystem.YES_ANSWER)
						run = false;
					else if(answer == ESystem.NO_ANSWER)
						save();
				}
			}
			else if(code == KeyEvent.VK_S)
			{
				if(!edited)
					run = false;
				else
				{
					int overwrite = console.prompt("Do you want to overwrite the current file", 
							true, false, ESystem.YES_NO_CANCEL_ANSWER).getResponse();
					if(overwrite == ESystem.YES_ANSWER)
						save();
				}
			}
		}
		else if(code != KeyEvent.VK_UP && code != KeyEvent.VK_LEFT && code != KeyEvent.VK_DOWN && code != KeyEvent.VK_RIGHT)
			edited = true;
		return ch != KeyEvent.CHAR_UNDEFINED;
	}

	private File target;
	private boolean edited;
	private volatile boolean run;
}
