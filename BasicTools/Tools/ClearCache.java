package Tools;

import java.io.File;
import java.util.ArrayList;

import console.Command;
import console.display.Console;
import console.exceptions.EException;
import lib.ESystem;

public class ClearCache extends Command
{
	public final static String[] help = 
	{
		"ClearCache --> No arguments, clears all system and local temp files"
	};

	public ClearCache(Console console) 
	{
		super("clearcache", console, null, help);
	}

	@Override
	public synchronized void execute(String[] args) throws EException 
	{
		int totalFiles = 0;
		long totalBytes = 0;
		if(console.getOSType() == ESystem.WINDOWS)
		{
			String appdata = System.getenv("appdata");
			ArrayList<FileRec> files = new ArrayList<>();
			files.add(new FileRec(new File(appdata.substring(0, appdata.length()-7) + "/Local/temp/"), false));
			files.add(new FileRec(new File("C:/Windows/Temp"), false));
			for(FileRec e: files)
			{
				e.deleteFiles();
				totalBytes += e.record.space;
				totalFiles += e.record.files;
			}
			console.output("");
			for(FileRec e: files)
				console.output(e.record.message);
			console.output("Total files deleted: " + totalFiles + ". Total space freed: " + makeResponse(totalBytes));
		}
		else if(console.getOSType() == ESystem.LINUX)
		{
			
		}
	}
	
	private synchronized String makeResponse(long decData)
	{
		double bytes = 0;
		if(decData > 1000000000)
		{
			bytes = decData/1000000000;
			String toShow = "" + bytes;
			if(toShow.length() > 5)
				toShow = toShow.substring(0, 5);
			return toShow + " GB of data";
		}
		else if(decData > 1000000)
		{
			bytes = decData/1000000;
			String toShow = "" + bytes;
			if(toShow.length() > 5)
				toShow = toShow.substring(0, 5);
			return toShow + " MB of data";
		}
		else if(decData > 1000)
		{
			bytes = decData/1000;
			String toShow = "" + bytes;
			if(toShow.length() > 5)
				toShow = toShow.substring(0, 5);
			return toShow + " KB of data";
		}
		else
		{
			bytes = decData;
			String toShow = "" + bytes;
			if(toShow.length() > 5)
				toShow = toShow.substring(0, 5);
			return toShow + " bytes of data";
		}
	}
	
	private final class FileRec
	{
		public FileRec(File file, boolean tempOnly)
		{
			this.file = file;
			this.tempOnly = tempOnly;
		}
		
		private final class DeletionRecord
		{
			public DeletionRecord(long space, int files, String message)
			{
				this.space = space;
				this.files = files;
				this.message = message;
			}
			
			private final long space;
			private final int files;
			private final String message;
		}
		
		public synchronized void deleteFiles()
		{
			String data = "";
			long bytes = 0;
			int numFiles = 0;
			File[] files = file.listFiles();
			if(tempOnly)
			{
				for(File e: files)
				{
					if(e.getName().contains(".log") || e.getName().contains(".tmp"))
					{
						long space = e.length();
						try
						{
							boolean deleted = e.delete();
							if(!deleted)
								console.output("Error in deleting file " + e.getName());
							bytes+=space;
							numFiles++;
						}
						catch(SecurityException f)
						{
							f.printStackTrace();
						}
					}
				}
			}
			else if(files != null)
			{
				for(File e: files)
				{
					long space = e.length();
					try
					{
						boolean deleted = e.delete();
						if(!deleted)
							console.output("Error in deleting file " + e.getName());
						bytes+=space;
						numFiles++;
					}
					catch(SecurityException f)
					{
						f.printStackTrace();
					}
				}
			}
			data = "Deleted " + numFiles + " of files out of " + file.getPath() + ", freeing " + makeResponse(bytes) + ESystem.newline;
			record = new DeletionRecord(bytes, numFiles, data);
		}
		
		private DeletionRecord record;
		private final File file;
		private final boolean tempOnly;
	}
}
