/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import lib.ESystem;
import console.Command;
import console.commands.exceptions.CommandErrorException;
import console.commands.exceptions.InvalidArgumentException;
import console.commands.exceptions.UnknownErrorException;
import console.display.Console;
import console.exceptions.EException;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author erikb
 */
public final class Move extends Command
{
	private final static String[] help = 
	{
		"Usage:",
	    "move <file> <path> --> moves the file to the specific path"
	};
	
    public Move(Console parent)
    {
        super("move",parent,null, help);
    }
    
    @Override
    public synchronized void execute(String[] args) throws EException 
    {
        if(args.length == 1)
            throw new InvalidArgumentException("Not enough arguments in this command.");
        original = ESystem.resolveFile(console.getCurrentDirectory(), args[0], false);
        if(original == null)
        	throw new InvalidArgumentException("The first argument doesn't exist");
        toMoveTo = new File(args[1]);
        if(!toMoveTo.exists())
        {
	        toMoveTo = new File(console.getPath() + args[1]);
	        if(!toMoveTo.exists())
	        	throw new InvalidArgumentException("The path given doesn't exist");
	        if(!toMoveTo.isDirectory())
	        	throw new InvalidArgumentException("The path given doesn't point to a directory");
        }
        if(!toMoveTo.isDirectory())
        	throw new InvalidArgumentException("The second path doesn't point to a directory");
        File[] files = toMoveTo.listFiles();
        int count = 0;
        String name = original.getName();
        for(File e: files)
        	if(e.getName().length() >= name.length() && e.getName().substring(0, name.length()).equals(name))
                count++;
        if(count != 0)
        	doIfExists();
        ESystem.copyToFile(original, toMoveTo);
        if(!original.delete())
            throw new CommandErrorException("Unknown error prevented the source file from being removed. Copying still worked.");
    }

    /**
     * If the destination file/path exists, or if there is one argument, therefore implying a local copying procedure, the
     * user is asked whether an overwrite is allowable, or if another name should be selected for the given file.
     * 
     * @return The appropriate response to the execute function
     */
    private synchronized void doIfExists() throws EException
    {
        File[] filesInDir = toMoveTo.getParentFile().listFiles();
        int index = original.getName().indexOf('.');
        String baseName = (index == -1 ? original.getName() : original.getName().substring(0, index));
        int counter = 0;
        for(File e: filesInDir)
            if(e.getName().length() >= baseName.length() && e.getName().substring(0, baseName.length()).equals(baseName))
                counter++;
        nonOverWrite = new File(index == -1 ? baseName + '(' + counter + ')' : original.getName().replace(".", "("+counter+")."));
        console.output("Do you want to overwrite the currently existing file? Yes, no, or cancel.");
        String userResponse = console.input();
        if(userResponse.equalsIgnoreCase("yes") || userResponse.equalsIgnoreCase("y"))
        {
            if(toMoveTo.exists())
                if(!toMoveTo.delete())
                    throw new CommandErrorException("Error while overwriting the file");
            try
            {
            	toMoveTo.createNewFile();
            }
            catch(IOException e)
            {
                throw new UnknownErrorException("Error creating file. Operation unsuccessful");
            }
            ESystem.copyToFile(original, toMoveTo);
        }
        else if(userResponse.equalsIgnoreCase("no") || userResponse.equalsIgnoreCase("n"))
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
        else if(userResponse.equalsIgnoreCase("cancel") || userResponse.equalsIgnoreCase("c"))
            console.output("Move cancelled");
    }
    
    private File original;
    private File toMoveTo;
    private File nonOverWrite;
}