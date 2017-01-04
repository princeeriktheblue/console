package console;

import console.display.Console;
import console.exceptions.EException;

/**
 * The command class is the abstract parent class of all commands; it is the basic framework for any command that would be
 * executed in a console instance. <b>ALL COMMANDS MUST EXTEND THIS CLASS</b>
 * 
 * @author Erik
 *
 */
public abstract class Command implements RunnableCommand, Comparable<Command>
{
    public Command(String initialValue, Console console, char[] modifiers, String[] helpArgs)
    {
        this.initialValue = initialValue.toLowerCase();
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
    
    public synchronized int compareTo(Command c)
    {
    	return initialValue.compareTo(c.initialValue);
    }
    
    protected final String initialValue;
    protected final Console console;
    protected final char[] modifiers;
    protected final String[] helpArgs;
    
	public abstract void execute(String[] args) throws EException;
}