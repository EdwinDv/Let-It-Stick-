package Let.It.Stick;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.io.StringReader;

import javax.json.stream;

public class Dictionary {
	
	public static String getDefinition(String word) {
		
		JsonParser parser = Json.createParser(new StringReader(call(word)));
		
		Event event = parser.next(); 

	}
	
	public static String call(String word) {
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.dictionaryapi.dev/api/v2/entries/en/rip")).method("GET", HttpRequest.BodyPublishers.noBody()).build();
		
		HttpResponse<String> response = null;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return response.body().toString();
	}
	
	public static void main(String args[]) {
		call("fall");
	}

}
