package display;

import commands.exceptions.InvalidDataException;
import lib.Storable;
import lib.datatypes.EList;
import lib.datatypes.PromptResponse;
import lib.datatypes.variables.Variable;
import lib.io.EFileReader;
import lib.runtime.CommandRunner;
import lib.Command;
import lib.ESystem;
import commands.*;

import java.awt.Color;
import java.awt.Font;

import java.io.File;
import java.io.IOException;

/**
 * This is the main display for the console. This class also handles all IO operations when it comes the screen. It also
 * hosts operations when it comes to commands, but in a separate thread.
 * 
 * @author Erik Belluomini
 */
public class Console
{	
	/**
	 * Creates a new console instance
	 */
	public Console() 
    {
		frame = new EFrame(this);
		etext = frame.getEText();
		String str = System.getProperty("os.name");
		if(str.contains("Win"))
		{
			isWindows = true;
			isLinux = false;
		}
		else if(str.contains("nux"))
		{
			isWindows = false;
			isLinux = true;
		}
		else
		{
			isWindows = false;
			isLinux = false;
		}
        commands = new EList<>();
        pastCommands = new EList<>();
        commands.add(new Cd(this));
        commands.add(new Clear(this));
        commands.add(new ClearCache(this));
        commands.add(new Copy(this));
        commands.add(new Delete(this));
        commands.add(new Display(this));
        commands.add(new Echo(this));
        commands.add(new Edit(this));
        commands.add(new EMath(this));
        commands.add(new Execute(this));
        commands.add(new Exit(this));
        commands.add(new Find(this));
        commands.add(new Help(this));
        commands.add(new Ipconfig(this));
        commands.add(new Ls(this));
        commands.add(new Mkdir(this));
        commands.add(new Move(this));
        commands.add(new Rename(this));
        commands.add(new Show(this));
        commands.add(new Start(this));
        commands.add(new Tree(this));
        var = new Var(this);
        commands.add(var);
        initVariables();
        initLaunch();
        frame.setVisible(true);
    }
	
	/**
	 * Initializes all the variables
	 */
	private synchronized void initVariables()
	{
        echo = true;
        runner = null;
        disableInput = false;
		enterPressed = false;
		isRunning = false;
	}
	
	/**
	 * Reads all the configuration and loads all data for commands.
	 */
	private synchronized void initLaunch()
	{
		readConfig();
        for(Command e: commands)
        	if(e instanceof Storable)
        		((Storable)e).loadData();
        output("Operating System: " + System.getProperty("os.name"));
        output(motd.isEmpty() ? motd: motd+ESystem.newline);
        frame.setTitle(title);
        displayCurrentLine(false);
	}
	
	/**
	 * Reads the configuration file from where it should be in respect to the console
	 * jar file
	 * 
	 * @return Errors while reading the config
	 */
	private synchronized void readConfig()
	{
		File conf = ESystem.getLocalFile("console.config");
		if(conf == null)
		{
			motd = "";
			currentDir = new File(System.getProperty("user.dir"));
			etext.setFont(new Font("Default",0,12));
			etext.setBackground(Color.BLACK);
			etext.setForeground(Color.GREEN);
			etext.setCaretColor(Color.GREEN);
			output("Config file not found");
		}
		else
		{
			try 
			{
				EFileReader in = new EFileReader(conf);
				in.open();
				String[] configLines = new String[configSize];
				int index = 0;
				while(index < configLines.length && in.ready())
					configLines[index++] = in.readLine();
				in.close();
				for(int i = 0; i < configSize; i++)
				{
					String data = readData(configLines[i]);
					if(data == null)
						output("Error: Line " + (i+1) + ": No ':' found");
					else
						loadConfigData(data, i);
				}
			} 
			catch (IOException e) 
			{
				output("Error while reading the file");
			}
		}
	}
	
