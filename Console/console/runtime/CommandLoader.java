package console.runtime;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import console.Command;
import console.display.Console;
import console.exceptions.ClassCommandLoadingException;
import lib.datatypes.EList;

public class CommandLoader 
{
	public CommandLoader(String defaultPath, Console con)
	{
		this.defaultPath = defaultPath;
		this.console = con;
	}
	
	public final EList<Class<Command>> loadAll() throws ClassCommandLoadingException
	{
		EList<Class<Command>> toReturn = new EList<>();
		File parentDir = new File(defaultPath+subDir);
		System.out.println(parentDir.getPath());
		if(!parentDir.exists())
			if(!parentDir.mkdir())
				console.output("Failed to make directory");
		if(!parentDir.isDirectory())
			throw new ClassCommandLoadingException();
		for(File f: parentDir.listFiles())
		{
			String path = f.getPath();
			try 
			{
				JarFile jarFile = new JarFile(path);
				Enumeration<JarEntry> e = jarFile.entries();

				URL[] urls = { new URL("jar:file:" + path+"!/") };
				URLClassLoader cl = URLClassLoader.newInstance(urls);

				while (e.hasMoreElements()) 
				{
				    JarEntry je = e.nextElement();
				    if(je.isDirectory() || !je.getName().endsWith(classExt))
				        continue;
				    String className = je.getName().substring(0,je.getName().length() - classExt.length());
				    className = className.replace('/', '.');
				    if(className.contains("java."))
				    	throw new ClassCommandLoadingException("Security thread detected in class " + className + ", aborting loading");
				    @SuppressWarnings("unchecked")
					Class<Command> c = (Class<Command>) cl.loadClass(className);
				    if(c.getSuperclass().equals(Command.class))
				    	toReturn.add(c);
				}
				jarFile.close();
			} 
			catch (IOException e1) 
			{
				throw new ClassCommandLoadingException(e1.getMessage());
			}
			catch (ClassNotFoundException e1)
			{
				throw new ClassCommandLoadingException(e1.getMessage());
			}
		}
		return toReturn;
	}
	
	private static final String classExt = ".class";
	private static final String subDir = "/Commands/";
	private final String defaultPath;
	private final Console console;
}
