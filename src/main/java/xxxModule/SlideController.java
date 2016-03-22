/**
* PPTController Module 
*
* @author  ADEM F. IDRIZ
* @version 1.0
* @since  2016 - TU Delft
*/

package xxxModule;

import pal.TECS.*;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class SlideController extends JFrame {

	private PALClient thePalClient;
	private JTextArea textWindow=new JTextArea();
	private int message = 0;
	private String PPTcommand = "";
	private int counter = 0;
	
	public SlideController(String clientName) {
		
		
		/*Initiate textWindow */
		setLayout(new BorderLayout());
		add(new JScrollPane(textWindow));
		setSize(600, 500);
	    setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setTitle(clientName);
		
		/*Create Connection and Subscription */
		connectClientandSubscribe(clientName);
		
	}
	
	  private void connectClientandSubscribe(final String clientName) {

		/*Manually assigned IP */
		String tecsserver = "127.0.0.1";
		//	    String tecsserver = System.getProperty(TECSSERVER_IP_PROP);
		thePalClient = new PALClient(tecsserver, clientName);
		    
		/*Subscription */
		thePalClient.subscribe(palConstants.SlideControlMsg, palEventHandler_SlideControl);
		
		
		// Start Listening messages 
		thePalClient.startListening();
	  }
	
	 // Handle type of SlideControlMsg 
	  EventHandler<SlideControlCommand> palEventHandler_SlideControl = new EventHandler<SlideControlCommand>() {
		    public void handleEvent(SlideControlCommand event) {
		    	counter++;
		    	message = ((SlideControlCommand) event).SlideNumber;
		    	
		    	PPTcommand = ((SlideControlCommand) event).command;
		    	
		    	System.out.println("Slide Number: "+ message + "  &  " + "PPTcommand: " + PPTcommand);
				textWindow.append(counter+ "- Slide Number: " + message + "  &  " + "PPTcommand: " + PPTcommand +  System.getProperty("line.separator"));
		    }
		  };
	
}
