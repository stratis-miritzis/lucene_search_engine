package anaktish;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class MainMusicSearcher {

	public static void main(String[] args) throws Exception {
		Parser parser = new Parser("/home/stratis/Desktop/anaktisi/reviews.csv");

		parser.parse();
		ArrayList<String[]>arrlist = parser.getColumns();

		
	}

}
