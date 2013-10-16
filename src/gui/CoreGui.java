/**
 * @author Jon Friesen (www.jonfriesen.ca)
 */
package gui;
import misc.CalenderComboBox;

import obj.Message;

import org.mlc.swing.layout.*;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import comm.MXLookup;
import comm.SendMessage;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JLabel;
public class CoreGui extends JFrame{
	private JPanel panel = new JPanel();
	private Component contentSep = DefaultComponentFactory.getInstance().createSeparator("Email Content",SwingConstants.LEFT);
	private JLabel targetLabel = new JLabel("Target email");
	private JLabel fromNameLabel = new JLabel("Spoof name");
	private JTextField timeField = new JTextField();
	private CalenderComboBox cal = new CalenderComboBox();
	private JTextPane contentPane = new JTextPane();
	private JLabel subjectLabel = new JLabel("Subject");
	private JButton submitButton = new JButton("Send E-Mail!");
	private JLabel timeLabel = new JLabel("Time");
	private JTextField fromField = new JTextField();
	private JLabel contentLabel = new JLabel("Message");
	private JTextField fromNameField = new JTextField();
	private JLabel dateLabel = new JLabel("Date");
	private JTextField targetField = new JTextField();
	private JTextField subjectField = new JTextField();
	private Component addressingSep = DefaultComponentFactory.getInstance().createSeparator("Header Info",SwingConstants.LEFT);
	private JLabel fromAddressLabel = new JLabel("From email");
	
	public CoreGui(){
		setTitle("Jon's Email Spoofer");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initGUI();
	}
	
	private void initGUI(){
		contentPane.setBorder(BorderFactory.createLineBorder(new Color(205,201, 201, 255)));
		timeField.setEditable(false);
		// here is where we load the layout constraints.  change the xml filename!!!
		org.mlc.swing.layout.LayoutConstraintsManager layoutConstraintsManager = 
			org.mlc.swing.layout.LayoutConstraintsManager.getLayoutConstraintsManager(
					this.getClass().getResourceAsStream("layout.xml"));
		LayoutManager panelLayout = layoutConstraintsManager.createLayout ( "panel" , panel ) ;
		panel.setLayout(panelLayout);	
		// here we add the controls to the container.
		panel.add(contentSep, "contentSep");

		panel.add(targetLabel, "targetLabel");

		panel.add(fromNameLabel, "fromNameLabel");

		panel.add(timeField, "timeField");

		panel.add(cal, "dateCombo");

		panel.add(contentPane, "contentPane");

		panel.add(subjectLabel, "subjectLabel");

		panel.add(submitButton, "submitButton");

		panel.add(timeLabel, "timeLabel");

		panel.add(fromField, "fromField");

		panel.add(contentLabel, "contentLabel");

		panel.add(fromNameField, "fromNameField");

		panel.add(dateLabel, "dateLabel");

		panel.add(targetField, "targetField");

		panel.add(subjectField, "subjectField");

		panel.add(addressingSep, "addressingSep");

		panel.add(fromAddressLabel, "fromAddressLabel");

		add(panel);
		pack();
		setVisible(true);
		// control configuration
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
	}
	private void sendMessage(){
		Message msg = new Message();
		msg.setTo(targetField.getText());
		msg.setFromName(fromNameField.getText());
		msg.setFrom(fromField.getText());
		msg.setDate(cal.getDate().toString());
		msg.setSubject(subjectField.getText());
		msg.setContent(contentPane.getText());
		
		if(new SendMessage().sendIt(new MXLookup(msg.getTo()).getMXList(), msg)){
			JOptionPane.showMessageDialog(this, "Sent Successfully!");
			System.exit(0);
		} else {
			JOptionPane.showMessageDialog(this, "Failed to Send");
		}
	}

}
