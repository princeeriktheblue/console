package engine;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import engine.lang.ObjectVariable;
import engine.lang.exceptions.EngineLoadingException;
import lib.datatypes.EList;

public class ObjectLoader 
{
	public ObjectLoader(String defaultPath)
	{
		this.defaultPath = defaultPath;
	}
	
	public final EList<Class<ObjectVariable>> loadAll() throws EngineLoadingException
	{
		EList<Class<ObjectVariable>> toReturn = new EList<>();
		File parentDir = new File(defaultPath+subDir);
		if(!parentDir.exists())
			if(!parentDir.mkdir())
				System.out.println("Failed to make directory");
		if(!parentDir.isDirectory())
			throw new EngineLoadingException();
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
				    	throw new EngineLoadingException("Security thread detected in class " + className + ", aborting loading");
				    @SuppressWarnings("unchecked")
					Class<ObjectVariable> c = (Class<ObjectVariable>) cl.loadClass(className);
				    toReturn.add(c);
				}
				jarFile.close();
			} 
			catch (IOException e1) 
			{
				Engine.log.log(Level.WARNING, e1.getMessage(), e1);
				throw new EngineLoadingException(e1.getMessage());
			}
			catch (ClassNotFoundException e1)
			{
				Engine.log.log(Level.WARNING, e1.getMessage(), e1);
				throw new EngineLoadingException(e1.getMessage());
			}
		}
		return toReturn;
	}
	
	private static final String classExt = ".class";
	private static final String subDir = "/Objects/";
	private final String defaultPath;
}
