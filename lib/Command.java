package lib;

import commands.exceptions.EException;
import display.Console;

public abstract class Command implements RunnableCommand
{
    public Command(String initialValue, Console console, char[] modifiers, String[] helpArgs)
    {
        this.initialValue = initialValue;
        this.console = console;
        this.modifiers = modifiers;
        this.helpArgs = helpArgs;
    }
    
    public void getHelp()
    {
    	for(String e: helpArgs)
    		console.output(e);
    }
    
    /**
     * Returns the name of the command
     * 
     * @return The name of the command
     */
    public final String getInitialValue()
    {
    	return initialValue;
    }
    
    public synchronized void getHelpLine(int line)
    {
    	console.output(helpArgs[line]);
    }
    
    protected final String initialValue;
    protected final Console console;
    protected final char[] modifiers;
    protected final String[] helpArgs;
    
	public abstract void execute(String[] args) throws EException;
}