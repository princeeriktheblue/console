package console;

import java.awt.event.KeyEvent;

public interface KeyOverride 
{
	/**
	 * The key event to be overridden by the command currently running. Has an option of whether the main console
	 * should still analyze the same key event, or if it should ignore it.
	 * 
	 * @param evt The key event
	 * @return <code>true</code> if the console should continue with it's own processing of the key event, <code>false</code> otherwise
	 */
	public boolean keyTyped(KeyEvent evt);
}