	/**
	 * Loads the config data for the given index. 
	 * 
	 * @param data The string data read
	 * @param i The line of the config file
	 */
	private synchronized void loadConfigData(String data, int i)
	{
		String fontName="Default";
		data = data.trim();
		switch(i)
		{
			case 0:
			{
				motd = data;
				break;
			}
			case 1:
			{
				File temp = new File(data);
				if(temp.exists() && temp.isDirectory())
				{
					currentDir = temp;
					homeDir = data;
				}
				else
				{
					currentDir = new File(System.getProperty("user.dir"));
					output("Error: Line 2: Directory doesn't exist");
				}
				break;
			}
			case 2:
			{
				if(!data.equals(fontName))
				{
					Font temp = new Font(data, 0, 12);
					if(temp.getName().equals(fontName))
						output("Error: Line 3: Font given doesn't exist");
					else
						fontName = data;
				}
				break;
			}
			case 3:
			{
				int size = 12;
				try
				{
					size = Integer.parseInt(data);
				}
				catch(NumberFormatException e)
				{
					output("Error: Line 4: Font size isn't a valid integer number");
					etext.setFont(new Font(fontName, 0, size));
				}
				break;
			}
			case 4:
			{
				Color col = ESystem.getColor(data);
				if(col == null)
				{
					output("Error: Line 5: Background color value isn't valid");
					col = Color.BLACK;
				}
				etext.setBackground(col);
				break;
			}
			case 5:
			{
				Color col = ESystem.getColor(data);
				if(col == null)
				{
					output("Error: Line 6: Foreground color value isn't valid");
					col = Color.GREEN;
				}
				etext.setForeground(col);
				break;
			}
			case 6:
			{
				Color col = ESystem.getColor(data);
				if(col == null)
				{
					output("Error: Line 7: Caret color value isn't valid");
					col = Color.GREEN;
				}
				break;
			}
			default:
			{
				break;
			}
		}
	}
	
	/**
	 * Reads each individual line of data from the config file, and separates it based on the ':' separator.
	 * 
	 * @param str The line from the config file
	 * @return The data in that line of the config file, and if a colon is not found, <code>null</code>
	 */
	private synchronized String readData(String str)
	{
		if(str == null)
			return null;
		int index = str.indexOf(':');
		return index == -1 ? null : str.substring(index+1).trim();
	}
    
	

    /**
     * This method is fired when the user hits enter, with some sort of input. This method determines whether
     * the input is a command, then whether it's an assumption, and then determines what action to take. This method
     * calls the <code>execute</code> method of the command which was selected, or it calls the <code>help</code> 
     * method of the same command.
     * <br><br>
     * If the given input is that of an assumption, it will display
     * 
     * @param in The data the user entered
     */
    public synchronized void exec(String in, boolean fromFile)
    {
    	if(in.equalsIgnoreCase("Exit"))
    		exit();
    	else
    	{
	    	runner = new CommandRunner(this, frame, in, fromFile);
	    	output("");
	    	runner.start();
    	}
    }
    
    public static void main(String args[]) 
    {
        try
        {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException e)
        {
            javax.swing.JOptionPane.showMessageDialog(null, "There was an internal error. Please re-install client", 
            		"Fatal Error", javax.swing.JOptionPane.ERROR_MESSAGE, null);
        }
        java.awt.EventQueue.invokeLater(() -> {
            new Console();
        });
    }
    
    /**
     * Clears the screen. Defaults to the message of the day and a command line
     */
    public synchronized void clear()
    {
    	etext.clear(false);
    	if(!motd.isEmpty())
    		output(motd);
    }
    
    public synchronized String getTitle()
    {
    	return title;
    }
    
    public synchronized void setTitleData(String data)
    {
    	frame.setTitle(title + " " + data);
    }
    
    public synchronized void clearScreen(boolean currentDir)
    {
    	etext.tempClear(currentDir);
    }
    
    public synchronized void restoreScreen()
    {
    	etext.restore();
    }
    
    public synchronized Variable getVar(String name)
    {
    	return var.getVar(name);
    }
    
    public synchronized Object getVarAsObject(String name)
    {
    	Variable variable = var.getVar(name);
    	return variable == null ? null : variable.getData();
    }
    
    public synchronized String getVarAsString(String name)
    {
    	Variable variable = var.getVar(name);
    	return variable == null ? null : variable.getData().toString();
    }
    
