package anaktish;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Embeddings {
	private HttpURLConnection connection;
	
    public Embeddings() {}
    
    public String apiCall(String query){
    	try{
    	    URL url = new URL("http://localhost:5000/simular");
    	
    	    // Create a connection to the API server
    	    connection = (HttpURLConnection) url.openConnection();
    	    
    	    connection.setRequestMethod("POST");
    	    
    	    connection.setRequestProperty("Content-Type", "application/json");
    	    connection.setDoOutput(true);
    	
		    // Create the JSON payload
		    String jsonInputString = "{\"string\":  \""+query+"\"}";
		
		    // Send the JSON payload to the server
		    try (OutputStream outputStream = connection.getOutputStream()) {
		        byte[] input = jsonInputString.getBytes("utf-8");
		        outputStream.write(input, 0, input.length);
		    }
		
		    // Get the response from the server
		    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		    StringBuilder response = new StringBuilder();
		    String line;
		    while ((line = reader.readLine()) != null) {
		        response.append(line);
		    }
		    reader.close();
		
		    // Print the response
		    System.out.println(response.toString());
		    return response.toString();
		    
		} catch (Exception e) {
			return "";
		}
    	
    }
		

}