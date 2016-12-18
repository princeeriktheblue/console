package commands;

import java.util.ArrayList;
import java.util.Calendar;

import lib.Command;
import display.Console;
import commands.exceptions.InvalidCommandException;

public class Time extends Command implements Runnable
{
	private final static String[] help = 
	{
		"Needs improvement"
	};

	public Time(Console console) 
	{
		super("time", console, new char[]{}, help);
	}

	@Override
	public void execute(String[] args) throws InvalidCommandException 
	{
		
	}

	@Override
	public void run() 
	{
		
	}

	@SuppressWarnings("unused")
	private ArrayList<TimeEvent> events;
	
	private final class TimeEvent
	{
		@SuppressWarnings("unused")
		public TimeEvent()
		{}
		
		@SuppressWarnings("unused")
		private Command execute;
		@SuppressWarnings("unused")
		private String name;
		@SuppressWarnings("unused")
		private Calendar time;
	}
}
