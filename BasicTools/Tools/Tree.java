package Tools;

import java.io.File;

import console.Command;
import console.commands.exceptions.InvalidArgumentException;
import console.commands.exceptions.InvalidCommandException;
import console.display.Console;
import lib.ESystem;

public class Tree extends Command
{
	private final static String[] help = 
	{
		""
	};
	
	public Tree(Console console) 
	{
		super("tree", console, null, help);
	}

	@Override
	public void execute(String[] args) throws InvalidCommandException 
	{
		if(args.length == 0)
			treeStart(console.getCurrentDirectory());
		else if(args.length == 1)
		{
			File f = ESystem.resolveFile(console.getCurrentDirectory(), args[0], false);
			if(f == null)
				throw new InvalidArgumentException("The file given doesn't exist");
			treeStart(f);
		}
		else
			throw new InvalidArgumentException("The command has too many arguments");
	}
	
	private synchronized void treeStart(File toTree)
	{
		if(toTree.isAbsolute())
		{
			if(console.getOSType() == ESystem.WINDOWS)
				console.output("C:/");
			else
				console.output("root");
		}
		else
			console.output(toTree.getName());
		tree(0,toTree);
	}

	private synchronized void tree(int numSpaces, File toTree)
	{
		if(!console.isFlagged())
		{
			String base = "";
			for(int i = 0; i < numSpaces; i++)
				base += '-';
			base += '-';
			File[] files = toTree.listFiles();
			for(int i = 0; i < files.length-1 && !console.isFlagged(); i++)
			{
				File e = files[i];
				console.output(base + e.getName());
				if(e.isDirectory() && e.list() != null && e.list().length > 0)
					tree(numSpaces+1, e);
			}
		}
	}
}
