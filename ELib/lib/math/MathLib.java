package lib.math;

import lib.exceptions.MathInputException;

/**
 * This is the reference class for all math related functions which are to be
 * determined in EBOS. This class may be extended for different specifications
 * of math which are not covered in this class. This class is for basic math
 * and arithmetic in general.
 * 
 * Tolerance of these equations is dynamically increased with the given complexity
 * of the numbers, as to give an accurate number every time without need of input
 * for tolerance by the user. 
 * 
 * @author Erik [REDACTED]
 *
 */
public class MathLib 
{
	
	/**
	 * This function takes the root of any given number if the
	 * equation is mathematically acceptable.
	 * 
	 * 
	 * @param x The number under the root
	 * @param y The root number
	 * @return The value of the root
	 */
	public static double root(double x, double y)
	{
		assert y > 0;
		if(y % 2 != 1)
			assert x >= 0;
		double guess = x/y;
		int numTimes = 4;
		double num = x;
		while(num > 10)
		{
			numTimes +=2;
			num /= 10;
		}
		for(int i = 0; i < numTimes; i++)
		{
			double newGuess = guess = guess - (Math.pow(guess, y) - x) / (y*guess);
			if(newGuess == guess)
				return guess;
			guess = newGuess;
		}
		return guess;
	}
	
	/**
	 * Takes a number and converts it to the power given.
	 * 
	 * @param x The number
	 * @param y The exponent
	 * @return The value
	 */
	public static double pow(double x, int y)
	{
		assert y >= 0;
		double toReturn = 1.0;
		for(int i = 0; i < y; i++)
			toReturn = toReturn * x;
		return toReturn;
	}
	
	/**
	 * Returns the logarithmic value of the given number with the log base as 10.
	 * 
	 * @param number The number inside the log
	 * @return The value
	 */
	public static double log10(double x)
	{
		return Math.log(x) / Math.log(10);
	}
	
