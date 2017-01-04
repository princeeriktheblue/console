package console.display;

import javax.swing.JTextArea;

import lib.ESystem;

/**
 * A specific implementation of the <code>JTextArea</code> object given by the basic JVM, this class is specifically implemented to support
 * the <code>Console</code> object which implements this code. The basic implementation is as follows:
 * <br>
 * <br>
 * <b>Appending:</b>
 * <br>
 * Appends to the basic DATA which is stored. However; this data is NOT saved as permanent, and can be cleared by the 
 * <code>clearAppended</code> method. That being said, appended data is added on top of previously appended values each time the
 * method is called. Additionally, instead of adding onto the appending data, you can set the appending data to a specific value to
 * override and remove all previously appended data. The <code>finalizeData</code> method then solidifies appending data to the main data,
 * therefore reseting the appending data field, allowing for compound appending options.
 * <br>
 * <br>
 * <b>Temporary clearing:</b>
 * <br>
 * Temporary clearing works as follows: whenever you would like to save the previous data of the text object, instead of having to deal
 * with the different appending data and text data, the class provides a provided mechanism to do such a deal. Calling the <code>tempClear</code>
 * method temporarily clears the data on the screen, and allows for another layer of text on top of the previous, without losing it. After, 
 * calling the </code>restore</code> method, the temporary, additional layer of text is removed and the original text is put back in place,
 * appending data included.
 * <br>
 * <br>
 * <b>Caret Checking:</b>
 * <br>
 * Additionally, this class provides caret checking to supplement the previously mentioned appending mechanism. These methods allow for
 * implementations of this object to be able to determine WHAT SPECIFIC GROUP OF DATA the user is currently selected, rather than having
 * to check the caret position with the data length. Additionally, there are methods such as <code>fixCaret</code> which allow for the
 * caret to be set at the end of the base data, or at the end of the appending data as well.
 * <br>
 * <br>
 * The more specific methods to the original implementation of this class are as follows. The <code>setPath</code> method allows for
 * a user to specify a path to be set. Then, when calling the paired method </code>insertPath</code>, that set path is then put into the
 * main data after calling <code>finalize</code>. 
 * <br>
 * <br>
 * Other, more generic methods, are provided for ease of access. The <code>length</code> replaces getting the text and getting the length of
 * the text with one method, rather than two compiled together. Additionally, the <code>length</code> method allows for inclusion of appending
 * data, therefore adding onto the use of the appending mechanism. The <code>newLine</code> method adds a new line escape character to the 
 * appending data, instead of calling the append method with an escape character.
 * 
 * @author Erik
 * @see Console
 * @see ESystem
 * @see JTextArea
 *
 */
public class EText extends JTextArea
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new <code>EText</code> object
	 */
	public EText()
	{
		data = "";
		appended = "";
		temp = "";
		tempAppended = "";
		path = "";
		layered = false;
	}
	
	@Override
	public void setText(String str)
	{
		data = str;
	}
	
	@Override
	public void append(String str)
	{
		append(str, false);
	}
	
	/**
	 * Appends the given text to the end of the document. Does nothing if the model is null or the string is null or empty.
	 * 
	 * @param str the text to insert
	 * @param newLine <code>true</code> to add a new line at the end of this appending, <code>false</code> otherwise
	 */
	public void append(String str, boolean newLine)
	{
		if(str != null)
		{
			appended += str + (newLine ? ESystem.newline : "");
			setData(true);
		}
	}
	
	/**
	 * Adds a new line to the appending data
	 */
	public void newLine()
	{
		append(ESystem.newline);
	}
	
	/**
	 * Sets the default path of the EText object
	 * @param path The path to set
	 */
	public void setPath(String path)
	{
		this.path = path;
		appended = "";
	}
	
	/**
	 * Finalizes the appending data, then adds the currently saved path to the text data
	 * @param newline <code>true</code> to add a new line at the end of this appending, <code>false</code> otherwise
	 */
	public void insertPath(boolean newline)
	{
		if(newline)
			appended += ESystem.newline;
		appended += path;
		finalizeData();
		setData(false);
	}
	
	/**
	 * Clears the text and appending data.
	 * @param includePath <code>true</code> to add the path after clearing, <code>false</code> otherwise
	 */
	public void clear(boolean includePath)
	{
		data = includePath ? path : "";
		appended = "";
		setData(false);
	}
	
	/**
	 * Saves the appending data to the current data
	 */
	public void finalizeData()
	{
		data += appended;
		appended = "";
	}
	
	/**
	 * Temporarily clears the EText text data, saving it to temporary storage inside of the object.
	 * @param includePath Whether to include the path after clearing
	 * @return The data which was temporarily cleared, <code>null</code> if the text is already overridden
	 */
	public String tempClear(boolean includePath)
	{
		if(!layered)
		{
			temp = data;
			tempAppended = appended;
			clear(includePath);
			return temp + tempAppended;
		}
		return null;
	}
	
	/**
	 * Sets the appending data, overwriting any data currently in the appending data pool
	 * @param str The data to append
	 */
	public void setAppended(String str)
	{
		appended = str;
		setData(true);
	}
	
	/**
	 * Clears the appending data pool
	 */
	public void clearAppended()
	{
		appended = "";
		setData(false);
	}
	
	/**
	 * Restores the previous data from before the <code>tempClear</code> method
	 * 
	 * @return The data which is being overwritten, <code>null</code> if the <code>tempClear<code> method was not called earlier
	 */
	public String restore()
	{
		if(layered)
		{
			String current = data + appended;
			data = temp;
			appended = tempAppended;
			setData(true);
			return current;
		}
		return null;
	}
	
	public void fixCaret(boolean complete)
	{
		setCaretPosition(complete ? length() : data.length());
	}
	
	public boolean caretBeforeData()
	{
		return getCaretPosition() < data.length();
	}
	
	public boolean isAtLength()
	{
		return getCaretPosition() == data.length();
	}
	
	public boolean caretAtEndOfData()
	{
		return getCaretPosition() == length();
	}
	
	public void checkCaret()
	{
		if(getCaretPosition() < data.length())
			fixCaret(false);
	}
	
	public int length()
	{
		return data.length() + appended.length();
	}
	
	public String getAppended()
	{
		appended = getText().substring(data.length());
		return appended;
	}
	
	public void backspace()
	{
		if(!appended.isEmpty())
		{
			appended = appended.substring(0, appended.length()-1);
			setData(true);
		}
	}
	
	private void setData(boolean ap)
	{
		super.setText(data + (ap ? appended : ""));
	}
	
	private boolean layered;
	private String temp;
	private String tempAppended;
	private String appended;
	private String data;
	private String path;
}
