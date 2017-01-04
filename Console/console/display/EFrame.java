package console.display;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import console.Command;
import console.KeyOverride;
import console.commands.exceptions.InvalidDataException;
import lib.ESystem;
import lib.datatypes.EList;

public class EFrame extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public EFrame(Console con)
	{
		etext = new EText();
		this.con = con;
		last = -1;
		initComponents();
	}
	
	/**
	 * Sets the previous command list to the given index
	 * 
	 * @param last The index of the current command in the past command list
	 */
	public synchronized void setLast(int last)
    {
    	this.last = last;
    }
	
	/**
	 * Returns the EText object which is inside of this frame
	 * 
	 * @return The current EText object
	 */
    public EText getEText()
    {
    	return etext;
    }
	
	/**
	 * Initializes all components that aren't changed specifically by the preset
	 */
    private void initComponents() 
    {
    	ImageIcon img = new ImageIcon(System.getProperty("user.dir") + "/Console.png");
    	setIconImage(img.getImage());
    	
        scroll = new javax.swing.JScrollPane();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        etext.setColumns(20);
        etext.setLineWrap(true);
        etext.setRows(5);
        etext.setCaretColor(new java.awt.Color(51, 255, 51));
        etext.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        etext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) 
            {
            	mouseCheck(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) 
            {
            	mouseCheck(evt);
            }
        });
        etext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
            	keyAnalyze(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
            	evt.consume();
            }
        });
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                con.close();
            }
        });
        scroll.setViewportView(etext);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        pack();
    }
    
    /**
	 * Analyzes the key event and handles it. Fires the key event for any command currently running.
	 * 
	 * @param evt The key event
	 */
    private synchronized void keyAnalyze(KeyEvent evt)
    {
    	if(evt.isControlDown() && evt.isShiftDown() && evt.getKeyCode() == KeyEvent.VK_C)
    		con.stopExec();
    	if(!con.inputDisabled())
    	{
    		int code = evt.getKeyCode();
    		EList<String> pastCommands = con.getPastCommands();
    		Command running = con.getRunning();
    		boolean cont = true;
    		if(con.isRunning() && running instanceof KeyOverride)
    			cont = ((KeyOverride)running).keyTyped(evt);
    		if(cont)
    		{
    			if(code == KeyEvent.VK_UP)
    			{
    				evt.consume();
    				if(last > 0)
    				{
    					last--;
    		            etext.setAppended(pastCommands.get(last));
    		        }
    		        etext.fixCaret(true);
    		    }
    			else if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_BACK_SPACE)
    			{
    				evt.consume();
    				if(etext.caretBeforeData() || etext.isAtLength())
    					etext.fixCaret(false);
    				else
    					etext.backspace();
		        }
	    		else if(code == KeyEvent.VK_ENTER)
	            {
	    			con.enterPressed();
	                evt.consume();
	                if(!etext.getAppended().isEmpty())
	                {
	                	String text = etext.getAppended();
	                	etext.clearAppended();
	                	con.exec(text, false);
	                }
	                else
	                {
	                	etext.setAppended("");
	                	etext.fixCaret(true);
	                }
	            }
	            else if(code == KeyEvent.VK_DOWN)
		        {
		            evt.consume();
		            if(last < pastCommands.size()-1)
		            {
		                last++;
		                etext.setAppended(pastCommands.get(last));
		                etext.fixCaret(true);
		            }
		            else
		            {
		            	if(last == pastCommands.size() - 1)
		            		last++;
		            	etext.clearAppended();
		            }
		        }
	            else if(code == KeyEvent.VK_TAB)
		        {
		        	evt.consume();
		            tabExec();
		        }
	            else
	            {
	            	evt.consume();
	            	char ch = evt.getKeyChar();
	            	if(ch != KeyEvent.CHAR_UNDEFINED)
	            		etext.append(ch + "");
	            }
    		}
        }
    	else
    		evt.consume();
    }
	
    /**
     * The mouse event fired to check the mouse position
     * 
     * @param evt
     */
    private synchronized void mouseCheck(MouseEvent evt)
    {
    	etext.checkCaret();
    }
    
    /**
     * Executes for a tab event
     */
    private synchronized void tabExec()
    {
    	String temp = etext.getAppended();
		try 
		{
			String[] args = con.getArguments(temp, "");
			if(args.length != 0)
				if(args.length == 1)
					appendCommand(args);
				else
					appendPath(args, temp);
		} 
		catch (InvalidDataException e) 
		{}
    }
    
    /**
     * Appends the command with what is currently relevant in the current directory.
     * 
     * @param args The words which are currently in the command line
     */
    private synchronized void appendCommand(String[] args)
    {
    	EList<String> found = new EList<>();
		for(String e: con.getCommandNames())
			if(args[0].length() <= e.length() && e.substring(0, args[0].length()).equals(args[0]))
				found.add(e);
		if(found.size() == 1)
			etext.append(found.get(0).substring(args[0].length()), false);
		else if(found.size() > 1)
		{
			String toShow = "";
			for(int i = 0; i < found.size(); i++)
				toShow += found.get(i) + '	';
			etext.append(toShow, false);
		}
    }
    
    /**
     * Appends the current relevant path onto the current line.
     * 
     * @param args The current list of words typed
     * @param temp The temporary line
     */
    private synchronized void appendPath(String[] args, String temp)
    {
    	EList<String> found = new EList<>();
		int index = args[args.length-1].lastIndexOf('/');
		File[] files = null;
		String toUse = args[args.length-1];
		if(index != -1)
		{
			String pathTo = args[args.length-1].substring(0, index+1);
			String fullPath = ESystem.getNaturalPath(con.getCurrentDirectory());
			fullPath += pathTo.charAt(0) == '/' ? pathTo : '/' + pathTo;
			File dir = new File(fullPath);
			if(dir.exists() && dir.isDirectory())
				files = dir.listFiles();
			else
			{
				dir = new File(pathTo);
				if(dir.exists() && dir.isDirectory())
					files = dir.listFiles();
			}
			toUse = args[args.length-1].substring(index+1);
		}
		else
			files = con.getCurrentDirectory().listFiles();
		if(files != null)
		{
			for(File e: files)
				if(e.getName().length() >= toUse.length() && e.getName().substring(0, toUse.length()).equals(toUse))
					found.add(e.getName());
			if(found.size() == 1)
				etext.append(found.get(0).substring(toUse.length()), false);
			else if(found.size() > 1)
			{
				String toShow = "";
				for(int i = 0; i < found.size(); i++)
					toShow += found.get(i) + '	';
				con.output(ESystem.newline + toShow + ESystem.newline);
				con.displayCurrentLine(false);
				etext.append(temp, false);
			}
		}
    }
    
    private volatile int last;
    private volatile JScrollPane scroll;
    private final Console con;
    private final EText etext;
}
