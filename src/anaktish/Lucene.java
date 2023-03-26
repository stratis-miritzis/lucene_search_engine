package anaktish;

import java.io.IOException;
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
import org.apache.lucene.document.Field;



public class Lucene {

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
		
	       // 2. query
        String querystr = "eventually";

        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        Query q = new QueryParser("content", analyzer).parse(querystr);

        // 3. search
        
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;

        // 4. display results
        System.out.println("Found " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("review_id") + "\t" + d.get("url") + "\t" + d.get("title") + "\t" + d.get("content") );
        }

        // reader can only be closed when there
        // is no need to access the documents any more.
        reader.close();
	}
	
	private static void addDoc(IndexWriter w, MusicReview music) throws IOException {
		  Document doc = new Document();
		  doc.add(new StringField("review_id", music.getReview_id(), Field.Store.YES));
		  doc.add(new TextField("content", music.getContent(), Field.Store.YES));
		  doc.add(new TextField("title", music.getTitle(), Field.Store.YES));
		  doc.add(new TextField("url", music.getUrl(), Field.Store.YES));
		  w.addDocument(doc);
	}
	
	
	public void createDocument() throws IOException, ParseException {
		try {
	        
	        analyzer = new StandardAnalyzer();

	        // 1. create the index
	        index = new ByteBuffersDirectory();

	        config = new IndexWriterConfig(analyzer);

	        w = new IndexWriter(index, config);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
