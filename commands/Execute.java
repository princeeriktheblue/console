/**
 * NOTICE: THIS CODE IS PROVIDED FOR USE AS IS, AND NOT WITH ANY MODIFICATIONS BY A USER
 * WHICH IS NOT APPROVED OF BY THE PROVIDER. I HIGHLY RECOMMEND YOU DO NOT EDIT THIS
 * CODE AND RUN IT UNLESS YOU KNOW WHAT YOU ARE DOING. AS THIS CODE PLAYS WITH FILES
 * AND STRUCTURES INSIDE THE COMPUTER, ANY EDITS OF THIS CODE MAY RESULT IN DATA LOSS
 * OR CORRUPTION OF THE HOST SYSTEM. DISCRETION IS ADVISED. 
 * 
 * I AM NOT RESPONSIBLE FOR ANY DAMAGES, FOR ANY VIOLATIONS OF LAW, OR ANY SORT OF LEGAL
 * PUNISHMENT FROM THE FOLLOWING CODE. ALL USE OF THIS CODE IS SUBJECT TO THE USER IN
 * ENTIRETY.
 * 
 */

package commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import lib.Command;
import lib.ESystem;
import display.Console;
import commands.exceptions.InvalidArgumentException;
import commands.exceptions.InvalidCommandException;
import commands.exceptions.InvalidSyntaxException;

/**
 *
 * @author Erik Belluomini
 */
public class Execute extends Command
{
	private final static String[] help = 
	{
			"Execute:",
			"-x [path] --> Executes the executable with the given path",
			"-j [path] --> Executes the jar with the given path",
			"-e [path] --> Loads the console with the execution orders from the ecm file"
	};
	
	/**
	 * 
	 * @param parent
	 */
	public Execute(Console parent) 
	{
		super("execute", parent, new char[]{'x', 'j', 'e'}, help);
	}

	/**
	 * 
	 */
	@Override
	public synchronized void execute(String[] args) throws InvalidCommandException 
	{
		if(args.length != 2)
			throw new InvalidSyntaxException("There needs to be 3 arguments", InvalidSyntaxException.SHOW_ALL);
		int code = -1;
		if(args[0].length() < 2)
			throw new InvalidSyntaxException("The modifier given isn't valid", InvalidSyntaxException.SHOW_ALL);
		char mod = args[0].charAt(1);
		for(int i = 0; i < modifiers.length && code == -1; i++)
			if(mod == modifiers[i])
				code = i;
		switch(code)
		{
			case 0:
			{
				
				break;
			}
			case 1:
			{
				
				break;
			}
			case 2:
			{
				File temp = ESystem.resolveFile(console.getCurrentDirectory(), args[1], true);
				try 
				{
					Scanner scan = new Scanner(temp);
					ArrayList<String> data = new ArrayList<>();
					while(scan.hasNextLine())
						data.add(scan.nextLine());
					String[] lines = new String[data.size()];
					for(int i = 0; i < data.size(); i++)
						lines[i] = data.get(i);
					console.executeLinesFromFile(lines);
					scan.close();
				} 
				catch (FileNotFoundException e) 
				{
					throw new InvalidArgumentException("File doesn't exist");
				}
			}
			default:
			{
				throw new InvalidArgumentException("The given modifier isn't valid for the operation execute");
			}
		}
	}
}
