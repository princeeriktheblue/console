package engine;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

import engine.lang.ObjectVariable;
import engine.lang.PrimitiveVariable;
import engine.lang.Variable;
import engine.lang.exceptions.EngineException;
import engine.lang.exceptions.EngineLoadingException;
import engine.lang.exceptions.InvalidAccessException;
import engine.lang.exceptions.InvalidComparisonException;
import engine.lang.exceptions.MalformedFunctionCallException;
import engine.lang.exceptions.MalformedStatementException;
import engine.lang.exceptions.MalformedVariableException;
import engine.lang.exceptions.MethodException;
import engine.lang.exceptions.VariableFormatException;
import lib.datatypes.EList;
import lib.io.EFileReader;

public class Engine extends Thread implements EngineImp
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
	public static final String _or = "or";
	public static final String _and = "and";
	public static final String _not = "not";
	public static final String _greaterThan = ">";
	public static final String _lessThan = "<";
	public static final String _greatEqual = "<=";
	public static final String _lessEqual = ">=";
	public static final String _equals = "==";
	public static final String _nEquals = "~=";
	public static final String _local = "local";
	public static final String _memberOperator = ".";
	
	public static final Logger log = Logger.getLogger("EngineLogger");
	
	public static final int GREATERTHAN = 1;
	public static final int LESSTHAN = -1;
	public static final int EQUAL = 0;
	public static final int NOTEQUAL = -2;
	
	public Engine()
	{
		globalFunctions = new EList<>();
		globalVariables = new EList<>();
		variableTypes = new EList<>();
		String path = System.getProperty("user.dir");
		ObjectLoader loader = new ObjectLoader(path);
		try
		{
			variableTypes = loader.loadAll();
			System.out.println("WORKED");
		}
		catch(EngineLoadingException e)
		{
			e.printStackTrace();
		}
	}
	
	public final void run(File[] args)
	{
		for(File e: args)
		{
			try
			{
				EList<String> str = EFileReader.readAllLines(e.toPath());
				ExecutionThread thread = new ExecutionThread(str);
				threads.add(thread);
			}
			catch(IOException f)
			{}
		}
		for(ExecutionThread e: threads)
			e.start();
	}
	
	public synchronized final void executeLine(EList<Variable> varsInContext, String[] args)
	{
		synchronized(this)
		{
			for(String e: args)
			{
				
			}
		}
	}
	
	private final String[] simplifyStatement(String[] data)
	{
		EList<String> toReturn = new EList<>();
		for(String e: data)
		{
			int last = 0;
			for(int i = 1; i < e.length()-1; i++)
			{
				char ch = e.charAt(i);
				if(ch == '<')
				{
					if(e.charAt(i+1) == '=')
					{
						toReturn.add(e.substring(last, i));
						toReturn.add(e.substring(i, i+2));
						i = last;
					}
					else
					{
						toReturn.add(e.substring(last, i));
						toReturn.add(e.substring(i, i+1));
						last = i+2;
						i = last;
					}
				}
				else if(ch == '>')
				{
					if(e.charAt(i+1) == '=')
					{
						
					}
					else
					{
						
					}
				}
				else if(ch == '=')
				{
					if(e.charAt(i+1) == '=')
					{
						
					}
					else
					{
						
					}
				}
				else if(ch == '.')
				{
					
				}
			}
			if(last != e.length()-1)
				toReturn.add(e.substring(last));
			else if(last == 0)
			{
				
			}
		}
		return (String[])toReturn.toArray();
	}
	
	private final void executeLines(String[] args) throws EngineException
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
					throw new MalformedStatementException("Error: No then keyword found.");
				String[] data = new String[end- i - 2];
				int counter = 0;
				for(int j = i+1; j < end; j++)
					data[counter++] = tempList.get(j);
				boolean isTrue = boolStatement(simplifyStatement(data));
				if(isTrue)
				{
					
				}
			}
		}
	}
	
	private final boolean boolStatement(String[] args) throws EngineException
	{
		Variable prev = null;
		boolean condition = false;
		String lastLogicOp = _or;
		String lastCompOp = null;
		boolean lastIsLogic = true;
		boolean negate = false;
		for(int i = 0; i < args.length; i++)
		{
			String currentData = args[i];
			if(isLogicOp(currentData))
			{
				if(currentData.equals(_not))
					negate = true;
				else
				{
					if(currentData.equals(_and))
					{
						if(!condition)
							return false;
						
					}
					else if(currentData.equals(_or))
					{
						if(condition)
							return true;
					}
					lastIsLogic = true;
					lastLogicOp = currentData;
				}
			}
			else if(isComparator(currentData))
			{
				lastCompOp = currentData;
				lastIsLogic = false;
			}
			else if(isLogicStatement(currentData))
			{
				boolean statement = currentData.equals(_true);
				if(lastIsLogic)
				{
					if(negate)
					{
						statement = !statement;
						negate = false;
					}
					condition = combine(condition, statement, lastLogicOp);
				}
				else
				{
					if(lastCompOp.equals(_equals))
						condition = condition == statement;
					else if(lastCompOp.equals(_nEquals))
						condition = condition != statement;
					else
						throw new MalformedStatementException();
				}
			}
			else if(prev == null)
			{
				try 
				{
					prev = PrimitiveVariable.makeVariable(null, currentData);
				} 
				catch (VariableFormatException e) 
				{
					throw new MalformedVariableException("Error: " + currentData + "cannot be parsed as a variable");
				}
			}
			else
			{
				try
				{
					Variable current = PrimitiveVariable.makeVariable(null, currentData);
					if(lastCompOp == null)
						throw new MalformedStatementException("No comparator found between variables");
					boolean isTrue = compare(prev, current, lastCompOp);
					if(lastLogicOp != null)
						condition = combine(condition, isTrue, lastLogicOp);
				}
				catch (VariableFormatException e) 
				{
					throw new MalformedVariableException("Error: " + args[1] + "cannot be parsed as a variable");
				}
				catch(InvalidComparisonException e)
				{
					throw new MalformedStatementException("Error: the statement is not valid");
				}
			}
		}
		return condition;
	}
	
	private final boolean isComparator(String str)
	{
		return str.equals(_lessEqual) || str.equals(_greatEqual) || str.equals(_greaterThan) || str.equals(_lessThan) || 
				str.equals(_equals) || str.equals(_nEquals);
	}
	
	private final boolean isLogicOp(String str)
	{
		return str.equals(_and) || str.equals(_or) || str.equals(_not);
	}
	
	private final boolean isLogicStatement(String str)
	{
		return str.equals(_true) || str.equals(_false);
	}
	
	private final String doMemberOperation(EList<Variable> vars, String object, String member, String[] args) 
			throws MethodException, InvalidAccessException, MalformedVariableException
	{
		Iterator<Variable> it = vars.iterator();
		while(it.hasNext())
		{
			Variable temp = it.next();
			if(temp.getName().equals(object))
				if(temp instanceof ObjectVariable)
					return ((ObjectVariable)temp).memberAccess(member, args);
				else
					throw new InvalidAccessException();
		}
		it = globalVariables.iterator();
		while(it.hasNext())
		{
			while(it.hasNext())
			{
				Variable temp = it.next();
				if(temp.getName().equals(object))
					if(temp instanceof ObjectVariable)
						return ((ObjectVariable)temp).memberAccess(member, args);
					else
						throw new InvalidAccessException();
			}
		}
		throw new MalformedVariableException();
	}
	
	private final boolean compare(Variable a, Variable b, String op) throws InvalidComparisonException
	{
		int score = a.compareTo(b);
		if(score == Engine.EQUAL)
			return op.equals(_equals) || op.equals(_greatEqual) || op.equals(_lessEqual);
		else if(score == Engine.GREATERTHAN)
			return op.equals(_greatEqual) || op.equals(_greaterThan);
		else if(score == Engine.LESSTHAN)
			return op.equals(_lessEqual) || op.equals(_lessThan);
		else
			throw new InvalidComparisonException("The two given variables are not comparable");
	}
	
	private final boolean combine(boolean a, boolean b, String op)
	{
		if(op.equals(_or))
			return a || b;
		else
			return a && b;
	}
	
	public final ObjectVariable createInstance(String name)
	{
		try
		{
			for(Class<? extends ObjectVariable> e: variableTypes)
				if(e.getName().equals(name))
					return e.newInstance();
			}
		catch(IllegalAccessException | InstantiationException e)
		{}
		return null;
	}
	
	private final Function getGlobalFunction(String name)
	{
		for(Function e: globalFunctions)
			if(e.getName().equals(name))
				return e;
		return null;
	}
	
	private final Variable getGlobalVariable(String name)
	{
		for(Variable e: globalVariables)
			if(e.getName().equals(name))
				return e;
		return null;
	}
	
	private final class ExecutionThread extends Thread
	{
		public ExecutionThread(EList<String> args)
		{
			this.args = args;
			try
			{
				buildFunctions();
			}
			catch(MalformedFunctionException e1)
			{
				System.out.println("ERROR IN BUILD: SYNTAX FOR FUNCTION ISN'T CORRECT");
			}
		}
		
		public void run()
		{
			
		}
		
		private void buildFunctions() throws MalformedFunctionException
		{
			for(int i = 0; i < args.size(); i++)
			{
				String str = args.get(i);
				if(str.contains(_func))
				{
					int index = str.indexOf(_func) + _func.length();
					if(str.charAt(index+1) == '(')
					{
						
					}
					else
					{
						
					}
					String name = "";
					for(int j = index; j < str.length(); j++)
					if(index == -1 || index > str.length() || str.charAt(index) != '(')
						throw new MalformedFunctionException();
					int end = str.indexOf(')', index);
					if(end == -1)
						throw new MalformedFunctionException();
					String arguments = str.substring(index+1, end);
					boolean local = false;
					if(str.substring(0, index).trim().equals(_local))
						local = true;
					int endNeeded = 0;
					int endIndex = -1;
					for(int j = i+1; j < args.size() && endIndex == -1; j++)
					{
						String temp = args.get(j);
						if(temp.contains(_if))
							endNeeded++;
						else if(temp.contains(_while))
							endNeeded++;
						else if(temp.contains(_end))
						{
							endNeeded--;
							if(endNeeded == -1)
								endIndex = j;
						}
					}
					if(endIndex == -1)
						throw new MalformedFunctionException();
				}
			}
		}
		
		private void buildFunction(String name, String args, String[] body, boolean isLocal)
		{
			
		}
		
		private volatile EList<Function> funcs;
		private volatile EList<Variable> inContext;
		private final EList<String> args;
	}
	
	public class Function 
	{
		public Function(String name, String[] argNames, String[] body)
		{
			this.name = name;
			this.argNames = argNames;
			this.body = body;
		}
		
		public final void execute(Variable[] args) throws MalformedFunctionCallException
		{
			if(args.length != argNames.length)
			{
				
			}
		}
		
		public final String getName()
		{
			return name;
		}

		private final String name;
		private final String[] argNames;
		private final String[] body;
	}
	
	private EList<Variable> globalVariables;
	private EList<Function> globalFunctions;
	private EList<ExecutionThread> threads;
	private EList<Class<ObjectVariable>> variableTypes;
}
