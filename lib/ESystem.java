package lib;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import commands.exceptions.CommandErrorException;
import commands.exceptions.EException;
import commands.exceptions.InvalidArgumentException;
import commands.exceptions.InvalidCommandException;
import commands.exceptions.UnknownErrorException;
import lib.datatypes.EList;
import lib.datatypes.variables.DoubleVariable;
import lib.datatypes.variables.IntegerVariable;
import lib.datatypes.variables.StringVariable;
import lib.datatypes.variables.TableVariable;
import lib.datatypes.variables.Variable;
import lib.io.EFileReader;
import lib.io.EFileWriter;

public final class ESystem 
{
	public final static String escape = "\\";
	public final static String newline = "\n";
	public final static String tab = "\t";
	public final static char[] disallowed = {'<','>',':','"','|','/','\\','?','*'};
    public final static String disList = "'<','>',':','\"','|','/','\\','?','*'";
    public final static int INVALID_ANSWER = -1;
    public final static int NO_ANSWER = 0;
    public final static int YES_ANSWER = 1;
    public final static int CANCEL_ANSWER = 2;
    public final static int NONBINARY_ANSWER = 3;
    public final static int YES_NO_ANSWER = 4;
    public final static int YES_NO_CANCEL_ANSWER = 5;
    public final static int ANY_ANSWER = 6;
    public final static String[] colorNames = {"BLACK","BLUE","CYAN","DARK_GREY","GREY","GREEN","LIGHT_GREY",
    		"MAGENTA","ORANGE","PINK","RED","WHITE","YELLOW"};
    public final static Color[] colors = {Color.BLACK,Color.BLUE,Color.CYAN,Color.DARK_GRAY,Color.GRAY,Color.GREEN,
    		Color.LIGHT_GRAY,Color.MAGENTA,Color.ORANGE,Color.PINK,Color.RED,Color.WHITE,Color.YELLOW};
    
    public final static synchronized boolean isValid(String str)
    {
    	for(int i = 0; i < str.length(); i++)
    	{
    		char ch = str.charAt(i);
    		if(ch < 32)
    			return false;
    		for(int j = 0; j < disallowed.length; j++)
    			if(ch == disallowed[j])
    				return false;
    	}
    	return true;
    }
    
    public final static int getAnswer(String in, int state)
    {
    	if(in.equalsIgnoreCase("yes") || in.equalsIgnoreCase("y"))
            return YES_ANSWER;
        else if(in.equalsIgnoreCase("no") || in.equalsIgnoreCase("n"))
        	return NO_ANSWER;
        else if(in.equalsIgnoreCase("cancel") || in.equalsIgnoreCase("c"))
        	return state == YES_NO_CANCEL_ANSWER ? CANCEL_ANSWER : INVALID_ANSWER;
        else if(state == ANY_ANSWER)
        	return NONBINARY_ANSWER;
        else
        	return INVALID_ANSWER;
    }
    
    public final static String getValidAnswersForState(int state)
    {
    	switch(state)
    	{
    		case YES_NO_ANSWER:
    			return "(yes, y) or (no, n)";
    		case YES_NO_CANCEL_ANSWER:
    			return "(yes, y) or (no, n) or (c, cancel)";
    		default:
    			return null;
    	}
    }
    
    public final static synchronized File getLocalFile(String name)
	{
		File[] files = new File(System.getProperty("user.dir")).listFiles();
		for(File e: files)
			if(e.getName().equals(name))
				return e;
		return null;
	}
	
	/**
	 * Takes in a path and returns a file object depicting the path given
	 * 
	 * @param input The path to a file
	 * @return A <code>File</code> representation of the path, if it exists. Else returns <code>null</code>
	 */
	public final static synchronized File resolveFile(File currentDir, String input, boolean returnAbstract)
	{
		System.out.println(input);
		if(input.equals("..")) 
			return currentDir.getParentFile();
		String path = getNaturalPath(currentDir);
		if(input.charAt(0) != '/' && path.charAt(path.length()-1) != '/')
			path += '/';
		File temp = new File(path + input);
		if(temp.exists())
			return temp;
		temp = new File(input);
		if(temp.exists())
			return temp;
		return returnAbstract ? temp : null;
	}
	
	public final static synchronized String getNaturalPath(File currentDir)
	{
		String toMod = currentDir.getAbsolutePath();
        for(int i = 0; i < toMod.length(); i++)
            if(toMod.charAt(i) == 92)
                toMod = toMod.substring(0, i) + '/' + toMod.substring(i+1);
        if(toMod.charAt(toMod.length()-1) != '/')
            toMod += '/';
        return toMod;
	}
	
