package anaktish;

import java.awt.EventQueue;
import org.apache.lucene.search.BooleanClause;
import java.util.ArrayList;


import javax.swing.JFrame;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;

import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.SortedNumericSortField;
import org.apache.lucene.search.TermQuery;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTabbedPane;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.ScoreDoc;

import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.Font;
import javax.swing.JCheckBox;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;

public class MainMusicSearcher {

	private JFrame frame;
	private JTextField txtSearch;
	private JTable table;
	private JPanel panel;
	private JComboBox comboBox;
	private static Lucene lucene;
	private static Directory index;
	private static StandardAnalyzer analyzer;
	private int result_counter = 0;
	private JButton btnNextPage;
	private JButton btnPrevPage;
	private ScoreDoc[] hits;
	private IndexSearcher searcher;
	private JComboBox comboBox_2;
	private JCheckBox chckbxDescending;
	private Sort sort;
	private TopDocs docs;
	private JCheckBox chckbxBestNewMusic;
	private JLabel lblNewLabel;

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
		txtSearch.setBounds(12, 12, 1074, 25);
		frame.getContentPane().add(txtSearch);
		txtSearch.setColumns(10);
		
		JButton btnNewButton = new JButton("Search");
		
		frame.getRootPane().setDefaultButton(btnNewButton);
		


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
				{"review_id", "content", "title", "artist", "url", "author", "pub_date", "label"},
			},
			new String[] {
				"", "", "", "", "", "", "", "", ""
			}
		));
		
		btnNextPage = new JButton("next page");
		btnNextPage.setBounds(1226, 99, 105, 25);
		frame.getContentPane().add(btnNextPage);
		
		btnPrevPage = new JButton("prev page");

		btnPrevPage.setBounds(1114, 99, 105, 25);
		frame.getContentPane().add(btnPrevPage);
			
		
    	btnPrevPage.setEnabled(true);
    	btnNextPage.setEnabled(false);
    	
    	String[] optionsToChoose = {"content", "review_id", "title", "artist", "url", "score", "author", "pub_date", "label"};
    	
    	comboBox = new JComboBox(optionsToChoose);
    	comboBox.setBounds(1098, 12, 116, 24);
    	frame.getContentPane().add(comboBox);
    	
    	JComboBox comboBox_1 = new JComboBox();
    	comboBox_1.setBounds(1056, 13, 30, 22);
    	frame.getContentPane().add(comboBox_1);
    	
    	String[] optionsToChoose2 = {"","score","publication date"};
    	comboBox_2 = new JComboBox(optionsToChoose2);
    	comboBox_2.setBounds(1070, 63, 144, 24);
    	frame.getContentPane().add(comboBox_2);
    	
    	chckbxDescending = new JCheckBox("ascending");
    	chckbxDescending.setBounds(1226, 64, 129, 23);
    	frame.getContentPane().add(chckbxDescending);
    	
    	JLabel lblSortBy = new JLabel("sort by");
    	lblSortBy.setBounds(1016, 65, 70, 20);
    	frame.getContentPane().add(lblSortBy);
    	
    	chckbxBestNewMusic = new JCheckBox("best new music");
    	chckbxBestNewMusic.setBounds(12, 64, 170, 23);
    	frame.getContentPane().add(chckbxBestNewMusic);
    	
    	lblNewLabel = new JLabel("New label");
    	lblNewLabel.setBounds(63, 102, 357, 22);
    	lblNewLabel.setVisible(false);
    	frame.getContentPane().add(lblNewLabel);
		
		//event listeners
		
		
		btnPrevPage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		
		table.addMouseListener(new MouseAdapter() {
			  public void mouseClicked(MouseEvent e) {

			        int row = table.getSelectedRow();
			        int col = table.getSelectedColumn();

			        if (col == 1 && row != 0) {
			        	ContextWindow contextwindow = new ContextWindow((String) table.getModel().getValueAt(row, col),txtSearch.getText());
			        	
			        	contextwindow.NewScreen();
			        }
			        
			        if (col == 4) {
				        URI uri = null;
						try {
							uri = new URI((String) table.getModel().getValueAt(row, col));
						} catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						


				        //see below
				        open(uri);
				      }
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
			    		model.addRow(new Object[]{WordColorer(d.get("review_id")),d.get("content"),WordColorer(d.get("title")),WordColorer(d.get("artist")),d.get("url"),WordColorer(d.get("score"))
			    				,WordColorer(d.get("author")),WordColorer(d.get("pub_date")),WordColorer(d.get("label"))});
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
				search();
			}
		});
		
		
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					search();
				}
			}
		});
		
		

	}
	
	
	public void search(){
		long startTime = System.nanoTime();
		
		
		try {
			String searchField = (String) comboBox.getItemAt(comboBox.getSelectedIndex());
			System.out.println(searchField);
	        Query q = new QueryParser(searchField, analyzer).parse(txtSearch.getText());
	        result_counter = 0;
	        
	        IndexReader reader = DirectoryReader.open(index);
	        searcher = new IndexSearcher(reader);
	        
			switch((String) comboBox_2.getSelectedItem()) {
			  case "score":
				sort = new Sort(new SortedNumericSortField("score_sort", SortField.Type.FLOAT, !chckbxDescending.isSelected()));
				
		        if (chckbxBestNewMusic.isSelected()) {
		        	Query query = new TermQuery(new Term(searchField, txtSearch.getText()));
			        Query filter = new TermQuery(new Term("best_new_music", "1"));
			        
			        BooleanQuery booleanQuery = new BooleanQuery.Builder()
			            .add(query, BooleanClause.Occur.MUST)
			            .add(filter, BooleanClause.Occur.FILTER)
			            .build();
			        
			        docs = searcher.search(booleanQuery, reader.numDocs(),sort);
		        }else {
		        	docs = searcher.search(q, reader.numDocs(),sort);
		        }
				
			    break;
			    
			  case "publication date":
				sort = new Sort(new SortField("pub_date_sorted", SortField.Type.INT,!chckbxDescending.isSelected()));
				docs = searcher.search(q, reader.numDocs(),sort);
				
				
		        if (chckbxBestNewMusic.isSelected()) {
		        	Query query = new TermQuery(new Term(searchField, txtSearch.getText()));
			        Query filter = new TermQuery(new Term("best_new_music", "1"));
			        
			        BooleanQuery booleanQuery = new BooleanQuery.Builder()
			            .add(query, BooleanClause.Occur.MUST)
			            .add(filter, BooleanClause.Occur.FILTER)
			            .build();
			        
			        docs = searcher.search(booleanQuery, reader.numDocs(),sort);
		        }else {
		        	docs = searcher.search(q, reader.numDocs(),sort);
		        }
				
			    break;
			    
			  default:
				if (chckbxBestNewMusic.isSelected()) {
		        	Query query = new TermQuery(new Term(searchField, txtSearch.getText()));
			        Query filter = new TermQuery(new Term("best_new_music", "1"));
			        
			        BooleanQuery booleanQuery = new BooleanQuery.Builder()
			            .add(query, BooleanClause.Occur.MUST)
			            .add(filter, BooleanClause.Occur.FILTER)
			            .build();
			        
			        docs = searcher.search(booleanQuery, reader.numDocs());
		        }else {
		        	docs = searcher.search(q, reader.numDocs());
		        }
			}
			
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
	        
	        long endTime = System.nanoTime();
	        lblNewLabel.setVisible(true);
	        DecimalFormat numberFormat = new DecimalFormat("#.###");
	        lblNewLabel.setText("About " + hits.length + " results (" + numberFormat.format((endTime - startTime)/1000000000f) + " seconds)");
	        for(int i=0;i < 10;++i) {
	        	if (i == hits.length) {
	        		btnNextPage.setEnabled(false);
	        		break;
	        	}
	            int docId = hits[i].doc;
	            Document d = searcher.doc(docId);
	    		DefaultTableModel model = (DefaultTableModel) table.getModel();
	    		model.addRow(new Object[]{WordColorer(d.get("review_id")),d.get("content"),WordColorer(d.get("title")),WordColorer(d.get("artist")),d.get("url"),WordColorer(d.get("score"))
	    				,WordColorer(d.get("author")),WordColorer(d.get("pub_date")),WordColorer(d.get("label"))});
	    		TableColumn tColumn = table.getColumnModel().getColumn(4);
	    		tColumn.setCellRenderer(new ColumnColorRenderer(Color.white, Color.blue));
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
	
	private static void open(URI uri) {
		  if (Desktop.isDesktopSupported()) {
		    try {
		       Desktop.getDesktop().browse(uri);
		      } catch (IOException e) { /* TODO: error handling */ }
		   } else { /* TODO: error handling */ }
	}
	
	private String WordColorer(String text) {
		String wordOfInterest = txtSearch.getText();
		StringBuilder sb = new StringBuilder();
		sb.append("<html><p style=\"width:1400\">");
		String[] splited = text.split(" ");
		
		for(String i : splited) {
			if (i.equals(wordOfInterest)) {
				sb.append("<font color='red'> " + i + " </font>");
			}else {
				sb.append(" " + i + " ");
			}
    	}
		sb.append("</p></html>");
		return sb.toString();
	}
	
	class ColumnColorRenderer extends DefaultTableCellRenderer {
		   Color backgroundColor, foregroundColor;
		   public ColumnColorRenderer(Color backgroundColor, Color foregroundColor) {
		      super();
		      this.backgroundColor = backgroundColor;
		      this.foregroundColor = foregroundColor;
		   }
		   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,   boolean hasFocus, int row, int column) {
		      Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		      cell.setBackground(backgroundColor);
		      cell.setForeground(foregroundColor);
		      return cell;
		   }
	}
}