    public synchronized PromptResponse prompt(String question, boolean override, boolean checkValid, int answer_state)
    {
    	String quest = question + "?";
    	PromptResponse answer = null;
    	if(override)
    	{
    		etext.tempClear(true);
    		etext.setText(quest);
        	answer = getResponse(quest, checkValid, answer_state);
        	etext.restore();
    	}
    	else
    	{
    		output(quest);
    		answer = getResponse(quest, checkValid, answer_state);
    	}
    	return answer;
    }
    
    private synchronized PromptResponse getResponse(String quest, boolean checkValid, int state)
    {
    	boolean responseValid = false;
    	do
    	{
    		String response = input();
    		if(checkValid && !ESystem.isValid(response))
	    	{
	    		output("Invalid input. Invalid characters are " + ESystem.disList);
	    		output(quest);
	    	}
    		else
    		{
    			int answer = ESystem.getAnswer(response, state);
    			if(answer != ESystem.INVALID_ANSWER)
    				return new PromptResponse(state, answer);
    			else if(answer == ESystem.NONBINARY_ANSWER)
	    			return new PromptResponse(state, response);
    			else
    				output("Invalid answer: Please respond with " + ESystem.getValidAnswersForState(state));
    		}
    			
    	}
    	while(!responseValid);
    	return new PromptResponse(state, ESystem.INVALID_ANSWER);
    }
    
    public synchronized void displayCurrentLine(boolean newLine)
    {
    	etext.setPath(ESystem.getNaturalPath(currentDir)+" >");
    	etext.insertPath(newLine);
    }
    
    public synchronized void output(String data)
    {
    	if(echo)
    	{
    		etext.append(data, true);
    		etext.finalizeData();
    	}
    }
    
    public synchronized String getCurrentText()
    {
    	return etext.getText();
    }
    
    public synchronized boolean nameUsed(String name)
    {
    	return var.isVariable(name);
    }
    
    public synchronized String input()
    {
    	disableInput = false;
    	String toReturn = null;
    	while(toReturn == null)
    	{
    		if(enterPressed)
    		{
    			enterPressed = false;
    			toReturn = etext.getAppended().trim();
    		}
    	}
    	disableInput = true;
    	return toReturn;
    }
    
    public synchronized void setPath(File file)
    {
    	currentDir = file;
    }
    
    public synchronized File getCurrentDirectory()
    {
    	return currentDir;
    }
    
    public synchronized String getPath()
    {
        return ESystem.getNaturalPath(currentDir);
    }
    
    public synchronized boolean inputDisabled()
    {
    	return disableInput;
    }
    
    public synchronized void setInputDisabled(boolean disabled)
    {
    	disableInput = disabled;
    }
    
    /**
     * Returns all loaded command names in the form of a string array.
     * 
     * @return A string array containing the names of the commands
     */
    public synchronized String[] getCommandNames()
    {
        String[] toReturn = new String[commands.size()];
        for(int i = 0; i < commands.size(); i++)
        	toReturn[i] = commands.get(i).getInitialValue();
        return toReturn;
    }
    
    /**
     * This method finds the arguments of the command from what the user inputed. Note that
     * the arguments are found between spaces, and are not limited to any number of arguments.
     * 
     * 
     * @param line The user input in the command line
     * @param initialValue The command name
     * @return An array of type string, each value in the array is an argument, first to last
     */
    public synchronized String[] getArguments(String line, String initialValue) throws InvalidDataException
    {
    	String toFind = line.substring(initialValue.length()).trim();
    	EList<String> args = new EList<>();
        int temp = 0;
        boolean spaceLast = false;
        boolean finalized = false;
        for(int i = 0; i < toFind.length(); i++)
        {
        	char ch = toFind.charAt(i);
        	if(ch == '"')
        	{
        		int tmp = i+1;
        		int ind = -2;
        		boolean done = false;
        		while(!done)
        		{
        			ind = toFind.indexOf('"', tmp);
        			if(ind == -1 || toFind.charAt(ind - 1) != 92)
        				done = true;
        			else
        				temp = ind + 1;
        		}
        		if(ind == -1)
        			throw new InvalidDataException("Quote was not closed.");
        		else
        		{
        			args.add(toFind.substring(i, ind + 1));
        			i = ind;
        			if(ind == toFind.length() - 1)
        				finalized = true;
        		}
        	}
        	else
        	{
        		boolean newPart = ch != ' ' && spaceLast;
        		if(newPart)
                {
                    spaceLast = false;
                    temp = i;
                }
                else if(!newPart && ch == ' ')
                {
                    spaceLast = true;
                    args.add(toFind.substring(temp, i));
                }
                else if(ch == '{')
                {
                	int count = 0;
                	int index = -1;
                	for(int j = i+1; j <toFind.length() && index == -1; j++)
                	{
                		char tempChar = toFind.charAt(j);
                		if(tempChar == '{')
                			count++;
                		else if(tempChar == '}')
                			if(count == 0)
                				index = j;
                			else
                				count--;
                			
                	}
                	if(index == -1)
                		throw new InvalidDataException("A table was not closed");
                	else
                	{
                		args.add(toFind.substring(i+1, index));
                		i=index;
                	}
                }
        	}
        }
        if(!finalized)
        {
        	if(temp != toFind.length())
        		args.add(toFind.substring(temp));
        }
        String[] toReturn = new String[args.size()];
        for(int i = 0; i < toReturn.length; i++)
        	toReturn[i] = args.get(i);
        return toReturn;
    }
    
