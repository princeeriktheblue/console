package console;

import console.display.Console;

class Main 
{
	public static void main(String[] args)
	{
		try
        {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException e)
        {
            javax.swing.JOptionPane.showMessageDialog(null, "There was an internal error. Please re-install client", 
            		"Fatal Error", javax.swing.JOptionPane.ERROR_MESSAGE, null);
        }
        java.awt.EventQueue.invokeLater(() -> {
            new Console();
        });
	}
}
