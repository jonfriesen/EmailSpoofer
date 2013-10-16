package misc;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import java.sql.Date;
import java.util.Calendar;

import javax.swing.*;

import javax.swing.border.*;
import javax.swing.plaf.basic.BasicArrowButton;



public class DateDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private static final String[] monthsStr = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	private Calendar calendar;
	private DatePanel datePanel;	
    private BasicArrowButton nextBtn, prevBtn;
    private static Dimension btnSize = new Dimension(10, 8);
    private JLabel monthAndYear, selectedDateLabel;
    public CalenderComboBox calendarComboBox;
    private boolean[] isDateAvailable = new boolean[32];
    private Date date;
    private DatePanel.DateLabel lastBordered;
    private int dateYear, dateMonth, dateDay;


	public DateDialog() {
		super((JFrame) null, "Date", true);
		calendar = Calendar.getInstance();
		monthAndYear = new JLabel();
		monthAndYear.setHorizontalAlignment(SwingConstants.CENTER);
	    prevBtn = new BasicArrowButton( SwingConstants.WEST);
	    nextBtn = new BasicArrowButton( SwingConstants.EAST); 
	    prevBtn.setSize(btnSize);
	    nextBtn.setSize(btnSize);
        prevBtn.addActionListener(this);
        nextBtn.addActionListener(this);
	    selectedDateLabel = new JLabel();
	    selectedDateLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    selectedDateLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
	    datePanel = new DatePanel();
		getContentPane().add(datePanel);
		setSize(180, 200);
	}

	public void show(CalenderComboBox calendarComboBox, Date date) {
		this.calendarComboBox = calendarComboBox;
		this.date = date;
		selectedDateLabel.setText(" " + date.toString() + " ");
		calendar.setTime(date);
		dateYear = calendar.get(Calendar.YEAR);
		dateMonth = calendar.get(Calendar.MONTH) + 1;
		dateDay = calendar.get(Calendar.DAY_OF_MONTH);
		reset();
		setVisible(true);
	}
	private void reset() {
		calendar.setTime(date);
		int m = calendar.get(Calendar.MONTH);
		int y = calendar.get(Calendar.YEAR);
		monthAndYear.setText("  " + monthsStr[m] + " - " + y + "  ");

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int startIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int nbJours = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		for(int j = 1; j <= nbJours; j++) {
			isDateAvailable[j] = true;
		}
		for(int j = nbJours+1; j < isDateAvailable.length; j++)
			isDateAvailable[j] = false;
		datePanel.reset(startIndex, nbJours);
	}

	public void actionPerformed(ActionEvent e) {
		BasicArrowButton b = (BasicArrowButton) e.getSource();
		calendar.setTime(date);
		int delta;
		if(b == prevBtn) 
			delta = -1;				
		else 
			delta = +1;				
		calendar.add(Calendar.MONTH, delta);
		date = Date.valueOf(new Date(calendar.getTimeInMillis()).toString());
		reset();
	}

	public void hideDialog() {
		setVisible(false);
	}
	
	
    private static final String[] daysStr = {"S", "M", "T", "W", "TH", "F", "S"};
    private static final String[] joursAscii = {" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
    	                                        "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

    private Border border = BorderFactory.createLineBorder(Color.BLACK);
    private Border selectedBorder = BorderFactory.createLineBorder(Color.BLUE, 2);


	public class DatePanel extends JPanel {
		private static final long serialVersionUID = 2L;
		private DateLabel[] dateLabel = new DateLabel[7*6];
		private Font regularFont, smallFont;

		DatePanel() {
			super(new GridLayout(9, 1, 1, 1));
			setBackground(Color.WHITE);
			Box box = Box.createHorizontalBox();
			box.add(prevBtn);
			box.add(Box.createHorizontalGlue());
			box.add(selectedDateLabel);
			box.add(Box.createHorizontalGlue());
			box.add(nextBtn);
			add(box);

			add(monthAndYear);

			JPanel linePanel = new JPanel(new GridLayout(1,7));
			linePanel.setBackground(Color.WHITE);
			JLabel[] dLabel = new JLabel[7];
			for(int i = 0; i < 7; i++) {
				dLabel[i] = new JLabel(daysStr[i]);
				dLabel[i].setHorizontalAlignment(SwingConstants.RIGHT);
				linePanel.add(dLabel[i]);
			}
			regularFont = dLabel[0].getFont();
			smallFont = regularFont.deriveFont(regularFont.getStyle() ^ Font.BOLD);	
			dLabel[0].setFont(smallFont);
			add(linePanel);
			
			int k = 0;
			for(int i = 0; i < 6; i++) {							
				linePanel = new JPanel(new GridLayout(1,7));
				linePanel.setBackground(Color.WHITE);
				for(int j = 0; j < 7; j++) {
					dateLabel[k] = new DateLabel();
					linePanel.add(dateLabel[k++]);
				}
				add(linePanel);										
			}
		}
		private void reset(int from, int nb) {
			if(lastBordered != null)
				lastBordered.setBorder(null);
			for(int i = 0; i < dateLabel.length; i++) {
				dateLabel[i].setText(0);
				dateLabel[i].setFont(regularFont);
			}
			int k = 0;
			for(int i = 0; i < nb; i++) {
				dateLabel[from].setText(++k);
				if(from % 7 == 0)
					dateLabel[from].setFont(smallFont);
				from++;
			}
		}

		public class DateLabel extends JLabel {
			private static final long serialVersionUID = 3L;
			private boolean available;
			private int day;
			private boolean selected;
			
			DateLabel() {
				setHorizontalAlignment(SwingConstants.RIGHT);
				addMouseListener(new DateLabelMouseListener(this));
			}

			public void setText(int n) {
				day = n;
				super.setText(joursAscii[day]);				
				available = isDateAvailable[day];				

				selected = false;
				setBorder(null);
				Color color;
				if(available) {
					color = Color.BLACK;
					if(calendar.get(Calendar.YEAR) == dateYear) {
						if(calendar.get(Calendar.MONTH) + 1 == dateMonth) {
							if(day == dateDay) {
								selected = true;
								setBorder(selectedBorder);
							}
						}
					}
					setFont(regularFont);
				}
				else {
					color = Color.LIGHT_GRAY;
					setFont(smallFont);
				}
				setForeground(color);
				
			}

			public class DateLabelMouseListener implements MouseListener {
				DateLabel label;
				

				DateLabelMouseListener(DateLabel label) {
					this.label = label;
				}
				

				public void mouseClicked(MouseEvent e) {
					if(!label.available)
						return;
					calendar.set(Calendar.DAY_OF_MONTH, label.day);
					calendarComboBox.dateChanged(Date.valueOf(new Date(calendar.getTimeInMillis()).toString()));
					hideDialog();
				}

				public void mouseEntered(MouseEvent e) {
					if(lastBordered != null) {
						lastBordered.setBorder(null);
						if(lastBordered.selected)
							lastBordered.setBorder(selectedBorder);
					}
					lastBordered = label;
					if(label.available)
						label.setBorder(border);
				}

				public void mouseExited(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {}
				public void mouseReleased(MouseEvent e) {}
				
			}
		}
	}
}

