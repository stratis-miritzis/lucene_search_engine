package anaktish;

import java.awt.EventQueue;
import java.util.ArrayList;


import javax.swing.JFrame;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainMusicSearcher {

	private JFrame frame;
	

	/**
	 * Launch the application.
	 * @throws Exception 
	 */
	private static ArrayList<MusicReview> convert(ArrayList<String[]> columns){
		ArrayList<MusicReview> returnMusic;
		returnMusic = new ArrayList<MusicReview>();
		
		for (String[] reviewData : columns){
			MusicReview musicReview = new MusicReview(reviewData);
			
			returnMusic.add(musicReview);
		}
		return returnMusic;
		
	}
	
	public static void main(String[] args) throws Exception {
		Parser parser = new Parser("/home/stratis/Desktop/anaktisi/reviews.csv");
		ArrayList<String[]> musicColumns = parser.getColumns();
		ArrayList<MusicReview> MusicReview = convert(musicColumns);
		

		Lucene lucene = new Lucene(MusicReview);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMusicSearcher window = new MainMusicSearcher();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainMusicSearcher() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTextArea txtrMusicReviewSearcher = new JTextArea();
		txtrMusicReviewSearcher.setText("Music Review Searcher");
		frame.getContentPane().add(txtrMusicReviewSearcher, BorderLayout.NORTH);
	}

}
