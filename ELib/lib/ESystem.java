package lib;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import lib.io.EFileReader;
import lib.io.EFileWriter;

public final class ESystem 
{
	public final static String escape = "\\";
	public final static String newline = "\n";
	public final static String tab = "\t";
	public final static char[] disallowed = {'<','>',':','"','|','/','\\','?','*'};
    public final static String disList = "'<','>',':','\"','|','/','\\','?','*'";
    public final static int INVALID_ANSWER = 0xffffff;
    public final static int NO_ANSWER = 0x0;
    public final static int YES_ANSWER = 0x1;
    public final static int CANCEL_ANSWER = 0x2;
    public final static int NONBINARY_ANSWER = 0x3;
    public final static int YES_NO_ANSWER = 0x4;
    public final static int YES_NO_CANCEL_ANSWER = 0x5;
    public final static int ANY_ANSWER = 0x6;
    public static final int WINDOWS = 0x10;
	public static final int MAXOS = 0x11;
	public static final int LINUX = 0x12;
	public static final int UNDEFINED = 0x13;
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
    
    public final static synchronized boolean createLocalFolder(String folderName)
    {
    	File parent = new File(System.getProperty("user.dir"));
    	File toCreate = new File(parent.getPath() + "/" + folderName + "/");
    	if(!toCreate.exists())
    		return toCreate.mkdir();
    	return true;
    }
	
    public final static synchronized boolean createLocalFile(String fileName)
    {
    	File parent = new File(System.getProperty("user.dir"));
    	File toCreate = new File(parent.getPath() + "/" + fileName);
    	if(!toCreate.exists())
    	{
			try 
    		{
				return toCreate.createNewFile();
			} 
    		catch (IOException e) 
    		{
				e.printStackTrace();
				return false;
			}
    	}
    	return false;
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
	
	public static synchronized boolean isInQuotes(String str, int index)
	{
		boolean inQuotes = false;
		for(int i = 0; i < index; i++)
			if(str.charAt(i) == 92)
				inQuotes = !inQuotes;
		return inQuotes;
	}
	
	public static synchronized void copyToFile(File original, File newCopy) throws IOException
    {
        if(original.isDirectory())
        {
            boolean made = newCopy.mkdir();
            if(made)
            {
                File[] files = original.listFiles();
                if(files == null)
                    throw new IOException("Problem while attempting to access children in a directory");
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
                throw new IOException("Error while creating necessary directories. Terminating");
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
                throw new IOException("Critical failure while trying to move data from one file to another");
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
