package console.commands;

import lib.ESystem;
import lib.datatypes.EList;
import lib.io.EFileReader;
import lib.io.EFileWriter;
import engine.lang.PrimitiveVariable;
import engine.lang.TableVariable;
import engine.lang.Variable;
import engine.lang.exceptions.EngineException;

import java.io.File;
import java.io.IOException;

import console.Command;
import console.Storable;
import console.commands.exceptions.CommandErrorException;
import console.commands.exceptions.CommandLoadingException;
import console.commands.exceptions.InvalidArgumentException;
import console.commands.exceptions.InvalidCommandException;
import console.commands.exceptions.InvalidSyntaxException;
import console.display.Console;

/**
 * This command is to dynamically save and load variables during runtime execution of a paired <code>Console</code> object. This class
 * manages and saves variables, and also outlines (but not executes) operations on these variable objects. NOTE: THIS CLASS DOES NOT HOLD
 * THE ACTUAL VARIABLE DATA TYPE, AND AS SUCH CANNOT DIRECTLY ACCESS MEMBERS OF ANY VARIABLE OBJECT.
 * 
 * @author Erik
 */
public class Var extends Command implements Storable
{
	private final static String[] help = 
	{
		"Savevar: ",
		"This command is used to save variables onto the machine to be used later. ",
		"These variables are global, and cannot be overwritten. Any attempt to overwrite a variable",
		"will fail. These variables are saved on the exiting of the current console using them. However, ",
		"different console locations will result in different variables.",
		"",
		"Argument list:",
		"/s -> Save variable",
		"-i [Varname] -> Gets user input and saves that input as a string.",
		"-v [Varname] -> Copies the data from one variable into another a new variable with that name",
		"-f [Varname] [File] -> Reads the ENTIRE file and saves it into the variable as a table",
		"-s [Varname] [Data] -> Creates a static variable with the given info",
		"-a -> Lists all variables and the data stored in them",
		"-n -> Lists all variable names, but not the data stored inside"
	};
	
	/**
	 * Creates an instance of the <code>Var</code> Class with the console parent <code>console</code>
	 * @param console
	 */
	public Var(Console console) 
	{
		//i=input, v=var, s=static, f=file, a=all, n=names
		super("var", console, new char[]{'i', 'v', 'f', 's', 'a', 'n'}, help);
		vars = new EList<>();
		tosave = new EList<>();
	}