    /**
     * Returns a <code>File</code> which is the current home directory.
     * 
     * @return The home directory given by the user
     */
    public synchronized File getHomeDirectory()
    {
    	return new File(homeDir);
    }
    
    public synchronized void addPastCommand(String cmd)
    {
    	pastCommands.add(cmd);
    }
    
    public synchronized boolean isFlagged()
    {
    	return runner.isInterrupted() || stop;
    }
    
    public synchronized boolean isVar(String name)
    {
    	return var.isVariable(name);
    }
    
    /**
     * 
     * @param on
     */
    public synchronized void setEcho(boolean on)
    {
    	echo = on;
    }
    
    public synchronized void executeLinesFromFile(String[] lines)
    {
    	boolean previous = echo;
    	echo = false;
    	for(String e: lines)
    		exec(e, true);
    	echo = previous;
    }
    
    public synchronized void close()
    {
    	exit();
    }
    
    public synchronized Command getRunning()
    {
    	return isRunning ? running : null;
    }
    
    public synchronized boolean isRunning()
    {
    	return running != null;
    }
    
    private synchronized void exit()
    {
    	for(Command e: commands)
    		if(e instanceof Storable)
    			((Storable)e).saveData();
    	int time = 0;
    	while(isRunning && time < 100)
    	{
    		time++;
    		try
    		{
    			Thread.sleep(1);
    		}
    		catch(InterruptedException e)
    		{}
    	}
    	if(isRunning)
    	{
    		try
    		{
    			runner.interrupt();
    		}
    		catch(SecurityException e)
    		{}
    		frame.setVisible(false);
    	}
    	frame.dispose();
    }
    
    /**
     * Stops execution of the command thread
     */
	public synchronized void stopExec()
    {
    	if(runner != null && runner.isAlive())
    	{
    		output("Execution Stopped");
    		stop = true;
    		frame.setTitle(title);
    		disableInput = false;
    	}
    }
    
    public synchronized boolean isWindows()
    {
    	return isWindows;
    }
    
    public synchronized boolean isLinux()
    {
    	return isLinux;
    }
    
    public synchronized EList<String> getPastCommands()
    {
    	return pastCommands;
    }
    
    public synchronized void enterPressed()
    {
    	enterPressed = true;
    }
    
    public CommandRunner getCommandRunner()
    {
    	return runner;
    }
    
    public synchronized Var getVarCommand()
    {
    	return var;
    }
    
    public synchronized EList<Command> getCommands()
    {
    	return commands;
    }
    
    
    private final boolean isLinux;
    private final boolean isWindows;
    
    private final EFrame frame;
    private final Var var; 
    private final EList<Command> commands;
    private final EList<String> pastCommands;
    private final EText etext;
    private final int configSize = 7;
    private final String title = "EBOS Console V1.1: ";
    
    private volatile boolean stop;
    private volatile boolean enterPressed;
    private volatile boolean isRunning;
    private volatile boolean echo;
    private volatile boolean disableInput;
    private volatile Command running;
    private volatile String motd;
    private volatile String homeDir;
    private volatile File currentDir;
    
    private CommandRunner runner;
}