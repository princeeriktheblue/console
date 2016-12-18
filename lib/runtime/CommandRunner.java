package lib.runtime;

import commands.Var;
import commands.exceptions.EException;
import commands.exceptions.InvalidAssumptionException;
import commands.exceptions.InvalidSyntaxException;
import display.Console;
import display.EFrame;
import display.EText;
import lib.Command;
import lib.ESystem;
import lib.datatypes.EList;

public final class CommandRunner extends Thread implements Runnable
{
	public CommandRunner(Console con, EFrame frame, String initial, boolean fromFile)
	{
		console = con;
		in = initial;
		result = null;
		recCall = false;
		error = false;
		this.frame = frame;
		this.fromFile = fromFile;
		this.etext = frame.getEText();
		this.var = console.getVarCommand();
	}
	
	private CommandRunner(Console con, EFrame frame, String initial, boolean rec, boolean fromFile)
	{
		console = con;
		in = initial;
		result = null;
		recCall = rec;
		error = false;
		this.frame = frame;
		this.fromFile = fromFile;
		this.etext = frame.getEText();
		this.var = console.getVarCommand();
	}
	
	public synchronized void run()
	{
        String data = in;
        String init = data.indexOf(' ') != -1 ? data.substring(0, data.indexOf(' ')) : data;
        if(!init.equalsIgnoreCase("exit"))
        {
        	data = executeEmbeddedCommand(data);
        	runCommand(init, data);
            if(!recCall && !console.isFlagged())
            {
            	running = null;
		    	console.setInputDisabled(false);
            }
        }
	}
	
	private synchronized String executeEmbeddedCommand(String data)
	{
		int index = data.indexOf('|');
        if(index != -1 && !interrupted())
        {
        	CommandRunner nextRunner = new CommandRunner(console, frame, data.substring(index+1), true, fromFile);
        	nextRunner.run();
        	if(nextRunner.error)
        	{
        		error = true;
        		result = nextRunner.result;
        	}
        	else
        		data = data.substring(0, index) + nextRunner.result;
        }
        return data;
	}
	
	/**
     * This method uses the <code>getArguments</code> method to derive the arguments from <code>line</code>, then
     * it checks all arguments for whether they are assumptions. If an argument is an assumption, the argument is replaced with the
     * assumption data. If it calls an assumptions which isn't registered, it throws an <code>InvalidAssumptionException</code>
     * 
     * @param line The user's input
     * @param initialValue The value of the command or function being called
     * @return An array of string values, assumption values included
     * @throws InvalidAssumptionException When an assumption call leads to an assumption which isn't registered
     * @see getArguments
     */
    private synchronized String[] analyzeCheck(String line, String initialValue) throws EException
    {
    	String[] args = console.getArguments(line, initialValue);
    	if(args.length > 0)
	    	for(int i = 0; i < args.length; i++)
	    		if(args[i].length() > 0)
	    			if(args[i].length() > 0 && args[i].charAt(0) == ':')
		        	{
		        		if(args[i].length() < 2)
		        			throw new InvalidAssumptionException("Assumption name is not given");
		        		if(var.isVariable(args[i].substring(1)))
		        			args[i] = console.getVarAsString(args[i].substring(1));
		        		else
		        			throw new InvalidAssumptionException(args[i].substring(1) + " is not a valid assumption");
		        	}
		    		else
		    			args[i].replaceAll("_", " ");
    	return args;
    }
	
	private synchronized void runCommand(String init, String data)
	{
		if(!error)
        {
            try
            {
            	boolean executed = false;
            	if(init.charAt(0) == ':' && var.isVariable(init.substring(1)))
            	{
            		String assumptionData = console.getVarAsString(init.substring(1));
                	console.output(assumptionData == null ? "The given assumption name is not valid": assumptionData);
            		executed = true;
               	}
               	else
               	{
               		boolean cont = true;
               		EList<Command> commands = console.getCommands();
               		for(int i = 0; i < commands.size() && cont && !interrupted(); i++)
               		{
               			Command e = commands.get(i);
                        if(e.getInitialValue().equalsIgnoreCase(init))
                        {
                        	cont = false;
                   			frame.setTitle(console.getTitle() + e.getInitialValue() + " executing...");
                            running = e;
                            String[] args = analyzeCheck(data, e.getInitialValue());
      	                    if(data.charAt(data.length() -1) == '?')
      	                    	e.getHelp();
      	                    else
      	                    	e.execute(args);
       	                    executed = true;
                        }
                	}
                }
            	if(!executed)
            	{
                	error = true;
                	console.output("The given input is neither a command nor an assumption");
            	}
            	if(recCall)
            		return;
            }
            catch(InvalidSyntaxException e)
            {
            	error = true;
            	console.output(e.getMessage());
            	int index = e.getIndex();
            	if(index == InvalidSyntaxException.SHOW_ALL)
            		running.getHelp();
            	else
            		running.getHelpLine(index);
            }
            catch(EException e)
            {
            	error = true;
            	console.output(e.getMessage());
            }
            console.addPastCommand(in);
            frame.setLast(console.getPastCommands().size());
            frame.setTitle(console.getTitle());
            if(exitFlag)
	    	{
	    		console.output("-Stopped-");
	    		console.displayCurrentLine(true);
	    		if(!recCall)
	    			exitFlag = false;
	    	}
            else
            {
            	if(error || (running != null && !running.getInitialValue().equals("clear")))
            		console.output("");
            	console.displayCurrentLine(false);
            }
	    	if(fromFile)
	    		result += ESystem.newline;
	    	if(previous != null)
	    		console.restoreScreen();
	    	etext.fixCaret(true);
        }
	}
	
	private volatile boolean exitFlag;
	private volatile Command running;
	private volatile boolean fromFile;
	private volatile String previous;
	private final boolean recCall;
	private volatile boolean error;
	private volatile String result;
    private final Console console;
    private final EFrame frame;
    private final EText etext;
    private final Var var;
    private final String in;
}