	@Override
	public synchronized void execute(String[] args) throws InvalidCommandException 
	{
		if(args.length < 1)
			throw new InvalidSyntaxException("The var command requires one or more arguments.", InvalidSyntaxException.SHOW_ALL);
		int code = -1;
		boolean save = false;
		boolean makevar = true;
		if(args[0].equalsIgnoreCase("/s"))
			save = true;
		int ind = save ? 1 : 0;
		if(args[ind].length() < 2 || args[ind].charAt(0) != '-')
			throw new InvalidArgumentException("The first argument is invalid");
		for(int i = 0; i < modifiers.length; i++)
			if(modifiers[i] == args[ind].charAt(1))
				code = i;
		String name = null;
		String vData = null;
		switch(code)
		{
			case 0:
			{
				if(args.length == 2 + ind)
				{
					String str = console.input().trim();
					name = args[1 + ind];
					vData = str;
				}
				else
					throw new InvalidSyntaxException("There needs to be two or three arguments for the input modifier", 8);
				break;
			}
			case 1:
			{
				if(args.length == 3 + ind)
				{
					String vName = args[2 + ind];
					Variable v = null;
					for(int i = 0; i < vars.size() && v == null; i++)
					{
						Variable temp = vars.get(i);
						if(temp.getName().equals(vName))
							v = temp;
					}
					for(int i = 0; i < tosave.size() && v == null; i++)
					{
						Variable temp = tosave.get(i);
						if(temp.getName().equals(vName))
							v = temp;
					}
					if(v == null)
						throw new InvalidArgumentException("The given variable name is invalid");
					name = args[1 + ind];
					vData = v.toString();
				}
				else
					throw new InvalidSyntaxException("There needs to be only three arguments for the variable modifier", 9);
				break;
			}
			case 2:
			{
				if(args.length == 4 + ind)
				{
					File f = ESystem.resolveFile(console.getCurrentDirectory(), args[1 + ind], false);
					if(f == null)
						throw new InvalidArgumentException("The file specified doesn't exist");
					EList<String> str = new EList<>();
					name = args[2 + ind];
					TableVariable<String> var = new TableVariable<>(args[2 + ind]);
					try 
					{
						for(String e: EFileReader.readAllLines(f.toPath()))
							str.add(e);
					} 
					catch (IOException e) 
					{
						throw new InvalidArgumentException("The file specified doesn't exist");
					}
					var.setData(str);
				}
				else 
					throw new InvalidSyntaxException("The number of arguments for the given command is invalid.", 10);
				break;
			}
			case 3:
			{
				if(args.length == 3 + ind)
				{
					name = args[1 + ind];
					vData = args[2 + ind];
				}
				else
					throw new InvalidSyntaxException("The number of arguments for the given command is invalid", 11);
				break;
			}
			case 4:
			{
				console.output("[Name]" + ESystem.tab + "[Data]");
				if(args.length == 1 + ind)
					for(PrimitiveVariable e: vars)
						console.output(e.getName() + ESystem.tab + e.getData());
				else
					throw new InvalidSyntaxException("The nunber of arguemnts for the given command is invalid", 12);
				makevar = false;
				break;
			}
			case 5:
			{
				console.output("[Name]");
				if(args.length == 1 + ind)
					for(Variable e: vars)
						console.output(e.getName());
				else
					throw new InvalidSyntaxException("The number of arguments for the given command is invalid", 13);
				makevar = false;
				break;
			}
			default:
				throw new InvalidArgumentException("The given modifier isn't valid for the var command");
		}
		if(makevar)
		{
			if(!ESystem.isValid(name))
				throw new InvalidArgumentException("The name given isn't valid");
			try
			{
				PrimitiveVariable v = PrimitiveVariable.makeVariable(name, vData);
				if(save)
					tosave.add(v);
				else
					vars.add(v);
			}
			catch(EngineException e)
			{
				throw new InvalidArgumentException("The data given isn't valid for a variable");
			}
		}
	}
	
	/**
	 * Returns whether the given string <code>name</code> coincides with a variable
	 * 
	 * @param name The name of the variable
	 * @return Whether a variable with that name exists
	 */
	public synchronized boolean isVariable(String name)
	{
		for(PrimitiveVariable e: vars)
			if(e.getName().equals(name))
				return true;
		for(PrimitiveVariable e: tosave)
			if(e.getName().equals(name))
				return true;
		return false;
	}
	
	/**
	 * Checks whether a variable with the name <code>name</code> is a save variable
	 * 
	 * @param str The name of the variable
	 * @return <code>true</code> if the variable exists and is marked as saved, <code>false</code> otherwise
	 */
	public synchronized boolean isSaveVariable(String str)
	{
		for(PrimitiveVariable e: tosave)
			if(e.getName().equals(str))
				return true;
		return false;
	}
	
	/**
	 * Returns a variable which is stored with the name <code>name</code>
	 * 
	 * @param name The name of the variable
	 * @return The <code>Variable</code> with that name, <code>null</code> if it doesn't exist.
	 */
	public synchronized PrimitiveVariable getVar(String name)
	{
		for(PrimitiveVariable e: vars)
			if(e.getName().equals(name))
				return e;
		for(PrimitiveVariable e: tosave)
			if(e.getName().equals(name))
				return e;
		return null;
	}
	
