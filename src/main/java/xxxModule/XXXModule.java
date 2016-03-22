/**
* Anonymous XXX Modules 
* e.g. Speech Recognition, Q&A System, Vision based Hand Raising Detection, Polling
* 
*
* @author  ADEM F. IDRIZ
* @version 1.0
* @since  2016 - TU Delft
*/

package xxxModule;

import pal.TECS.*;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;



public class XXXModule extends JFrame {

	private PALClient thePalClient;
	
//	 Vision
	public String [] positionArm =  {"LeftArmFor","RightArmFor"};
	public String msjV="";
	
//	Speech Interaction
	public String [] SpeechOutput =  {"What is robot?","I did not understand that point.",
									"Where is the train station?","Could you repeat once again?",
									"Can I touch you?", "It is boring!", "May I ask a question?",
									"Where are you from?","How old are you?", "Okey, I got it","What is a humanoid robot?"};
	public String msjS="";
	
	public int S;
	
//	QnA
	public String [] QnAOutput =  {"Robot is a virtually human in many regards.","Could you tell me which point was not clear for you ?",
									"The train station is just 100 meters away from here.","Which point you did not understand?",
									"Yes but lightly touch please.", "Okey, lets change subject!", "Sure, go ahead please.",
									"I belong to Earth.","3 years old.", "Well, that is good.","I will present that point in following slides. You will find your answer."};
	public String msjQnA="";
	
//	QnA
	public String [] PollingOutput =  {"A","B","C","D"};
	
	public String msjP="";
	
	
	public XXXModule(String clientName) {
		
		/*Initiate GUI */
		makeGUI();
		setSize(600, 500);
	    setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setTitle(clientName);
	
		
		/*Create Connection */
		connectClient(clientName);
	}
	

	  private void connectClient(final String clientName) {

		/*Manually assigned IP */
	    String tecsserver = "127.0.0.1";
	    
	    //String tecsserver = System.getProperty(TECSSERVER_IP_PROP);
	    thePalClient = new PALClient(tecsserver, clientName);
	    
	    // Start Listening messages 
	    thePalClient.startListening();

	 }
	
	// Create GUI
	private void makeGUI() {
		
		JLabel lblThisIsA = new JLabel("Module XXX");
		lblThisIsA.setHorizontalAlignment(SwingConstants.CENTER);
		lblThisIsA.setFont(new Font("Tahoma", Font.PLAIN, 40));
		getContentPane().add(lblThisIsA, BorderLayout.NORTH);
		
		// Panel
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		// First Button
		JButton btnAnswA = new JButton("Q&A");
		btnAnswA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int Q=S;
				msjQnA= "qna& "+ QnAOutput[Q]; 
				thePalClient.send(new XXXCommand(102, msjQnA ));
			}
		});
		
		btnAnswA.setBounds(213, 143 , 195, 64);
		panel.add(btnAnswA);
		
		// Second Button
		JButton btnAnswB = new JButton("Polling");
		btnAnswB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int P= (int) Math.floor( Math.random()*PollingOutput.length);
				msjP= "polling&"+ PollingOutput[P]; 
				thePalClient.send(new XXXCommand(102, msjP));


			}
		});
		btnAnswB.setBounds(213, 66, 195, 64);
		panel.add(btnAnswB);
		
		// Third Button
		JButton btnAnswC = new JButton("Speech Interaction");
		btnAnswC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				S= (int) Math.floor( Math.random()*SpeechOutput.length);
				msjS= "speechInteraction& "+ SpeechOutput[S]; 
				thePalClient.send(new XXXCommand(102, msjS ));

			}
		});
		btnAnswC.setBounds(213, 220, 195, 64);
		panel.add(btnAnswC);
		
		// Fourth Button
		JButton btnAnswD = new JButton("Vision based hand-raising detection");
		btnAnswD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int V=(Math.random()<0.5)?0:1;
				msjV= "vision&"+ positionArm[V]; 
				thePalClient.send(new XXXCommand(102, msjV));


			}
		});
		btnAnswD.setBounds(183, 297, 250, 64);
		panel.add(btnAnswD);
	}
}
