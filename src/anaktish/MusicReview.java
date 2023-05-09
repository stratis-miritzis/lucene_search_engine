package anaktish;

public class MusicReview {
	

	private String review_id;
	private String content;
	private String title;
	private String artist;
	private String url;
	private String score;
	private String best_new_music;
	private String author;
	private String author_type;
	private String pub_date;
	private String pub_day;
	private String pub_month;
	private String pub_year;
	private String label;

	public MusicReview(String[] columns) {
		super();
		
		this.review_id = columns[0];
		this.content = columns[1];
		this.title = columns[2];
		this.artist = columns[3];
		this.url = columns[4];
		this.score = columns[5];
		this.best_new_music = columns[6];
		this.author = columns[7];
		this.pub_date = columns[9];
		this.pub_day = columns[11];
		this.pub_month = columns[12];
		this.pub_year = columns[13];
		this.label = columns[14];
	}


	public String getReview_id() {
		return review_id;
	}

	public String getContent() {
		return content;
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public String getUrl() {
		return url;
	}

	public String getScore() {
		return score;
	}

	public String getBest_new_music() {
		return best_new_music;
	}

	public String getAuthor() {
		return author;
	}

	public String getAuthor_type() {
		return author_type;
	}

	public String getPub_date() {
		return pub_date;
	}

	public String getPub_day() {
		return pub_day;
	}

	public String getPub_month() {
		return pub_month;
	}

	public String getPub_year() {
		return pub_year;
	}

	public String getLabel() {
		return label;
	}

	
}
