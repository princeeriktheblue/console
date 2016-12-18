package commands;

import commands.exceptions.InvalidCommandException;
import display.Console;
import lib.Command;
import lib.math.MathLib;

public class EMath extends Command
{
	private final static String[] help = 
	{
			"Math:",
			"Math(args) --> Calculate args to a decimal point number",
			"Modifiers:",
			"		-r --> rounds the number to an integer",
			"Functions:",
			"		round(x) --> returns the rounded value of x",
			"		ln(x) --> returns the natural log of x",
			"		log(b, x) --> returns the value of log base b of x",
			"		fact(x) --> returns the factorial of x",
			"		abs(x) --> returns the absolute value of x",
			"		root(x, y) --> returns the y root of x"
	};
	
	public EMath(Console parent) 
	{
		super("math", parent, null, help);
	}

	@Override
	public synchronized void execute(String[] args) throws InvalidCommandException 
	{
		String input = "";
		for(String e : args)
			input+=e;
		console.output("" + MathLib.calculate(input));
	}
}