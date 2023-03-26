package anaktish;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Parser {
	
	
	
	private ArrayList<String[]> columns;
	private Path file;
	
	
	public Parser(String systemPath) throws Exception {
		
		this.file = Paths.get(systemPath);
		
		this.columns = new ArrayList<String[]>();
		
		this.parse();
	}

	public void parse() throws Exception {
		int lineNum = 1;
		System.out.println(file);
		try(BufferedReader br = Files.newBufferedReader(file)){
			
			String header = br.readLine();
			String line = br.readLine();

			
			while (line != null) {
			
				String[] columns = line.split("~");
				if(columns.length != 15) {
//					System.out.println(lineNum + " " + line);
//					System.out.println(columns.length);
					line = br.readLine();
					continue;
				}
				
				this.columns.add(columns);
				lineNum+=1;
				
				
				line = br.readLine();
				
			}
		}
	}
	
	public ArrayList<String[]> getColumns(){
		return this.columns;
	}

}
