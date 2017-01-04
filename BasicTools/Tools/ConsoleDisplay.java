package Tools;

import console.Command;
import console.display.Console;
import console.exceptions.EException;

public class ConsoleDisplay extends Command
{
	public ConsoleDisplay(Console console) 
	{
		super("console", console, new char[]{}, new String[]{});
	}

	@Override
	public void execute(String[] args) throws EException {
		// TODO Auto-generated method stub
		
	}

}
