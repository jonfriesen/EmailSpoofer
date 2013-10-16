package misc;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicArrowButton;



public class CalenderComboBox extends Box implements ActionListener{

	private static final long serialVersionUID = 1L;
	private boolean change = false;

	private static Border bLine = BorderFactory.createLineBorder(Color.black);		
	private BasicArrowButton comboBtn = new BasicArrowButton( SwingConstants.SOUTH ); 
    private static Dimension comboBtnSize = new Dimension(8, 8);
    private static DateDialog dateDialog = new DateDialog();
    private Date date;
    private JLabel  dateLabel;
    private Vector<CalenderComboBoxListener> vector;

    public CalenderComboBox() {  
    	super(BoxLayout.X_AXIS);
        // la box au centre
        JLabel label = new JLabel("Date:    ");
        add(label);
        // la date java.sql celle du jour
        date = new Date(System.currentTimeMillis());
        dateLabel = new JLabel("  " + date.toString() + "  ");
        dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dateLabel.setOpaque(true);
        dateLabel.setBackground(Color.WHITE);
        dateLabel.setBorder(bLine);
        add(dateLabel);
        // un espace avant le boutton pour "expand" le combo
        add(Box.createHorizontalStrut(2));
        comboBtn.addActionListener(this);
        comboBtn.setMaximumSize(comboBtnSize);
        comboBtn.setBackground(Color.WHITE);
        comboBtn.setForeground(Color.BLUE);
        add(comboBtn);
        add(Box.createHorizontalGlue());
        // determine de facon tout a fait empirique
        setMaximumSize(new Dimension(150, 26));
    }

    public java.util.Date getDate(){
    	return date;
    }
    
    public void setDate(Date newDate) {
    	date = newDate;
    	dateLabel.setText("  " + date.toString() + "  ");
    	change = true;
    }

    public boolean getChange(){
    	return change;
    }

    public void dateChanged(Date date) {
    	setDate(date);
    	if(vector == null)
    		return;
    	for(CalenderComboBoxListener listener : vector) {
    	}
    }

    public void addListener(CalenderComboBoxListener listener) {
    	if(vector == null)
    		vector = new Vector<CalenderComboBoxListener>(5, 2);
    	vector.add(listener);
    }
    

    public void actionPerformed( ActionEvent e ){
    	dateDialog.setLocationRelativeTo(this);
    	dateDialog.show(this, date);   
    }
}


