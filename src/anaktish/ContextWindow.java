package anaktish;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JScrollPane;

public class ContextWindow {

	private JFrame frmContextwindow;
	private String text;
	private String wordOfInterest;


	public void NewScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ContextWindow window = new ContextWindow(text,wordOfInterest);
					window.frmContextwindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ContextWindow(String text,String wordOfInterest) {
		this.text = text;
		this.wordOfInterest = wordOfInterest;
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frmContextwindow = new JFrame();
		frmContextwindow.setTitle("Content");
		frmContextwindow.setBounds(100, 100, 1600, 800);
		frmContextwindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmContextwindow.getContentPane().setLayout(null);
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("<html><p style=\"width:1400\">");
		String[] splited = text.split(" ");
		
		for(String i : splited) {
			if ((i.toLowerCase()).equals(wordOfInterest.toLowerCase())) {
				sb.append("<font color='red'> " + i + " </font>");
			}else {
				sb.append(" " + i + " ");
			}
    	}
		sb.append("</p></html>");
		text = sb.toString();

		JLabel label = new JLabel(text);
		label.setBounds(12, 12, 1576, 839);

		frmContextwindow.getContentPane().add(label);
		
		
		
	}
}
