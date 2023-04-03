package anaktish;

import java.awt.EventQueue;
import java.util.ArrayList;


import javax.swing.JFrame;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTabbedPane;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.ScoreDoc;

import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.Font;

public class MainMusicSearcher {

	private JFrame frame;
	private JTextField txtSearch;
	private JTable table;
	private JPanel panel;
	private static Lucene lucene;
	private static Directory index;
	private static StandardAnalyzer analyzer;
	private int result_counter = 0;
	private JButton btnNextPage;
	private JButton btnPrevPage;
	private ScoreDoc[] hits;
	private IndexSearcher searcher;

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
		

		lucene = new Lucene(MusicReview);
		
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
		
        // 3. search        
        
        analyzer = lucene.getAnalyzer();

        // 1. create the index
        index = lucene.getIndex();
		
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
		frame.setBounds(100, 100, 1366, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		txtSearch = new JTextField();
		txtSearch.setText("Search");
		txtSearch.setBounds(12, 12, 1202, 25);
		frame.getContentPane().add(txtSearch);
		txtSearch.setColumns(10);
		
		JButton btnNewButton = new JButton("Search");

		btnNewButton.setBounds(1226, 12, 117, 25);
		frame.getContentPane().add(btnNewButton);
		
		panel = new JPanel();
		panel.setBounds(12, 129, 1331, 542);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		table = new JTable();
		table.setFont(new Font("Dialog", Font.PLAIN, 18));
		table.setRowHeight(40);
		
		table.setBounds(0, 24, 1307, 518);
		panel.add(table);		
		
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"review_id", "content", "title", "artist", "url", "score", "author", "pub_date", "label"},
			},
			new String[] {
				"", "", "", "", "", "", "", "", ""
			}
		));
		
		btnNextPage = new JButton("next page");
		btnNextPage.setBounds(1238, 96, 105, 25);
		frame.getContentPane().add(btnNextPage);
		
		btnPrevPage = new JButton("prev page");

		btnPrevPage.setBounds(1108, 96, 117, 25);
		frame.getContentPane().add(btnPrevPage);
			
		
    	btnPrevPage.setEnabled(true);
    	btnNextPage.setEnabled(false);
		
		//event listeners
		
		
		btnPrevPage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		
		btnNextPage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					table.setModel(new DefaultTableModel(
							new Object[][] {
								{"review_id", "content", "title", "artist", "url", "score", "author", "pub_date", "label"}
							},
							new String[] {
								"", "", "", "", "", "", "", "", ""
							}
					));
					
			        for(int i=result_counter;i < result_counter+10;++i) {
			        	System.out.println(i);
			        	if (i == hits.length) {
			        		result_counter = i;
			        		btnNextPage.setEnabled(false);
			        		break;
			        	}
			            int docId = hits[i].doc;
			            Document d = searcher.doc(docId); 
			    		DefaultTableModel model = (DefaultTableModel) table.getModel();
			    		model.addRow(new Object[]{d.get("review_id"),d.get("content"),d.get("title"),d.get("artist"),d.get("url"),d.get("score")
			    				,d.get("author"),d.get("pub_date"),d.get("label")});
			        }
			        result_counter += 10;
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
			        Query q = new QueryParser("content", analyzer).parse(txtSearch.getText());
			        result_counter = 0;
			        
			        IndexReader reader = DirectoryReader.open(index);
			        searcher = new IndexSearcher(reader);
			        TopDocs docs = searcher.search(q, reader.numDocs());
			        hits = docs.scoreDocs;
			        
			        
			        if (hits.length > 10) {
			        	btnNextPage.setEnabled(true);
			        }
			        
					table.setModel(new DefaultTableModel(
							new Object[][] {
								{"review_id", "content", "title", "artist", "url", "score", "author", "pub_date", "label"}
							},
							new String[] {
								"", "", "", "", "", "", "", "", ""
							}
					));
			        
			        // 4. display results
			        System.out.println("Found " + hits.length + " hits.");
			        for(int i=0;i < 10;++i) {
			        	if (i == hits.length) {
			        		btnNextPage.setEnabled(false);
			        		break;
			        	}
			            int docId = hits[i].doc;
			            Document d = searcher.doc(docId);
			    		DefaultTableModel model = (DefaultTableModel) table.getModel();
			    		model.addRow(new Object[]{d.get("review_id"),d.get("content"),d.get("title"),d.get("artist"),d.get("url"),d.get("score")
			    				,d.get("author"),d.get("pub_date"),d.get("label")});
			    		
			        }
			        result_counter += 10;
					
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
}
//