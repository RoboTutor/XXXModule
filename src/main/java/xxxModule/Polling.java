/**
* Polling Module 
*
* @author  ADEM F. IDRIZ
* @version 1.0
* @since  2016 - TU Delft
*/

package xxxModule;

import pal.TECS.*;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import java.awt.BorderLayout;


import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JPanel;


import java.net.*;
import java.io.*;
import java.util.*;


public class Polling extends JFrame {
	
	Toolkit toolkit;
	Timer timer;
    boolean tUp = false;

	private PALClient thePalClient;
	private JTextArea textWindow=new JTextArea();
	private int message = 0;
	private String PPTcommand = "";
	private int counter = 0;
	
	public Polling(String clientName) {
		
		makeGUI();
		setSize(600, 100);
	    setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
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
			
//			// Set timer
//			ReminderBeep(1);
	    }
	  };

			
	  public void ReminderBeep(int seconds) {
	    toolkit = Toolkit.getDefaultToolkit();
	    timer = new Timer();
	    timer.schedule(new RemindTask(), seconds * 1000);
	    
	  }

	  class RemindTask extends TimerTask {
	    public void run() {
	      System.out.println("Time's up!");
	      toolkit.beep();
	      tUp = true;
	      timer.cancel(); 
          // scheduled task 
			try {
			//		    	Poll Title: What's your favorite cupcake?  
			ConnectURL("www.polleverywhere.com", "https://www.polleverywhere.com/multiple_choice_polls/sKxNLqlMTJquXoC");
			
			//				Poll Title: Which of these, according to the definition, is not a robot?
			//				ConnectURL("www.polleverywhere.com", "https://www.polleverywhere.com/multiple_choice_polls/Mo5DyMDlIQNiKvs");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  
	    }
	  }
		  
	 public void ConnectURL(String argInetAddress, String argURL)throws Exception {
		 if (tUp) {
			 		tUp = false;
			 		//Check connection to POll Server 
			 		try {
			 			InetAddress addr;
			 			Socket sock = new Socket(argInetAddress, 80); 
						 addr = sock.getInetAddress();
						 System.out.println("Connected to " + addr);
						 textWindow.append("Connected to " + addr + System.getProperty("line.separator"));
						     sock.close();
						  } catch (IOException e) {
						     System.out.println("Can't connect to ");
						     System.out.println(e);
						  }
        
			 		//Connect  to poll url
			 		URL quiz = new URL(argURL+"/web.js");
			 
			        URLConnection result = quiz.openConnection();
			        BufferedReader in = new BufferedReader(new InputStreamReader(result.getInputStream()));
			        String inputLine;

			       
        
			        
			        ArrayList Keywords = new ArrayList();
			        
			        ArrayList Percentage = new ArrayList();
        
       
						while ((inputLine = in.readLine()) != null) {
		
							
						    System.out.println(inputLine);
						    
						    	int index_keyword=0;
	  	
							     // find all occurrences forward - keywords
						        for (int intKeyword = -1; (intKeyword = inputLine.indexOf("\"keyword\":\"", intKeyword + 1)) != -1; ) {
						            textWindow.append( inputLine.substring(intKeyword,intKeyword+13)+  System.getProperty("line.separator"));
						            Keywords.add(index_keyword, inputLine.substring(intKeyword+11,intKeyword+12));
						            index_keyword++;
						        } 

						        int index_Percentage=0;
						    	
						     // find all occurrences forward -percentages
						        for (int intPercentage = -1; (intPercentage = inputLine.indexOf("\"results_percentage\":", intPercentage + 1)) != -1; ) {
	
						            textWindow.append( inputLine.substring(intPercentage,intPercentage+26)+  System.getProperty("line.separator"));
						            
						            //Only digits
						            String str = inputLine.substring(intPercentage,intPercentage+26).replaceAll("[^0-9]", "");
						            //Convert string to integer
						            int percent = Integer.parseInt(str)/10;

						            Percentage.add(index_Percentage, percent);
						            index_Percentage++;
						        }					
						    
						}
						//	Close input stream 		     
						in.close();
						
						textWindow.append(Keywords+  System.getProperty("line.separator"));
						textWindow.append(Percentage + System.getProperty("line.separator"));
						// Find maximum value and index						
						int  indexMax = Percentage.indexOf(Collections.max(Percentage));
						textWindow.append(Collections.max(Percentage)+ System.getProperty("line.separator"));
						textWindow.append(indexMax + System.getProperty("line.separator"));
						textWindow.append(Keywords.get(indexMax) + System.getProperty("line.separator"));
						
						//Send polling message to TECS server
						thePalClient.send(new XXXCommand(102, "polling&"+ Keywords.get(indexMax)));
						textWindow.append("Sending message with id: 102 Content: [XXX Module:" + "polling" +
								"', Result = '" + Keywords.get(indexMax) + "']" + System.getProperty("line.separator"));
						 	}
						



	 		}
		  

		// Create GUI
		private void makeGUI() {
		
		// Panel
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		// Refresh Button
		JButton btnAnswB = new JButton("Refresh");
		btnAnswB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			ReminderBeep(1);

			}
		});
		btnAnswB.setBounds(450, 10, 100, 30);
		panel.add(btnAnswB);
		}
			  
}
