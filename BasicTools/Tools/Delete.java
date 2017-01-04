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

package Tools;

import java.io.File;

import console.Command;
import console.commands.exceptions.InvalidArgumentException;
import console.commands.exceptions.InvalidCommandException;
import console.commands.exceptions.InvalidSyntaxException;
import console.display.Console;
import lib.ESystem;

/**
 *
 * @author Erik Belluomini
 */
public final class Delete extends Command
{
	private final static String[] help = 
	{
			"Usage:",
	        "delete [path] --> deletes the file in the given path"
	};
	
	/**
	 * 
	 * @param con The console calling the command
	 */
    public Delete(Console con)
    {
        super("delete",con, null, help);
    }

    @Override
    public synchronized void execute(String[] args) throws InvalidCommandException 
    {
    	if(args.length != 1)
    		throw new InvalidSyntaxException("Too many arguments for the delete command", InvalidSyntaxException.SHOW_ALL);
        File file = ESystem.resolveFile(console.getCurrentDirectory(), args[0], false);
        if(file == null)
        	throw new InvalidArgumentException("The file asked for doesn't exist");
        if(!file.delete())
        	console.output("An unknown error stopped the file from being deleted");
    }
}
