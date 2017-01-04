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

import lib.ESystem;
import lib.datatypes.PromptResponse;
import console.Command;
import console.commands.exceptions.CommandErrorException;
import console.commands.exceptions.InvalidArgumentException;
import console.commands.exceptions.InvalidCommandException;
import console.commands.exceptions.InvalidSyntaxException;
import console.commands.exceptions.UnknownErrorException;
import console.display.Console;
import console.exceptions.EException;

import java.io.File;
import java.io.IOException;

/**
 * This class is used to copy entire files and/or file directories from one location to either
 * another location, or the same path. This command also prompts the user whether to overwrite
 * or not to overwrite any existing files.
 * 
 * @author Erik Belluomini
 */
public final class Copy extends Command
{
	private final static String[] help = 
		{
			"Usage: ",
		    "copy <path> --> creates a file/directory in the current directory with the given name ",
		    "copy <source> <destination> copies the source directory/file to the destination directory"
		};
	
    /**
     * Constructs the copy class
     * 
     * @param parent The console calling for this command
     */
    public Copy(Console parent)
    {
        super("copy",parent, null, help);
    }

    /**
     * This method is called when the user inputs the command.
     * 
     * @param line The user input for this command
     * @return The output to display. It returns an empty string if prompt is given, or returns information for the user.
     * @throws InvalidCommandException When the arguments given aren't valid for the operation.
     */
    @Override
    public synchronized void execute(String[] args) throws EException 
    {
    	if(args.length > 2)
    		throw new InvalidSyntaxException("The copy command doesn't allow for more than two arguments", InvalidSyntaxException.SHOW_ALL);
        if(args.length != 1)
        {
        	original = ESystem.resolveFile(console.getCurrentDirectory(), args[0], false);
        	toCopyTo = ESystem.resolveFile(console.getCurrentDirectory(), args[1], true);
        	if(original == null)
        		throw new InvalidArgumentException("The source file doesn't exist for the copy command");
            boolean isDirectory = original.isDirectory();
            if(!toCopyTo.exists())
            {
            	toCopyTo = new File(console.getPath() + args[1]);
            	if(!toCopyTo.exists())
            	{
	            	if(toCopyTo.getParentFile() == null || !toCopyTo.getParentFile().exists())
	            		throw new InvalidArgumentException("The second argument doesn't exist");
	            	if(isDirectory && !toCopyTo.isDirectory())
	            		throw new InvalidArgumentException("The second argument needs to be a directory if the first argument is one");
	            }
            	else
            		doIfExists();
            }
            else
                doIfExists();
        }
        else
        {
        	original = ESystem.resolveFile(console.getCurrentDirectory(), args[0], false);
            if(!original.exists())
                throw new InvalidArgumentException("The first argument doesn't exist");
            String name = original.getName();
            if(name.indexOf('.') != -1)
                name = name.substring(0, name.indexOf('.'));
            if(name.indexOf('(') != -1)
                name = name.substring(0, name.indexOf('('));
            int counter = 0;
            for(File e: original.getParentFile().listFiles())
                if(e.getName().length() >= name.length() && e.getName().substring(0, name.length()).equals(name))
                    counter++;
            String newName = original.getName();
            int index = newName.indexOf('.');
            if(index != -1)
                newName = newName.substring(0, index) + "(" + counter + ")" + newName.substring(index);
            else
                newName += "("+counter+")";
            String newPath = original.getParentFile().getPath();
            if(newPath.charAt(newPath.length()-1) != '/')
                newPath+= "/";
            toCopyTo = new File(newPath + newName);
        }
        ESystem.copyToFile(original, toCopyTo);
    }
    
    /**
     * If the destination file/path exists, or if there is one argument, therefore implying a local copying procedure, the
     * user is asked whether an overwrite is allowable, or if another name should be selected for the given file.
     * 
     * @return The appropriate response to the execute function
     */
    private synchronized void doIfExists() throws EException
    {
        File[] filesInDir = toCopyTo.getParentFile().listFiles();
        int index = original.getName().indexOf('.');
        String baseName = (index == -1 ? original.getName() : original.getName().substring(0, index));
        int counter = 0;
        for(File e: filesInDir)
            if(e.getName().length() >= baseName.length() && e.getName().substring(0, baseName.length()).equals(baseName))
                counter++;
        nonOverWrite = new File(index == -1 ? baseName + '(' + counter + ')' : original.getName().replace(".", "("+counter+")."));
        PromptResponse answer = console.prompt("Do you want to overwrite the currently existing file?"
        		, false, false, ESystem.YES_NO_CANCEL_ANSWER);
        if(answer.getResponse() == ESystem.YES_ANSWER)
        {
            if(toCopyTo.exists())
                if(!toCopyTo.delete())
                    throw new CommandErrorException("Error while overwriting the file");
            try
            {
                toCopyTo.createNewFile();
            }
            catch(IOException e)
            {
                throw new UnknownErrorException("Error creating file. Operation unsuccessful");
            }
            ESystem.copyToFile(original, toCopyTo);
        }
        else if(answer.getResponse() == ESystem.NO_ANSWER)
        {
            try
            {
                nonOverWrite.createNewFile();
            }
            catch(IOException e)
            {
                throw new UnknownErrorException("Error creating file. Operation unsuccessful");
            }
            ESystem.copyToFile(original, nonOverWrite);
        }
        else if(answer.getResponse() == ESystem.CANCEL_ANSWER)
            console.output("\nCopy cancelled");
    }
    
    private File original;
    private File toCopyTo;
    private File nonOverWrite;
}