package lib.lang;

import commands.exceptions.EException;
import commands.exceptions.InvalidSyntaxException;
import display.Console;
import lib.datatypes.EList;

public class Engine 
{
	public static final String[] keyworkds = {"if", "then", "else", "elseif", "return", "end", "while", "do", "for", "func", "true", "false"};
	public static final String _if = "if";
	public static final String _then = "then";
	public static final String _else = "else";
	public static final String _elseif = "elseif";
	public static final String _return = "return";
	public static final String _end = "end";
	public static final String _while = "while";
	public static final String _do = "do";
	public static final String _for = "for";
	public static final String _func = "func";
	public static final String _true = "true";
	public static final String _false = "false";
	
	private Engine(Console con)
	{
		//this.con = con;
	}
	
	public final void run(String[] args) throws EException
	{
		EList<String> tempList = new EList<>();
		for(String e: args)
			tempList.add(e);
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals(_if))
			{
				int end = -1;
				for(int j = i + 1; j < args.length && end == -1; j++)
					if(args[j] == _then)
						end = j;
				if(end == -1)
					throw new InvalidSyntaxException("Error: No then keyword found.");
				String[] data = new String[end- i - 2];
				int counter = 0;
				for(int j = i+1; j < end; j++)
					data[counter++] = tempList.get(j);
				ifStatement(data);
			}
		}
	}
	
	private final boolean ifStatement(String[] args)
	{
		return true;
	}
	
	//private final Console con;
}
