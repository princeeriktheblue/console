/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commands;

import lib.Command;
import display.Console;
import commands.exceptions.InvalidCommandException;

import java.io.File;

/**
 *
 * @author erikb
 */
public class Mkdir extends Command
{
	private final static String[] help = 
	{
		"Usage:",
	    "mkdir <path> --> creates the path given"
	};
	
    public Mkdir(Console parent)
    {
        super("mkdir", parent, null, help);
    }

    @Override
    public synchronized void execute(String[] args) throws InvalidCommandException 
    {
        File toMake = new File(args[0]);
        if(toMake.exists())
            throw new IllegalArgumentException("The directory already exists");
        if(!toMake.mkdirs())
        	console.output("Problem creating directories");
    }
}
