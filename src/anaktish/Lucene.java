package anaktish;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FloatDocValuesField;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;






public class Lucene {

	public IndexWriter getW() {
		return w;
	}

	public StandardAnalyzer getAnalyzer() {
		return analyzer;
	}

	public Directory getIndex() {
		return index;
	}

	public IndexWriterConfig getConfig() {
		return config;
	}


	private IndexWriter w;
	private StandardAnalyzer analyzer;
	private Directory index;
    private IndexWriterConfig config = new IndexWriterConfig(analyzer);

    
	public Lucene(ArrayList<MusicReview> MusicReview) throws IOException, ParseException, org.apache.lucene.queryparser.classic.ParseException {

		createDocument();
		
		for (MusicReview review : MusicReview){
			addDoc(w, review);
		}
		w.close();
     
	}
	
	private static void addDoc(IndexWriter w, MusicReview music) throws IOException {
		  Document doc = new Document();
		  doc.add(new StringField("review_id", music.getReview_id(), Field.Store.YES));
		  doc.add(new TextField("content", music.getContent(), Field.Store.YES));
		  doc.add(new TextField("title", music.getTitle(), Field.Store.YES));
		  doc.add(new TextField("artist", music.getArtist(), Field.Store.YES));
		  doc.add(new StringField("url", music.getUrl(), Field.Store.YES));
		  doc.add(new StringField("score", music.getScore(), Field.Store.YES));
		  doc.add(new FloatDocValuesField("score_sort", Float.parseFloat(music.getScore())));
		  doc.add(new TextField("best_new_music", music.getBest_new_music(), Field.Store.YES));
		  doc.add(new TextField("author", music.getAuthor(), Field.Store.YES));
		  doc.add(new TextField("pub_date", music.getPub_date(), Field.Store.YES));
		  int days = Integer.parseInt(music.getPub_day()) + 30 * Integer.parseInt(music.getPub_month()) + 365 * Integer.parseInt(music.getPub_year());
		  doc.add(new NumericDocValuesField("pub_date_sorted", days));
		  doc.add(new SortedDocValuesField("pub_day", new BytesRef(music.getPub_day())));
		  doc.add(new SortedDocValuesField("pub_month", new BytesRef(music.getPub_month())));
		  doc.add(new SortedDocValuesField("pub_year", new BytesRef(music.getPub_year())));
		  doc.add(new StringField("label", music.getLabel(), Field.Store.YES));

		  
		  
		  w.addDocument(doc);
	}
	
	
	public void createDocument() throws IOException, ParseException {
		try {
	        
	        analyzer = new StandardAnalyzer();

	        // 1. create the index
//	        Path indexPath = Files.createTempDirectory("tempIndex");
//	        index = FSDirectory.open(indexPath);
	        
	        index = new ByteBuffersDirectory();

	        config = new IndexWriterConfig(analyzer);

	        w = new IndexWriter(index, config);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