	/**
	 * Returns the factorial up until that number, inclusively
	 * 
	 * @param num The largest number in the factorial
	 * @return The value of the factorial
	 */
	public static double factorial(int num)
	{
		double toReturn = 1;
		for(int i = num; i >0; i--)
			toReturn *= i;
		return toReturn;
	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	public static double abs(double x)
	{
		return x < 0 ? -1 * x : x;
	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	public static double rint(double x)
	{
		long round = (long)x;
		return x-round >= 5 ? (double) round++: (double)round;
	}
	
	/**
	 * 
	 * @param x
	 * @return
	 */
	public static int round(double x)
	{
		int round = (int)x;
		return x-round >= 0.5 ? round++: round;
	}
	
	public static double log(double x, double y)
	{
		return Math.log(x)/Math.log(y);
	}
	
	public static double calculate(String input) throws MathInputException
	{
		String toChange = input.trim();
		for(int i = 0; i < toChange.length(); i++)
			if(toChange.charAt(i) == ' ')
				toChange = toChange.substring(0, i) + toChange.substring(i+1);
		int fStart = -1;
		for(int i = 0; i < toChange.length(); i++)
		{
			char ch = toChange.charAt(i);
			if(fStart == -1 && ch > 96 && ch < 123)
				fStart = i;
			else if(ch == '(' || ch == '[')
			{
				boolean bracket = ch == '[';
				int lastIndex = -1;
				if(bracket)
				{
					int numParens = 0;
					for(int j = i+1; j < toChange.length(); j++)
					{
						if(toChange.charAt(j) == '[')
							numParens++;
						else if(toChange.charAt(j) == ']')
						{
							if(numParens == 0)
								lastIndex = j;
							else 
								numParens--;
						}
					}
					lastIndex = toChange.lastIndexOf(']');
					bracket = false;
				}
				else
				{
					int numParens = 0;
					for(int j = i+1; j < toChange.length(); j++)
					{
						if(toChange.charAt(j) == '(')
							numParens++;
						else if(toChange.charAt(j) == ')')
						{
							if(numParens == 0)
								lastIndex = j;
							else
								numParens--;
						}
					}
				}
				if(lastIndex == -1)
					throw new MathInputException("The function is not enclosed");	
				String a = toChange.substring(0, fStart == -1 ? i : fStart);
				char prevch = toChange.charAt(i-1);
				if(fStart == -1 && (prevch != '+' || prevch != '-' || prevch != '/' || prevch!= '%'))
					a+= '*';
				double ans = 0;
				if(fStart != -1)
				{
					ans = function(toChange.substring(fStart, lastIndex));
					fStart = -1;
				}
				else
					ans = calculate(toChange.substring(i+i, lastIndex));
				if(lastIndex >= toChange.length())
				{
					toChange = a + ans;
					i = toChange.length();
				}
				else
				{
					String b = toChange.substring(lastIndex+1);
					toChange = a + ans;
					i = toChange.length();
					toChange += b;
				}
			}
		}
		//Multiplication and division
		int start = 0;
		int end = 0;
		for(int i = 0; i < toChange.length(); i++)
		{
			char ch = toChange.charAt(i);
			if(ch == '*' || ch == '/')
			{
				if(i+1 == toChange.length())
					throw new MathInputException("An operation is missing a parameter");
				double n1 = convert(toChange.substring(start, i));
				double n2 = 0;
				boolean neg = toChange.charAt(i+1) == '-';
				int second = neg ? i+2: i+1;
				for(int j = second; j < toChange.length() && end == 0; j++)
				{
					char ch2 = toChange.charAt(j);
					if(!Character.isDigit(ch2) && ch2 != '.')
					{
						n2 = convert(toChange.substring(second, j));
						end = j;
					}
				}
				if(end == 0)
					n2 = convert(toChange.substring(second));
				double ans = ch == '*' ? n1*n2 : n1/n2;
				String a = start == 0 ? "" + ans : toChange.substring(0, start) + ans;
				if(start != 0)
					start = a.length();
				i = a.length()-1;
				if(end != 0)
					toChange = a + toChange.substring(end);
				else
					toChange = a;
				end = 0;
			}
			else if(ch == '+' || ch == '%')
			{
				if(i+1 == toChange.length())
					throw new MathInputException("An operation is missing a parameter");
				start = i+1;
			}
			else if(ch == '-')
			{
				if(i+1 == toChange.length())
					throw new MathInputException("An operation is missing a parameter");
				if(toChange.charAt(i+1) == '-')
				{
					if(i+2 == toChange.length())
						throw new MathInputException("An operation is missing a parameter");
					start = i+1;
					i++;
				}
				else
					start = i+1;
			}
		}
		//Addition and subtraction
		for(int i = 0; i < toChange.length(); i++)
			if(toChange.charAt(i) == '-' && i+1 < toChange.length() && toChange.charAt(i+1) == '-')
				toChange = toChange.substring(0, i) + '+' + toChange.substring(i+2);
		start = 0;
		end = 0;
		for(int i = 0; i < toChange.length(); i++)
		{
			char ch = toChange.charAt(i);
			if(ch == '+' || (ch == '-' && i != 0))
			{
				if(start == i)
					throw new MathInputException("An operation is missing a parameter");
				double n1 = convert(toChange.substring(start, i));
				double n2 = 0;
				for(int j = i+1; j < toChange.length() && end == 0; j++)
				{
					char ch2 = toChange.charAt(j);
					if(!Character.isDigit(ch2) && ch2 != '.')
					{
						n2 = convert(toChange.substring(i+1, j));
						end = j;
					}
				}
				if(end == 0)
					n2 = convert(toChange.substring(i+1));
				double ans = ch == '+' ? n1+n2 : n1-n2;
				String a = start == 0 ? "" + ans: toChange.substring(0, start) + ans;
				if(start != 0)
					start = a.length();
				i = a.length()-1;
				if(end != 0)
					toChange = a + toChange.substring(end);
				else
					toChange = a;
				end = 0;
			}
		}
		//Modulus
		start = 0;
		end = 0;
		for(int i = 0; i < toChange.length(); i++)
		{
			char ch = toChange.charAt(i);
			if(ch == '%')
			{
				if(start == i)
					throw new MathInputException("An operation is missing a parameter");
				double n1 = convert(toChange.substring(start, i));
				double n2 = 0;
				for(int j = i+1; j < toChange.length() && end == 0; j++)
				{
					char ch2 = toChange.charAt(j);
					if(!Character.isDigit(ch2) && ch2 != '.')
					{
						n2 = convert(toChange.substring(i+1, j));
						end = j;
					}
				}
				if(end == 0)
					n2 = convert(toChange.substring(i+1));
				double ans = n1 % n2;
				String a = start == 0 ? "" + ans: toChange.substring(0, start) + ans;
				if(start != 0)
					start = a.length();
				i = a.length()-1;
				if(end != 0)
					toChange = a + toChange.substring(end);
				else
					toChange = a;
				end = 0;
			}
		}
		return convert(toChange);
	}
	
	private static double convert(String str) throws MathInputException
	{
		try
		{
			boolean dot = false;
			for(int i = 0; i < str.length(); i++)
			{
				if(str.charAt(i) == '.' && !dot)
				{
					if(dot)
						throw new MathInputException("There are too many dots in a given argument");
					dot = true;
				}
			}
			double toReturn = Double.parseDouble(str);
			return toReturn;
		}
		catch(NumberFormatException e)
		{
			throw new MathInputException("Issue while parsing.");
		}
	}

	private static double function(String line) throws MathInputException
	{
		String functionName = line.substring(0,line.indexOf('('));
		String body = line.substring(line.indexOf('(') + 1);
		if(functionName.equalsIgnoreCase("round"))
			return round(calculate(body));
		else if(functionName.equalsIgnoreCase("pow"))
		{
			int num = body.indexOf(',');
			return Math.pow(calculate(body.substring(0, num)),calculate(body.substring(num+1)));
		}
		else if(functionName.equalsIgnoreCase("ln"))
			return Math.log(calculate(body));
		else if(functionName.equalsIgnoreCase("log"))
		{
			int num = body.indexOf(',');
			return log(calculate(body.substring(0, num)),calculate(body.substring(num+1)));
		}
		else if(functionName.equalsIgnoreCase("fact"))
		{
			double ans = calculate(body);
			if(isInt(ans))
				return factorial((int)calculate(body));
			else
				throw new MathInputException("The fact(x) function requires x to be an integer");
		}
		else if(functionName.equalsIgnoreCase("abs"))
			return abs(calculate(body));
		else if(functionName.equalsIgnoreCase("root"))
		{
			int num = body.indexOf(',');
			return root(calculate(body.substring(0, num)),calculate(body.substring(num+1)));
		}
		else
			throw new MathInputException("The given function isn't valid");
	}

	private static boolean isInt(double conv) throws MathInputException
	{
		return (int)conv == conv;
	}
}