	/**
	 * Called at the beginning of execution, loads the variable data for the console instance
	 */
	public synchronized void loadData()
	{
		File file = ESystem.getLocalFile(FILENAME);
		boolean exists = file != null;
		if(!exists)
			if(!ESystem.createLocalFile(FILENAME))
				console.output("Error: Could not create variable file");
			else
				exists = true;
		else
		{
			EFileReader in = new EFileReader(file);
    		try 
    		{
    			in.open();
    			int counter = 1;
    			String data = "";
    			String name = "";
    			while(in.ready())
    			{
    				String temp = in.readLine();
    				if(name.isEmpty())
    				{
    					int index = temp.indexOf(':');
    					if(index == -1)
    						console.output("Error: Variable file: Line "+counter);
    					else
    					{
    						name = temp.substring(0, index);
    						for(int i = 0; i < temp.length(); i++)
    							if(temp.charAt(i) == ':')
    								data += temp.substring(0, i) + ESystem.newline + temp.substring(i+1);
    						int index2 = temp.indexOf('|');
    						if(index2 == -1 && data.isEmpty())
    							data+=temp.substring(index+1);
    						else if(index2 != -1)
    						{
    							try 
    							{
									addVariable(name, temp.substring(index+1, index2));
								} 
    							catch (CommandErrorException e) 
    							{
									console.output("Error while loading variable " + name);
								}
    							name = "";
    							data = "";
    						}
    					}
    				}
    				else
    				{
    					for(int i = 0; i < temp.length(); i++)
    						if(temp.charAt(i) == ':')
    							temp = temp.substring(0, i) + ESystem.newline + temp.substring(i+1);
    					int index = temp.indexOf('|');
    					if(index == -1)
    						data += temp;
    					else
    					{
    						try
    						{
								addVariable(name, data + temp.substring(0, index));
							} 
    						catch (CommandErrorException e) 
    						{
								console.output("Error while loading variable " + name);
							}
    						name = "";
    						data = "";
    					}
    				}
    				counter++;
    			}
    			in.close();
    		}
    		catch (IOException e) 
    		{
    			console.output("Error while loading variables");
    		}
    		finally
    		{
    			try 
    			{
					in.close();
				} 
    			catch (IOException e) 
    			{}
    		}
		}
	}
	
	/**
	 * Called at the end of execution, saves all variables to the data file
	 */
	public void saveData() 
	{
		File file = ESystem.getLocalFile(FILENAME);
    	boolean made = true;
    	if(file == null)
    		made = !ESystem.createLocalFile(FILENAME);
    	if(made)
    	{
    		EFileWriter writer = new EFileWriter(file, false);
    		try
    		{
    			writer.open();
    			for(PrimitiveVariable e: tosave)
    				if(e != null)
    					writer.write(e.getName()+':'+e.toString()+'|');
    			writer.close();
    		}
    		catch(IOException e)
    		{}
    		finally
    		{
    			try 
    			{
					writer.close();
				} 
    			catch (IOException e) 
    			{}
    		}
    	}
	}
	
	/**
	 * Adds a variable from a basic string input (specifically from a file) and then adds it to the save pool
	 * 
	 * @param name The name of the variable
	 * @param data The data of the variable
	 * @throws CommandErrorException If something goes wrong
	 */
	private synchronized void addVariable(String name, String data) throws CommandErrorException
    {
		try 
		{
			PrimitiveVariable toAdd = PrimitiveVariable.makeVariable(name, data);
			if(toAdd == null)
	    		throw new CommandLoadingException("Error: Invalid variable format.");
	    	tosave.add(toAdd);
		} 
		catch (EngineException e)
		{
			throw new CommandLoadingException("Error: Corrupt variable data table.");
		}
    }
	
	/**
	 * Normal, non-saving variables
	 */
	private EList<PrimitiveVariable> vars;
	/**
	 * Saving variables to be loaded and saved
	 */
	private EList<PrimitiveVariable> tosave;
	
	private final static String FILENAME = "variables.DATA";
}