	public static synchronized Variable makeVariable(String name, String str) throws InvalidCommandException
	{
		byte type = detectDataType(str);
		Variable var = null;
		if(type == -1) 
			throw new InvalidArgumentException();
		switch(type)
		{
			case 0:
			{
				try
				{
					Integer in = Integer.parseInt(str);
					var = new IntegerVariable(name);
					var.setData(in);
				}
				catch(NumberFormatException e)
				{
					throw new InvalidCommandException();
				}
				break;
			}
			case 1:
			{
				try
				{
					Double in = Double.parseDouble(str);
					var = new DoubleVariable(name);
					var.setData(in);
				}
				catch(NumberFormatException e)
				{
					throw new InvalidCommandException();
				}
				break;
			}
			case 2:
			{
				var = new StringVariable(name);
				var.setData(str);
				break;
			}
			case 3:
			{
				String[] split = str.substring(1, str.length()-1).split(",");
				EList<Variable> list = new EList<>();
				for(String e: split)
				{
					Variable temp = makeVariable(null, e);
					list.add(temp);
				}
				var = new TableVariable(name);
				var.setData(list);
				break;
			}
			default:
			{
				throw new InvalidArgumentException("Something didn't register");
			}
		}
		return var;
	}
	
	public static synchronized byte detectDataType(String str)
	{
		int startTable = str.indexOf('{');
		int endTable = str.lastIndexOf('}');
		int dot = str.indexOf('.');
		if(startTable != -1 && !isInQuotes(str, startTable) && !isInQuotes(str, endTable))
		{
			if(endTable == -1)
				return -1;
			return 3;
		}
		else if(str.indexOf(92) != -1)
		{
			if(str.lastIndexOf(92) == -1)
				return -1;
			return 2;
		}
		try
		{
			if(dot != -1)
			{
				if(str.lastIndexOf('.') != dot)
					return -1;
				Double.parseDouble(str);
				return 1;
			}
			else
			{
				Integer.parseInt(str);
				return 0;
			}
		}
		catch(NumberFormatException e)
		{
			return -1;
		}
	}
	
	public static synchronized boolean isInQuotes(String str, int index)
	{
		boolean inQuotes = false;
		for(int i = 0; i < index; i++)
			if(str.charAt(i) == 92)
				inQuotes = !inQuotes;
		return inQuotes;
	}
	
	public static synchronized void copyToFile(File original, File newCopy) throws EException
    {
        if(original.isDirectory())
        {
            boolean made = newCopy.mkdir();
            if(made)
            {
                File[] files = original.listFiles();
                if(files == null)
                    throw new CommandErrorException("Problem while attempting to access children in a directory");
                for(File e: files)
                {
                    String path = newCopy.getPath();
                    if(path.charAt(path.length()-1) != '/')
                        path+="/";
                    File nextToWrite = new File(path + e.getName());
                    copyToFile(e, nextToWrite);
                }
            }
            else
                throw new CommandErrorException("Error while creating necessary directories. Terminating");
        }
        else
        {
            EFileWriter writer = new EFileWriter(newCopy, false);
            try
            {
            	writer.open();
                for (String toAdd : EFileReader.readAllLines(Paths.get(original.getAbsolutePath())))
                    for (String part : toAdd.split("\n")) 
                        writer.write(part);
            }
            catch(IOException e)
            {
                throw new UnknownErrorException("Critical failure while trying to move data from one file to another");
            }
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
	 * Takes either 3 integer values or a string value to determine a color from the <code>Color</code> class. 
	 * These integers must be from a value of 0 - 255 each. Otherwise, color presets that work are as follows:
	 * <br>
	 * BLACK, BLUE, CYAN, DARK_GREY, GREY, GREEN, LIGHT_GREY, MAGENTA, ORANGE, PINK, RED, WHITE, YELLOW
	 * 
	 * @param data User input for a color
	 * @return The <code>Color</code> object that represents the data, otherwise return <code>null</code>
	 */
	public static synchronized Color getColor(String data)
	{
		if(data.indexOf(',') != -1)
		{
			int[] values = new int[3];
			boolean valid = true;
			int last = 0;
			for(int i = 0; i < values.length && valid; i++)
			{
				int com = data.indexOf(',');
				String val;
				if(i < 2 && com != -1)
				{
					val = data.substring(last,com).trim();
					last = com+1;
				}
				else
					val = data.substring(last);
				try
				{
					values[i] = Integer.parseInt(val);
				}
				catch(NumberFormatException e)
				{
					return null;
				}
			}
			return new Color(values[0], values[1], values[2]);
		}
		else 
		{
			if(data.indexOf(' ') != -1)
				data = data.replace(' ', '_');
			for(int i = 0; i < colors.length; i++)
				if(colorNames[i].equalsIgnoreCase(data))
					return colors[i];
			return null;
		}
	}
}
